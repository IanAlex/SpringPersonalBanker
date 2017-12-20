package com.ija.pb.services;

import java.util.*;
import java.math.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.joda.time.MutableDateTime;

import com.ija.pb.hibernate.tables.*;
import com.ija.pb.hibernate.dao.*;

@Service
public class SavingsServiceImpl implements SavingsService {
	
	@Autowired
	@Qualifier("savingsDao")
	private SavingsDao savingsDao;
	@Autowired
	@Qualifier("savingsTxnDao")
	private SavingsTxnDao savingsTxnDao;
	@Autowired
	@Qualifier("savingsRateDao")
	private SavingsRateDao savingsRateDao;
	
	private static final String OPEN_ACCOUNT = "O";
	private static final String DEPOSIT = "D";
	private static final String WITHDRAWAL = "W";
	private static final String TRANSFER_IN = "TI";
	private static final String TRANSFER_OUT = "TO";
	private static final String INTEREST = "I";
	private static final String ALL = "ALL";
	
	private static final String DEBIT = "D";
	private static final String CREDIT = "C";

	@Override
	public double calcBalance(Set<SavingsTxn> savingsTxns) {
		if (savingsTxns != null & !savingsTxns.isEmpty()) {
			double total = 0;
			for (SavingsTxn savingsTxn: savingsTxns) {
				if (!savingsTxn.getReversed()) {
					total += (savingsTxn.getDrCr().equals("D") ? 1 : -1) * savingsTxn.getAmount().doubleValue();
				}
			}
			return total;
		}
		else {
			return (0);
		}		
	}
	
	@Override
	public SavingsTxn createTxn(Double amount, String savingsTxnType, Savings savings, 
							Date txnDt, Integer offsetTxnId, Account offsetAccount)
	{
		SavingsTxn savingsTxn = new SavingsTxn();
		Date sysDt = new Date();
		savingsTxn.setAmount(amount);
		savingsTxn.setSavingsTxnType(savingsTxnType);
		savingsTxn.setSavings(savings);
		savingsTxn.setTxnAcctType("SV");
		savingsTxn.setTxnDt(txnDt);
		savingsTxn.setReversed(Boolean.FALSE);
		savingsTxn.setDtCreated(sysDt);
		savingsTxn.setDtChanged(sysDt);
		savingsTxn.setUpdatedBy("SYSTEM");
		if (savingsTxnType.equals(DEPOSIT) || savingsTxnType.equals(TRANSFER_IN) ||
			  savingsTxnType.equals(INTEREST) || savingsTxnType.equals(OPEN_ACCOUNT)) 
		{
			savingsTxn.setDrCr(DEBIT);
		}
		else {
			savingsTxn.setDrCr(CREDIT);
		}
		if (savingsTxnType.equals(TRANSFER_IN) || savingsTxnType.equals(TRANSFER_OUT) || 
				savingsTxnType.equals(OPEN_ACCOUNT)) 
		{
			savingsTxn.setOffsetAccount(offsetAccount);
			savingsTxn.setOffsetTxnId(offsetTxnId);
		}
		savingsTxnDao.store(savingsTxn);
		// re-do the savings interest Txns to reflect the injection of non-interest Dr/Cr
		if (!savingsTxnType.equals(INTEREST)) {
			savings = savingsDao.findById(savings.getAccountId());
			generateInterestTxns(savings, txnDt);
		}
		return savingsTxn;		
	}
	
	public void updateRunningTotals(Savings savings) {
		List<SavingsTxn> savingsTxns = savingsTxnDao.findWithinTxnDtRangeByType(savings, savings.getOpenDt(), new Date(), ALL, "ASC");
		SavingsTxn oldTxn = null;
		double runningBalance = 0;
		boolean updateFlag = false;
		for (SavingsTxn aSavingsTxn : savingsTxns) {
			updateFlag = false;
			if (oldTxn != null && oldTxn.getReversed().booleanValue() ) {
				if (oldTxn.getRunningAcctBalance() != null) {
					oldTxn.setRunningAcctBalance(null);
					updateFlag = true;
				}
			}
			else if (oldTxn != null && aSavingsTxn.getTxnDt().compareTo(oldTxn.getTxnDt()) != 0 && 
						(oldTxn.getRunningAcctBalance() == null || oldTxn.getRunningAcctBalance().doubleValue() != runningBalance)) 
			{
					oldTxn.setRunningAcctBalance(new Double(runningBalance));
					updateFlag = true;
			}
			else if (oldTxn != null && aSavingsTxn.getTxnDt().compareTo(oldTxn.getTxnDt()) == 0 && 
							 oldTxn.getRunningAcctBalance() != null) 
			{
				oldTxn.setRunningAcctBalance(null);
				updateFlag = true;
			}
			if (updateFlag) {
				savingsTxnDao.store(oldTxn);
			}
			if (!aSavingsTxn.getReversed().booleanValue()) {
				runningBalance += (aSavingsTxn.getDrCr().equals("D") ? 1 : -1) * aSavingsTxn.getAmount().doubleValue();
			}
			oldTxn = aSavingsTxn;
		}
		if (oldTxn != null && oldTxn.getReversed().booleanValue() && oldTxn.getRunningAcctBalance() != null) {
			oldTxn.setRunningAcctBalance(null);
			updateFlag = true;
		}
		else if (oldTxn != null && !oldTxn.getReversed().booleanValue() && 
				(oldTxn.getRunningAcctBalance() == null || oldTxn.getRunningAcctBalance().doubleValue() != runningBalance))
		{
			oldTxn.setRunningAcctBalance(new Double(runningBalance));
			updateFlag = true;
		}
		if (updateFlag) {
			savingsTxnDao.store(oldTxn);
		}
	}
	
	@Override
	public Savings generateInterestTxns(Savings inSavings, Date inDt) {
		Savings outSavings;		
		Date sysDt = new Date();
		// inDt is the valuation date we set curDt to the beginning of tht month if not before
		// the savings account open date. If opening date of savings account is after 1st of txn 
		// month then set the curDt to the savings account opening date.
		Date curDt = null;
		MutableDateTime curDtTime = new MutableDateTime(inDt);
		System.out.println("hi ian 555.07 dayOfMonth = " + curDtTime.getDayOfMonth());
		curDtTime.addDays(1 - curDtTime.getDayOfMonth());
		//MutableDateTime prelimValDtTime = new MutableDateTime(inDt);
		//prelimValDtTime.addDays(-1);
		Calendar calCur = new GregorianCalendar();
		calCur.setTimeInMillis(curDtTime.getMillis());
		curDt = calCur.getTime();	
		if (curDt.before(inSavings.getOpenDt())) {
			curDt = inSavings.getOpenDt();
			curDtTime = new MutableDateTime(inSavings.getOpenDt());
		}
		Date prelimValDt = null;
		MutableDateTime prelimValDtTime = (MutableDateTime) curDtTime.clone();
		prelimValDtTime.addDays(-1);
		Calendar calPrelimVal = new GregorianCalendar();
		calPrelimVal.setTimeInMillis(prelimValDtTime.getMillis());
		prelimValDt = calPrelimVal.getTime();
		//start with balance of all txns as at the prelimDt (or savings acct open dt if later)
		double balance = 0;
		if (!prelimValDt.before(inSavings.getOpenDt())) {
			List<SavingsTxn> savingsAllPrelimTxnsList = savingsTxnDao.findWithinTxnDtRangeByType(inSavings, inSavings.getOpenDt(), prelimValDt, ALL, null);
			if (savingsAllPrelimTxnsList != null) {
				Set<SavingsTxn> savingsAllPrelimTxnsSet = new HashSet<SavingsTxn>();
				savingsAllPrelimTxnsSet.addAll(savingsAllPrelimTxnsList);
				balance = calcBalance(savingsAllPrelimTxnsSet);
			}
		}
		List<SavingsTxn> savingsAllOrderedTxns = savingsTxnDao.findWithinTxnDtRangeByType(inSavings, curDt, sysDt, ALL, "ASC");
		if (savingsAllOrderedTxns == null) {
			savingsAllOrderedTxns = new ArrayList<SavingsTxn>();
		}
		List<SavingsTxn> savingOrderedNonInterestTxns = new ArrayList<SavingsTxn>();
		List<SavingsTxn> savingOrderedInterestTxns = new ArrayList<SavingsTxn>();
		for (SavingsTxn aSavingsTxn : savingsAllOrderedTxns) {
			if (aSavingsTxn.getSavingsTxnType().equals(INTEREST)) {
				savingOrderedInterestTxns.add(aSavingsTxn);
			}
			else {
				// only account for non-reversed txns
				if (!aSavingsTxn.getReversed().booleanValue()) {
					savingOrderedNonInterestTxns.add(aSavingsTxn);
				}
			}
		}
		List<SavingsRate> savingsRatesPrelim = savingsRateDao.findOrderByEffectiveDt(inSavings);
		List<SavingsRate> savingsRates = new ArrayList<SavingsRate>();	
		SavingsRate oldSavingsRate = null;
		boolean hitCurDt = false;
		for (SavingsRate aSavingsRate : savingsRatesPrelim) {
			if (!aSavingsRate.getEffectiveDt().before(curDt)) {
				if (oldSavingsRate != null && !hitCurDt) {
					savingsRates.add(oldSavingsRate);
				}
				savingsRates.add(aSavingsRate);
				hitCurDt = true;
			}
			else {
				oldSavingsRate = aSavingsRate;
			}
		}
		if (savingsRates.isEmpty()) {
			savingsRates.add(savingsRatesPrelim.get(savingsRatesPrelim.size()-1));
		}
		int nonInterestIdx = 0;
		int interestIdx = 0;
		int savRtIdx = 0;
		double curInterest = savingsRates.get(savRtIdx).getInterestRate().doubleValue();
		Date nextSavRtDt;
		if ((savRtIdx + 1) >= savingsRates.size()) {
			nextSavRtDt = sysDt;
		}
		else {
			nextSavRtDt = savingsRates.get(savRtIdx + 1).getEffectiveDt();
		}		
		//double balance = 0;
		double interestForMonth = 0;
		while (!curDt.after(sysDt)) { 
			if (!curDt.before(nextSavRtDt) && (curDt.compareTo(sysDt) != 0)) {
				savRtIdx++;
				curInterest = savingsRates.get(savRtIdx).getInterestRate().doubleValue();
				if ((savRtIdx + 1) >= savingsRates.size()) {
					nextSavRtDt = sysDt;
				}
				else {
					nextSavRtDt = savingsRates.get(savRtIdx + 1).getEffectiveDt();
				}
			}
			int tIdx = nonInterestIdx;
			for (int i = tIdx; i < savingOrderedNonInterestTxns.size(); i++) {
				if (curDt.compareTo(savingOrderedNonInterestTxns.get(i).getTxnDt()) == 0) {
						balance += savingOrderedNonInterestTxns.get(i).getAmount().doubleValue() *
							    	((savingOrderedNonInterestTxns.get(i).getDrCr().equals(DEBIT)) ? 1.0 : -1.0);
						nonInterestIdx++;
				}
				else {
					break;
				}
			}
			interestForMonth += balance * (Math.pow(1.0 + curInterest, 1.0/365.0) - 1.0);
			balance += balance * (Math.pow(1.0 + curInterest, 1.0/365.0) - 1.0);
			MutableDateTime tomorrow = (MutableDateTime) curDtTime.clone();
			tomorrow.addDays(1);
			if (tomorrow.getDayOfMonth() == 1  && interestForMonth > 0) {
				boolean insertIntFlag = false;
				Calendar cal = new GregorianCalendar();
				cal.setTimeInMillis(curDtTime.getMillis());
				Date endMoDt = cal.getTime();
				SavingsTxn existInterestTxn = null;
				for (int i = interestIdx; i < savingOrderedInterestTxns.size(); i++) {
					if (endMoDt.compareTo(savingOrderedInterestTxns.get(i).getTxnDt()) == 0) {
						existInterestTxn = savingOrderedInterestTxns.get(i);					
					}
					else {
						interestIdx = i;
						break;
					}
				}
				if (existInterestTxn != null) {
					BigDecimal intForMthBD = new BigDecimal(interestForMonth);
					intForMthBD = intForMthBD.setScale(2, RoundingMode.HALF_EVEN);
					BigDecimal existIntTxnAmt = new BigDecimal(existInterestTxn.getAmount().doubleValue());
					existIntTxnAmt = existIntTxnAmt.setScale(2, RoundingMode.HALF_EVEN);
					if (interestForMonth != existInterestTxn.getAmount().doubleValue())
					{
						savingsTxnDao.delete(existInterestTxn.getTxnId().intValue());
						insertIntFlag = true;
						
					}
					else {
						insertIntFlag = false;
					}
				} 
				else {
					insertIntFlag = true;
				}
				if (insertIntFlag) {
					createTxn(new Double(interestForMonth), INTEREST, inSavings, 
							endMoDt, null, null);
				}
				interestForMonth = 0;
			}
			curDtTime.addDays(1);
			Calendar calA = new GregorianCalendar();
			calA.setTimeInMillis(curDtTime.getMillis());
			curDt = calA.getTime();
		}
		outSavings = savingsDao.findById(inSavings.getAccountId());
		outSavings.setBalance(calcBalance(outSavings.getSavingsTxns()));
		return  outSavings;
	}

}

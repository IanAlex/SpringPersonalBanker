package com.ija.pb.services;

import java.util.*;

import org.joda.time.DateTime;
import org.joda.time.Days;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ija.pb.hibernate.tables.*;
import com.ija.pb.hibernate.dao.*;
import com.ija.pb.nondaodata.*;

@Service
public class AnnuityServiceImpl implements AnnuityService {
	
	@Autowired
	@Qualifier("annuityDao")
	private AnnuityDao annuityDao;
	@Autowired
	@Qualifier("annuityTxnDao")
	private AnnuityTxnDao annuityTxnDao;
	@Autowired
	@Qualifier("chequingService")
	private ChequingService chequingService;
	@Autowired
	@Qualifier("savingsService")
	private SavingsService savingsService;
	
	private static final String OPEN_BUY = "O";
	private static final String PAYMENT = "P";
	private static final String SELL = "S";
	private static final String TRANSFER_IN = "TI";
	private static final String ALL = "ALL";
	
	private static final String DEBIT = "D";
	private static final String CREDIT = "C";

	@Override
	public AnnuityTxn createTxn(Double amount, String annuityTxnType,
			Annuity annuity, Date txnDt, Integer offsetTxnId,
			Account offsetAccount) {
		AnnuityTxn annuityTxn = new AnnuityTxn();
		Date sysDt = new Date();
		annuityTxn.setAmount(amount);
		annuityTxn.setAnnuityTxnType(annuityTxnType);
		annuityTxn.setAnnuity(annuity);
		annuityTxn.setTxnAcctType("AN");
		annuityTxn.setReversed(Boolean.FALSE);
		annuityTxn.setTxnDt(txnDt);
		annuityTxn.setDtCreated(sysDt);
		annuityTxn.setDtChanged(sysDt);
		annuityTxn.setUpdatedBy("SYSTEM");
		if (annuityTxnType.equals(OPEN_BUY)) {
			annuityTxn.setDrCr(DEBIT);
		}
		else {
			annuityTxn.setDrCr(CREDIT);
		}
		annuityTxn.setOffsetAccount(offsetAccount);
		annuityTxn.setOffsetTxnId(offsetTxnId);
		annuityTxnDao.store(annuityTxn);
		return annuityTxn;		
	}
	
	@Override
	public List<AnnuityPayout> calcAnnuityPayout(Annuity annuity, Date valuationDt) {
		List<AnnuityPayout> annuityPayouts = new ArrayList<AnnuityPayout>();
		int noMthBtwPay = 12 / annuity.getPayfrequency().intValue();
		int noMthCount = noMthBtwPay;
		double effAnnualInterest = Math.pow(1.0 + annuity.getInterestRate().doubleValue() / annuity.getIntCompoundfrequency().doubleValue(), annuity.getIntCompoundfrequency().doubleValue()) - 1.0;
		double i = 1;
		DateTime dateTimeOpen = new DateTime(annuity.getOpenDt());
		while (annuity.getTermMonths().intValue() >= noMthCount) {
			AnnuityPayout annuityPayout = new AnnuityPayout();
			annuityPayout.setPvAmount(annuity.getPeriodicPayment().doubleValue() * (1.0 / Math.pow(1.0 + effAnnualInterest, i / annuity.getPayfrequency().doubleValue())));
			annuityPayout.setPayDate(dateTimeOpen.plusMonths(noMthCount).toDate());
			if (!valuationDt.before(annuityPayout.getPayDate())) {
				annuityPayout.setPaid(Boolean.TRUE);
			}
			else {
				annuityPayout.setPaid(Boolean.FALSE);
			}
			annuityPayouts.add(annuityPayout);
			noMthCount += noMthBtwPay;
			i++;
		}
		return annuityPayouts;
	}
	
	@Override
	public double calcPV(Annuity annuity, Date valuationDt) {
		double pv = 0;
		List<AnnuityPayout> annuityPayouts = calcAnnuityPayout(annuity, valuationDt);		
		int durationDaysOpenToVal = Days.daysBetween(new DateTime(annuity.getOpenDt()), new DateTime(valuationDt)).getDays();
		double effAnnualInterest = Math.pow(1.0 + annuity.getInterestRate().doubleValue() / annuity.getIntCompoundfrequency().doubleValue(), annuity.getIntCompoundfrequency().doubleValue()) - 1.0;
		double valDtFVFactor = Math.pow(1.0 + effAnnualInterest, (durationDaysOpenToVal + 0.0)/365.0);
		for (AnnuityPayout annuityPayout : annuityPayouts) {
			pv += annuityPayout.getPvAmount().doubleValue() * (annuityPayout.getPaid().booleanValue() ? 0.0 : 1.0) ;
		}
		return (valDtFVFactor * pv);
	}
	
	@Override
	public void updateRunningTotals(Annuity annuity) {
		List<AnnuityTxn> annuityTxns = annuityTxnDao.findWithinTxnDtRangeByType(annuity, annuity.getOpenDt(), new Date(), ALL, "ASC");
		AnnuityTxn oldTxn = null;
		double runningBalance = 0;
		boolean updateFlag = false;
		for (AnnuityTxn aAnnuityTxn : annuityTxns) {
			updateFlag = false;
			if (oldTxn != null && oldTxn.getReversed().booleanValue() ) {
				if (oldTxn.getRunningAcctBalance() != null) {
					oldTxn.setRunningAcctBalance(null);
					updateFlag = true;
				}
			}
			else if (oldTxn != null && aAnnuityTxn.getTxnDt().compareTo(oldTxn.getTxnDt()) != 0 && 
						(oldTxn.getRunningAcctBalance() == null || oldTxn.getRunningAcctBalance().doubleValue() != runningBalance)) 
			{
					oldTxn.setRunningAcctBalance(new Double(runningBalance));
					updateFlag = true;
					annuityTxnDao.store(oldTxn);
			}
			else if (oldTxn != null && aAnnuityTxn.getTxnDt().compareTo(oldTxn.getTxnDt()) == 0 && 
							 oldTxn.getRunningAcctBalance() != null) 
			{
				oldTxn.setRunningAcctBalance(null);
				updateFlag = true;
			}
			if (updateFlag) {
				annuityTxnDao.store(oldTxn);
			}
			if (!aAnnuityTxn.getReversed().booleanValue()) {
				runningBalance += (aAnnuityTxn.getDrCr().equals("D") ? 1 : -1) * aAnnuityTxn.getAmount().doubleValue();
			}
			oldTxn = aAnnuityTxn;
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
			annuityTxnDao.store(oldTxn);
		}
	}
	
	public void updateForAnnuityPayments(Annuity inAnnuity, Date inDt) {
		int txnNewCnt = 0;
		List<AnnuityPayout> annuityPayouts = calcAnnuityPayout(inAnnuity, inDt);
		List<AnnuityTxn> annuityTxns = annuityTxnDao.findWithinTxnDtRangeByType(inAnnuity, inAnnuity.getOpenDt(), inDt, "ALL", null);
		for (AnnuityPayout annuityPayout : annuityPayouts) {
			if (annuityPayout.getPaid().booleanValue()) {
				boolean txnExist = false;
				for (AnnuityTxn aTxn : annuityTxns) {
					if (aTxn.getAnnuityTxnType().equals(PAYMENT) && aTxn.getAmount().equals(inAnnuity.getPeriodicPayment())  && 
							(aTxn.getTxnDt().compareTo(annuityPayout.getPayDate()) == 0) )
					{
						txnExist = true;
					}
				}
				if (!txnExist) {  //create annuity payment txn
					AnnuityTxn annuityTxnNew = createTxn(inAnnuity.getPeriodicPayment(), PAYMENT, inAnnuity, 
															annuityPayout.getPayDate(), null, inAnnuity.getPayAccount());
					if (annuityTxnNew != null) {
						Txn offsetTxn = null;
						if (inAnnuity.getPayAccount() != null) {
							if (inAnnuity.getPayAccount() instanceof Chequing) {
								offsetTxn = chequingService.createTxn(inAnnuity.getPeriodicPayment(), TRANSFER_IN, null,
																	(Chequing) inAnnuity.getPayAccount(), 
																	annuityPayout.getPayDate(),
																	annuityTxnNew.getTxnId(), inAnnuity);
							}
							else if (inAnnuity.getPayAccount() instanceof Savings) {
								offsetTxn = savingsService.createTxn(inAnnuity.getPeriodicPayment(), TRANSFER_IN, 
																	(Savings) inAnnuity.getPayAccount(), 
																	annuityPayout.getPayDate(),
																	annuityTxnNew.getTxnId(), inAnnuity);
							}
						}
						if (offsetTxn != null) {
							annuityTxnNew.setOffsetTxnId(offsetTxn.getTxnId());
						}
						inAnnuity.getAnnuityTxns().add(annuityTxnNew);
						txnNewCnt++;
						System.out.println("hi ian 111.7 create annuity payment txn: " + txnNewCnt );
					}
					
				}
			}
		}
		if (txnNewCnt > 0 ) {
			annuityDao.store(inAnnuity);
		}
}		

}

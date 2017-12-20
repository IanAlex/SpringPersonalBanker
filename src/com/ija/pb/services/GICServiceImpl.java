package com.ija.pb.services;

import java.util.*;

import org.joda.time.DateTime;
import org.joda.time.Days;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ija.pb.hibernate.tables.*;
import com.ija.pb.hibernate.dao.*;


@Service
public class GICServiceImpl implements GICService {

	@Autowired
	@Qualifier("gicTxnDao")
	private GICTxnDao gicTxnDao;
	
	private static final String OPEN_ACCOUNT = "O";
	private static final String FUNDS_OUT = "W";
	private static final String MATURITY = "M";
	private static final String ALL = "ALL";
	
	private static final String DEBIT = "D";
	private static final String CREDIT = "C";
	
	@Override
	public double calcBalance(Set<GICTxn> gicTxns) {
		if (gicTxns != null & !gicTxns.isEmpty()) {
			double total = 0;
			for (GICTxn gicTxn: gicTxns) {
				if (!gicTxn.getReversed()) {
					total += (gicTxn.getDrCr().equals("D") ? 1 : -1) * gicTxn.getAmount().doubleValue();	
				}
			}
			return total;
		}
		else {
			return (0);
		}		
	}
	
	@Override
	public double calcMaturityValue(GIC gic) {
		double balance = calcBalance(gic.getGicTxns());
		double effAnnualInterest = Math.pow(1.0 + gic.getInterestRate().doubleValue() / gic.getIntCompoundfrequency().doubleValue(), gic.getIntCompoundfrequency().doubleValue()) - 1.0;
		int durationDays = Days.daysBetween(new DateTime(gic.getOpenDt()), new DateTime(gic.getMaturityDt())).getDays();
		double maturityValue = balance * Math.pow((1.0 + effAnnualInterest), (durationDays + 0.0)/(365 + 0.0));		
		return maturityValue;
	}
	
	@Override
	public double calcMaturityValue(GIC gic, double openAmt) {
		double effAnnualInterest = Math.pow(1.0 + gic.getInterestRate().doubleValue() / gic.getIntCompoundfrequency().doubleValue(), gic.getIntCompoundfrequency().doubleValue()) - 1.0;
		int durationDays = Days.daysBetween(new DateTime(gic.getOpenDt()), new DateTime(gic.getMaturityDt())).getDays();
		double maturityValue = openAmt * Math.pow((1.0 + effAnnualInterest), (durationDays + 0.0)/(365 + 0.0));		
		return maturityValue;
	}
	
	@Override
	public void updateRunningTotals(GIC gic) {
		List<GICTxn> gicTxns = gicTxnDao.findWithinTxnDtRangeByType(gic, gic.getOpenDt(), new Date(), ALL, "ASC");
		GICTxn oldTxn = null;
		double runningBalance = 0;
		boolean updateFlag = false;
		for (GICTxn aGICTxn : gicTxns) {
			updateFlag = false;
			if (oldTxn != null && oldTxn.getReversed().booleanValue() ) {
				if (oldTxn.getRunningAcctBalance() != null) {
					oldTxn.setRunningAcctBalance(null);
					updateFlag = true;
				}
			}
			else if (oldTxn != null && aGICTxn.getTxnDt().compareTo(oldTxn.getTxnDt()) != 0 && 
						(oldTxn.getRunningAcctBalance() == null || oldTxn.getRunningAcctBalance().doubleValue() != runningBalance)) 
			{
					oldTxn.setRunningAcctBalance(new Double(runningBalance));
					updateFlag = true;
			}
			else if (oldTxn != null && aGICTxn.getTxnDt().compareTo(oldTxn.getTxnDt()) == 0 && 
							 oldTxn.getRunningAcctBalance() != null) 
			{
				oldTxn.setRunningAcctBalance(null);
				updateFlag = true;
			}
			if (updateFlag) {
				gicTxnDao.store(oldTxn);
			}
			if (!aGICTxn.getReversed().booleanValue()) {
				runningBalance += (aGICTxn.getDrCr().equals("D") ? 1 : -1) * aGICTxn.getAmount().doubleValue();
			}
			oldTxn = aGICTxn;
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
			gicTxnDao.store(oldTxn);
		}
	}
	
	@Override
	public GICTxn createTxn(Double amount, String gicTxnType, GIC gic,
			Date txnDt, Integer offsetTxnId, Account offsetAccount) 
	{
		GICTxn gicTxn = new GICTxn();
		Date sysDt = new Date();
		gicTxn.setAmount(amount);
		gicTxn.setGicTxnType(gicTxnType);
		gicTxn.setGic(gic);
		gicTxn.setTxnAcctType("GC");
		gicTxn.setReversed(Boolean.FALSE);
		gicTxn.setTxnDt(txnDt);
		gicTxn.setDtCreated(sysDt);
		gicTxn.setDtChanged(sysDt);
		gicTxn.setUpdatedBy("SYSTEM");
		if (gicTxnType.equals(OPEN_ACCOUNT)) {
			gicTxn.setDrCr(DEBIT);
		}
		else {
			gicTxn.setDrCr(CREDIT);
		}
		gicTxn.setOffsetAccount(offsetAccount);
		gicTxn.setOffsetTxnId(offsetTxnId);
		gicTxnDao.store(gicTxn);
		return gicTxn;		
	}

}

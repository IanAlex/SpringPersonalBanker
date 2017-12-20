package com.ija.pb.services;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ija.pb.hibernate.tables.*;
import com.ija.pb.hibernate.dao.*;

@Service
public class ChequingServiceImpl implements ChequingService {
	
	@Autowired
	@Qualifier("chequingTxnDao")
	private ChequingTxnDao chequingTxnDao;
	
	private static final String OPEN_ACCOUNT = "O";
	private static final String DEPOSIT = "D";
	private static final String WITHDRAWAL = "W";
	private static final String TRANSFER_IN = "TI";
	private static final String TRANSFER_OUT = "TO";
	private static final String ISSUE_CHEQUE = "C";
	private static final String ALL = "ALL";
	
	private static final String DEBIT = "D";
	private static final String CREDIT = "C";
	

	@Override
	public double calcBalance(Set<ChequingTxn> chequingTxns) {
		if (chequingTxns != null & !chequingTxns.isEmpty()) {
			double total = 0;
			for (ChequingTxn chequingTxn: chequingTxns) {
				if (!chequingTxn.getReversed()) {
					total += (chequingTxn.getDrCr().equals("D") ? 1 : -1) * chequingTxn.getAmount().doubleValue();	
				}
			}
			return total;
		}
		else {
			return (0);
		}		
	}
	
	@Override
	public void updateRunningTotals(Chequing chequing) {
		List<ChequingTxn> chequingTxns = chequingTxnDao.findWithinTxnDtRangeByType(chequing, chequing.getOpenDt(), new Date(), ALL, "ASC");
		ChequingTxn oldTxn = null;
		double runningBalance = 0;
		boolean updateFlag = false;
		for (ChequingTxn aChequingTxn : chequingTxns) {
			updateFlag = false;
			if (oldTxn != null && oldTxn.getReversed().booleanValue() ) {
				if (oldTxn.getRunningAcctBalance() != null) {
					oldTxn.setRunningAcctBalance(null);
					updateFlag = true;
				}
			}
			else if (oldTxn != null && aChequingTxn.getTxnDt().compareTo(oldTxn.getTxnDt()) != 0 && 
						(oldTxn.getRunningAcctBalance() == null || oldTxn.getRunningAcctBalance().doubleValue() != runningBalance)) 
			{
					oldTxn.setRunningAcctBalance(new Double(runningBalance));
					updateFlag = true;
			}
			else if (oldTxn != null && aChequingTxn.getTxnDt().compareTo(oldTxn.getTxnDt()) == 0 && 
							 oldTxn.getRunningAcctBalance() != null) 
			{
				oldTxn.setRunningAcctBalance(null);
				updateFlag = true;
			}
			if (updateFlag) {
				chequingTxnDao.store(oldTxn);
			}
			if (!aChequingTxn.getReversed().booleanValue()) {
				runningBalance += (aChequingTxn.getDrCr().equals("D") ? 1 : -1) * aChequingTxn.getAmount().doubleValue();
			}
			oldTxn = aChequingTxn;
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
			chequingTxnDao.store(oldTxn);
		}
	}
	
	
	@Override
	public ChequingTxn createTxn(Double amount, String chequingTxnType, String chequeNo, Chequing chequing, 
						Date txnDt, Integer offsetTxnId, Account offsetAccount) 
	{
		ChequingTxn chequingTxn = new ChequingTxn();
		Date sysDt = new Date();
		chequingTxn.setAmount(amount);
		chequingTxn.setChequingTxnType(chequingTxnType);
		chequingTxn.setChequeNo(chequeNo);
		chequingTxn.setChequing(chequing);
		chequingTxn.setTxnAcctType("CH");
		chequingTxn.setTxnDt(txnDt);
		chequingTxn.setReversed(Boolean.FALSE);
		chequingTxn.setDtCreated(sysDt);
		chequingTxn.setDtChanged(sysDt);		
		chequingTxn.setUpdatedBy("SYSTEM");
		if (chequingTxnType.equals(DEPOSIT) || chequingTxnType.equals(TRANSFER_IN) || 
				chequingTxnType.equals(OPEN_ACCOUNT))
		{
			chequingTxn.setDrCr(DEBIT);
		}
		else {
			chequingTxn.setDrCr(CREDIT);
		}
		if (chequingTxnType.equals(TRANSFER_IN) || chequingTxnType.equals(TRANSFER_OUT) || 
			  chequingTxnType.equals(OPEN_ACCOUNT)) 
		{
			chequingTxn.setOffsetAccount(offsetAccount);
			chequingTxn.setOffsetTxnId(offsetTxnId);
		}
		chequingTxnDao.store(chequingTxn);
		return chequingTxn;		
	}

}

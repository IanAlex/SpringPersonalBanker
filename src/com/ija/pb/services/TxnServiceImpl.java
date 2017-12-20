package com.ija.pb.services;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ija.pb.hibernate.tables.*;
import com.ija.pb.hibernate.dao.*;

@Service
public class TxnServiceImpl implements TxnService {

	@Autowired
	@Qualifier("txnDao")
	private TxnDao txnDao;
	@Autowired
	@Qualifier("chequingDao")
	private ChequingDao chequingDao;
	@Autowired
	@Qualifier("savingsDao")
	private SavingsDao savingsDao;
	@Autowired
	@Qualifier("gicDao")
	private GICDao gicDao;
	@Autowired
	@Qualifier("annuityDao")
	private AnnuityDao annuityDao;
	@Autowired
	@Qualifier("stockDao")
	private StockDao stockDao;
	@Autowired 
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired 
	@Qualifier("savingsService")
	private SavingsService savingsService;
	
	
	@Override
	public void reverse(Account account, Txn txn) {
		txn.setReversed(Boolean.TRUE);
		txnDao.store(txn);
		if (account instanceof Savings) {
			boolean genIntFlag = false;
			Savings savings = (Savings) account;
			Set<SavingsTxn> savingsTxnSet = savings.getSavingsTxns();
			for (SavingsTxn tSavTxn : savingsTxnSet) {
				if (tSavTxn.getTxnId().intValue() == txn.getTxnId().intValue()) {
					genIntFlag = true;
					break;
				}
			}
			if (genIntFlag) {
				savingsService.generateInterestTxns(savings, txn.getTxnDt());
			}
		}
		if (txn.getOffsetTxnId() != null) {
			Txn offsetTxn = txnDao.findById(txn.getOffsetTxnId());
			if (offsetTxn != null) {
				offsetTxn.setReversed(Boolean.TRUE);
				txnDao.store(offsetTxn);
			}
			if (txn.getOffsetAccount() != null && txn.getOffsetAccount() instanceof Savings) {
				Savings savings = (Savings) account;
				savingsService.generateInterestTxns(savings, txn.getTxnDt());
			}
		}
	}
	
	@Override
	public String getTxnAccountInfo(Integer txnId) {
		String result = null;
		Txn txn = txnDao.findById(txnId);
		if (txn != null) {
			Account account = null;
			if (txn instanceof ChequingTxn) {
				account = ((ChequingTxn) txn).getChequing();
			}
			else if (txn instanceof SavingsTxn) {
				account = ((SavingsTxn) txn).getSavings();
			}
			else if (txn instanceof GICTxn) {
				account = ((GICTxn) txn).getGic();
			}
			else if (txn instanceof AnnuityTxn) {
				account = ((AnnuityTxn) txn).getAnnuity();
			}
			else if (txn instanceof StockTxn) {
				account = ((StockTxn) txn).getStock();
			}
			result = accountService.makeSpecificAccountDisplay(account, Boolean.TRUE);
		}
		return result;
	}

}

package com.ija.pb.services;

import java.util.*;

import com.ija.pb.hibernate.tables.*;

public interface SavingsService {
	
	double calcBalance(Set<SavingsTxn> savingsTxns);
	SavingsTxn createTxn(Double amount, String savingsTxnType, Savings savings, 
						Date txnDt, Integer offsetTxnId, Account offsetAccount);
	void updateRunningTotals(Savings savings);
	Savings generateInterestTxns(Savings inSavings, Date inDt);

}

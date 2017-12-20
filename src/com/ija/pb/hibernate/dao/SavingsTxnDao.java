package com.ija.pb.hibernate.dao;

import java.util.*;

import com.ija.pb.hibernate.tables.*;

public interface SavingsTxnDao {
	List<SavingsTxn> findAll();
	SavingsTxn findById(Integer txnId);
	List<SavingsTxn> findWithinTxnDtRangeByType(Savings savings, Date txnDtMin, Date txnDtMax,
			String savingsTxnType, String orderStr );
	List<SavingsTxn> findOrderedNonInterestTxns(Savings savings);
	List<SavingsTxn> findOrderedInterestTxns(Savings savings);
	SavingsTxn findByTxnTypeAndTxnDt (Savings savings, String savingsTxnType, Date txnDt);
	void delete(int txnId);
	void store(SavingsTxn savingsTxn);	
}

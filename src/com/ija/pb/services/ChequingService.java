package com.ija.pb.services;

import java.util.*;

import com.ija.pb.hibernate.tables.*;

public interface ChequingService {
	double calcBalance(Set<ChequingTxn> chequingTxns);
	void updateRunningTotals(Chequing chequing);
	ChequingTxn createTxn(Double amount, String chequingTxnType, String chequeNo, Chequing chequing, 
					Date txnDt, Integer offsetTxnId, Account offsetAccount);
}

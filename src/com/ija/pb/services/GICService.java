package com.ija.pb.services;

import java.util.*;

import com.ija.pb.hibernate.tables.*;

public interface GICService {
	double calcBalance(Set<GICTxn> gicTxns);
	double calcMaturityValue(GIC gic);
	double calcMaturityValue(GIC gic, double openAmt);
	void updateRunningTotals(GIC gic);
	GICTxn createTxn(Double amount, String gicTxnType,  GIC gic, Date txnDt, Integer offsetTxnId, Account offsetAccount);
}

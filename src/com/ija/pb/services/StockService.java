package com.ija.pb.services;

import java.util.*;

import com.ija.stockserv.*;

import com.ija.pb.hibernate.tables.*;

public interface StockService {
	
	int calcNoShares(Set<StockTxn> stockTxns);
	//double calcAverageCostBase(Set<StockTxn> stockTxns);
	double calcAverageCostBase(Stock stock, Date valDt);
	double calcCapitalGain(double price, int unitsSold, int unitsOnHand, double fee, double acb);
	public void updateRunningUnits(Stock stock);
	StockTxn createTxn(Double price, Integer units, Double fee, Double capitalGain, Double acbForTxn,
					String stockTxnType, Stock stock, Date txnDt, Integer offsetTxnId, Account offsetAccount);

}

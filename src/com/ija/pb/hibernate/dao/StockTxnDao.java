package com.ija.pb.hibernate.dao;

import java.util.*;

import com.ija.pb.hibernate.tables.*;

public interface StockTxnDao {
	List<StockTxn> findAll();
	StockTxn findById(Integer txnId);
	List<StockTxn> findWithinTxnDtRangeByType(Stock stock, Date txnDtMin, Date txnDtMax,
			 String stockTxnType, String orderStr);
	void store(StockTxn stockTxn);	
}

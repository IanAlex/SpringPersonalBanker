package com.ija.pb.hibernate.dao;

import java.util.*;

import com.ija.pb.hibernate.tables.*;

public interface ChequingTxnDao {
	List<ChequingTxn> findAll();
	List<ChequingTxn> findInDateRange(Chequing chequing, Date startDt, Date endDt);
	ChequingTxn findById(Integer txnId);
	List<ChequingTxn> findWithinTxnDtRangeByType(Chequing chequing, Date txnDtMin, Date txnDtMax,
			String chequingTxnType, String orderStr);
	void store(ChequingTxn chequingTxn);	
}

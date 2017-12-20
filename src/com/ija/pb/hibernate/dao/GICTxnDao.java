package com.ija.pb.hibernate.dao;

import java.util.*;

import com.ija.pb.hibernate.tables.*;

public interface GICTxnDao {
	List<GICTxn> findAll();
	GICTxn findById(Integer txnId);
	List<GICTxn> findWithinTxnDtRangeByType(GIC gic, Date txnDtMin, Date txnDtMax,
			String gicTxnType, String orderStr);
	void store(GICTxn gicTxn);	
}

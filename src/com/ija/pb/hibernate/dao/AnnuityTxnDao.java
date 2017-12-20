package com.ija.pb.hibernate.dao;

import java.util.*;

import com.ija.pb.hibernate.tables.*;

public interface AnnuityTxnDao {
	List<AnnuityTxn> findAll();
	AnnuityTxn findById(Integer txnId);
	List<AnnuityTxn> findWithinTxnDtRangeByType(Annuity annuity, Date txnDtMin, Date txnDtMax,
			String annuityTxnType, String orderStr);
	void store(AnnuityTxn annuityTxn);	
}

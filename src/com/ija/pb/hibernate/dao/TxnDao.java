package com.ija.pb.hibernate.dao;

import java.util.*;
import com.ija.pb.hibernate.tables.*;

public interface TxnDao {
	List<Txn> findAll();
	Txn findById(Integer txnId);
	void store(Txn txn);
}

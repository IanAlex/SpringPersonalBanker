package com.ija.pb.services;

import java.util.*;

import com.ija.pb.hibernate.tables.*;

public interface TxnService {
	void reverse(Account account, Txn txn);
	String getTxnAccountInfo(Integer txnId);
}

package com.ija.pb.hibernate.dao;

import java.util.*;
import com.ija.pb.hibernate.tables.*;

public interface AccountDao {
	List<Account> findAll();
	List<Account> findForClient(Client client);
	Set<Account> findForClientByAccountType(Client client, String acctType, Boolean openOnly);
	Account findById(Integer accountId);
	void store(Account account);	
}
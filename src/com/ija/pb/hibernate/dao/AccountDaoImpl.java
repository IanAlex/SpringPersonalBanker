package com.ija.pb.hibernate.dao;

import java.util.*;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ija.pb.hibernate.tables.*;

@Repository
public class AccountDaoImpl implements AccountDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;

	@Override
	public List<Account> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from Account");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public List<Account> findForClient(Client client) {
		if (client == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			String qryStr = "from Account A where" +
		               " A.client = :pClient";
			Query query = session.createQuery(qryStr)
					.setParameter("pClient", client);
			return  query.list();
		} finally {
			session.close();
		}			
	}

	@Override
	public Set<Account> findForClientByAccountType(Client client,
			String acctType, Boolean openOnly) {
		if (client != null && acctType != null && !acctType.trim().equals("") &&
				client.getAccounts() != null && !client.getAccounts().isEmpty()) {
			Set<Account> accounts = client.getAccounts();
			Set<Account> result = new HashSet<Account>();
			for(Account account : accounts) {
				if (account.getAccountType().equals(acctType) && (account.getOpen() || !openOnly)) {
					result.add(account);
				}
			}
			return result;
			/*
			Session session = sessionFactory.openSession();		
			try {
				String qryStr = "from Account A where" +
			               " A.ClientId = pClientId" +
						   " and A.AcctType = pAcctType";
				Query query = session.createQuery(qryStr)
						.setParameter("pClientId", client.getClientId())
						.setParameter("pAcctType", acctType);
				return  query.list();
			} finally {
				session.close();
			}
			*/		
		}
		else {
			return new HashSet<Account>();
		}
	}
	
	@Override
	public Account findById(Integer accountId) {
		if (accountId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (Account) session.get(Account.class, accountId);
		} finally {
			session.close();
		}
	}

	@Override
	public void store(Account account) {
		if (account != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(account);
				tx.commit();
			} catch (RuntimeException e) {
				tx.rollback();
				throw e;
			}
			finally {
				session.close();
			}
		}
	}

}

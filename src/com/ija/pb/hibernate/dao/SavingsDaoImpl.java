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
public class SavingsDaoImpl implements SavingsDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;
	
	@Autowired
    @Qualifier("accountDao")
	private AccountDao accountDao;

	@Override
	public List<Savings> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from Savings");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public Set<Savings> findForClient(Client client, Boolean openOnly) {
		Set<Account> accounts = accountDao.findForClientByAccountType(client, "SV", openOnly);
		if (accounts!= null && !accounts.isEmpty()) {
			Session session = sessionFactory.openSession();	
			Set<Savings> savingsSet = new HashSet<Savings>();
			try {
				for(Account account : accounts) {
					Savings savings = (Savings) session.get(Savings.class, account.getAccountId());
					savingsSet.add(savings);
				}
				return savingsSet;
			} finally {
				session.close();
			}
		}
		else {
			return new HashSet<Savings>();
		}
	}

	@Override
	public Savings findById(Integer accountId) {
		if (accountId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (Savings) session.get(Savings.class, accountId);
		} finally {
			session.close();
		}
	}

	@Override
	public void store(Savings savings) {
		if (savings != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(savings);
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

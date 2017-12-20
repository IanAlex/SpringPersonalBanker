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
public class StockDaoImpl implements StockDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;
	
	@Autowired
    @Qualifier("accountDao")
	private AccountDao accountDao;

	@Override
	public List<Stock> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from Stock");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public Set<Stock> findForClient(Client client, Boolean openOnly) {
		Set<Account> accounts = accountDao.findForClientByAccountType(client, "ST", openOnly);
		if (accounts!= null && !accounts.isEmpty()) {
			Session session = sessionFactory.openSession();	
			Set<Stock> stocks = new HashSet<Stock>();
			try {
				for(Account account : accounts) {
					Stock stock = (Stock) session.get(Stock.class, account.getAccountId());
					stocks.add(stock);
				}
				return stocks;
			} finally {
				session.close();
			}
		}
		else {
			return new HashSet<Stock>();
		}
	}

	@Override
	public Stock findById(Integer accountId) {
		if (accountId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (Stock) session.get(Stock.class, accountId);
		} finally {
			session.close();
		}
	}

	@Override
	public void store(Stock stock) {
		if (stock != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(stock);
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

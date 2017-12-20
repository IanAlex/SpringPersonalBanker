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
public class ChequingDaoImpl implements ChequingDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;
	
	@Autowired
    @Qualifier("accountDao")
	private AccountDao accountDao;

	@Override
	public List<Chequing> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from Chequing");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public Set<Chequing> findForClient(Client client, Boolean openOnly) {
		Set<Account> accounts = accountDao.findForClientByAccountType(client, "CH", openOnly);
		if (accounts!= null && !accounts.isEmpty()) {
			Session session = sessionFactory.openSession();	
			Set<Chequing> chequingSet = new HashSet<Chequing>();
			try {
				for(Account account : accounts) {
					Chequing chequing = (Chequing) session.get(Chequing.class, account.getAccountId());
					chequingSet.add(chequing);
				}
				return chequingSet;
			} finally {
				session.close();
			}
		}
		else {
			return new HashSet<Chequing>();
		}
	}

	@Override
	public Chequing findById(Integer accountId) {
		if (accountId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (Chequing) session.get(Chequing.class, accountId);
		} finally {
			session.close();
		}
	}

	@Override
	public void store(Chequing chequing) {
		if (chequing != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(chequing);
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

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
public class AnnuityDaoImpl implements AnnuityDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;
	
	@Autowired
    @Qualifier("accountDao")
	private AccountDao accountDao;

	@Override
	public List<Annuity> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from Annuity");
			return query.list();
		} finally {
			session.close();
		}
	}
	
	@Override
	public Set<Annuity> findForClient(Client client, Boolean openOnly) {
		Set<Account> accounts = accountDao.findForClientByAccountType(client, "AN", openOnly);
		if (accounts!= null && !accounts.isEmpty()) {
			Session session = sessionFactory.openSession();	
			Set<Annuity> annuities = new HashSet<Annuity>();
			try {
				for(Account account : accounts) {
					Annuity annuity = (Annuity) session.get(Annuity.class, account.getAccountId());
					annuities.add(annuity);
				}
				return annuities;
			} finally {
				session.close();
			}
		}
		else {
			return new HashSet<Annuity>();
		}
	}

	@Override
	public Annuity findById(Integer accountId) {
		if (accountId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (Annuity) session.get(Annuity.class, accountId);
		} finally {
			session.close();
		}
	}

	@Override
	public void store(Annuity annuity) {
		if (annuity != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(annuity);
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

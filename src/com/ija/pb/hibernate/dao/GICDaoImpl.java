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
public class GICDaoImpl implements GICDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;
	
	@Autowired
    @Qualifier("accountDao")
	private AccountDao accountDao;

	@Override
	public List<GIC> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from GIC");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public Set<GIC> findForClient(Client client, Boolean openOnly) {
		Set<Account> accounts = accountDao.findForClientByAccountType(client, "GC", openOnly);
		if (accounts!= null && !accounts.isEmpty()) {
			Session session = sessionFactory.openSession();	
			Set<GIC> gics = new HashSet<GIC>();
			try {
				for(Account account : accounts) {
					GIC gic = (GIC) session.get(GIC.class, account.getAccountId());
					gics.add(gic);
				}
				return gics;
			} finally {
				session.close();
			}
		}
		else {
			return new HashSet<GIC>();
		}
	}

	@Override
	public GIC findById(Integer accountId) {
		if (accountId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (GIC) session.get(GIC.class, accountId);
		} finally {
			session.close();
		}
	}

	@Override
	public void store(GIC gic) {
		if (gic != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(gic);
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

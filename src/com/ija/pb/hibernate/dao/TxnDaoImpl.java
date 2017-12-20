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
public class TxnDaoImpl implements TxnDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;

	@Override
	public List<Txn> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from Txn");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public Txn findById(Integer txnId) {
		if (txnId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (Txn) session.get(Txn.class, txnId);
		} finally {
			session.close();
		}
	}

	@Override
	public void store(Txn txn) {
		if (txn != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(txn);
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

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
public class BusinessDaoImpl implements BusinessDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;

	@Override
	public List<Business> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from Business");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public Business findById(Integer clientId) {
		if (clientId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (Business) session.get(Business.class, clientId);
		} finally {
			session.close();
		}
	}

	@Override
	public void store(Business business) {
		if (business != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(business);
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

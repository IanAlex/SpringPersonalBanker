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
public class AddressDaoImpl implements AddressDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;

	@Override
	public List<Address> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from Address");
			return query.list();
		} finally {
			session.close();
		}
	}

	
	@Override
	public Address findById(Integer addressId) {
		if (addressId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (Address) session.get(Address.class, addressId);
		} finally {
			session.close();
		}
	}

	@Override
	public void store(Address address) {
		if (address != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(address);
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

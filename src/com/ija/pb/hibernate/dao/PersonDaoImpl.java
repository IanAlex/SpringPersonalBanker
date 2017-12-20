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
public class PersonDaoImpl implements PersonDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;

	@Override
	public List<Person> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from Person");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public Person findById(Integer clientId) {
		if (clientId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (Person) session.get(Person.class, clientId);
		} finally {
			session.close();
		}
	}

	@Override
	public void store(Person person) {
		if (person != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(person);
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

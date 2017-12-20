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
public class ClientDaoImpl implements ClientDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;
	
	@Autowired
    @Qualifier("loginDao")
	private LoginDao loginDao;

	@Override
	public List<Client> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from Client");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public Client findById(Integer clientId) {
		if (clientId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (Client) session.get(Client.class, clientId);
		} finally {
			session.close();
		}
	}

	@Override
	public Client findByLoginKey(String loginKey) {
		Login login = loginDao.findForLoginKey(loginKey);
		if (login != null) {
			Session session = sessionFactory.openSession();	
			try {
				String qryStr = "from Client A where" +
			               " A.login = :pLogin";
				Query query = session.createQuery(qryStr)
						.setParameter("pLogin", login);
				return (Client) query.uniqueResult();
			} finally {
				session.close();
			}			
		}
		else {
			return null;
		}
	}

	@Override
	public void store(Client client) {
		if (client != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(client);
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

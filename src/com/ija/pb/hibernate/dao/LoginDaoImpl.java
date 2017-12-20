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
public class LoginDaoImpl implements LoginDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;

	@Override
	public List<Login> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from Login");
			return query.list();
		} finally {
			session.close();
		}
	}
	
	@Override
	public Login findForUserInfo(String userId, String pswd) {
		if (userId != null && (!userId.trim().equals("")) &&
				pswd != null && (!pswd.trim().equals("")) ) {
			
			Session session = sessionFactory.openSession();	
			try {
				String qryStr = "from Login A where" +
			               " A.name = :pName" +
						   " and A.password = :pPswd ";
				Query query = session.createQuery(qryStr)
						.setParameter("pName", userId)
						.setParameter("pPswd", pswd);
				return (Login) query.uniqueResult();
			} finally {
				session.close();
			}			
		}
		else {
			return null;
		}
	}

	@Override
	public Login findForLoginKey(String loginKey) {
		if (loginKey != null && !loginKey.trim().equals("")) {
			
			Session session = sessionFactory.openSession();	
			try {
				String qryStr = "from Login A where" +
			               " A.loginKey = :pLoginKey";
				Query query = session.createQuery(qryStr)
						.setParameter("pLoginKey", loginKey);
				return (Login) query.uniqueResult();
			} finally {
				session.close();
			}			
		}
		else {
			return null;
		}
	}

	@Override
	public void store(Login login) {
		if (login != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(login);
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

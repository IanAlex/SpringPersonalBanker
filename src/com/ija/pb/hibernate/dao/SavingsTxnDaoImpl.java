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
public class SavingsTxnDaoImpl implements SavingsTxnDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;
	
	private static final String INTEREST = "I";

	@Override
	public List<SavingsTxn> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from SavingsTxn");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public SavingsTxn findById(Integer txnId) {
		if (txnId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (SavingsTxn) session.get(SavingsTxn.class, txnId);
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<SavingsTxn> findWithinTxnDtRangeByType(Savings savings, Date txnDtMin, Date txnDtMax,
														String savingsTxnType, String orderStr ) 
	{
		if (savings != null) {
			Session session = sessionFactory.openSession();	
			try {
				Query query = null;
				if (savingsTxnType != null && !savingsTxnType.equals("ALL")) {
					String qryStr = "from SavingsTxn A where" +
			               			" A.savings = :pSavings and" +
			               			" A.txnDt >= :ptxnDtMin and" +
			               			" A.txnDt <= :ptxnDtMax and" +
			               			" A.savingsTxnType = :psavingsTxnType";
					if (orderStr != null && (orderStr.trim().equalsIgnoreCase("desc") || orderStr.trim().equalsIgnoreCase("asc"))) 
					{
						qryStr += " order by A.txnDt " + orderStr + ", A.reversed DESC ";
					}
					query = session.createQuery(qryStr)
							.setParameter("pSavings", savings)
							.setParameter("ptxnDtMin", txnDtMin)
							.setParameter("ptxnDtMax", txnDtMax)
							.setParameter("psavingsTxnType", savingsTxnType);
				}
				else {
					String qryStr = "from SavingsTxn A where" +
	               			" A.savings = :pSavings and" +
	               			" A.txnDt >= :ptxnDtMin and" +
	               			" A.txnDt <= :ptxnDtMax";
					if (orderStr != null && (orderStr.trim().equalsIgnoreCase("desc") || orderStr.trim().equalsIgnoreCase("asc"))) 
					{
						qryStr += " order by A.txnDt " + orderStr + ", A.reversed DESC ";
					}
					query = session.createQuery(qryStr)
							.setParameter("pSavings", savings)
							.setParameter("ptxnDtMin", txnDtMin)
							.setParameter("ptxnDtMax", txnDtMax);
				}
				return query.list();
			} finally {
				session.close();
			}	
		}
		else {
			return null;
		}
	}	
	
	@Override
	public List<SavingsTxn> findOrderedNonInterestTxns(Savings savings) {
		if (savings != null) {
			Session session = sessionFactory.openSession();	
			try {
				String qryStr = "from SavingsTxn A where" +
								" A.savings = :pSavings and" +
								" A.savingsTxnType != :pSavingsTxnType" +
			               		" order by A.txnDt";
				Query query = session.createQuery(qryStr)
						.setParameter("pSavings", savings)
						.setParameter("pSavingsTxnType", INTEREST);
				return query.list();
			} finally {
				session.close();
			}	
		}
		else {
			return null;
		}
	}
	
	@Override
	public List<SavingsTxn> findOrderedInterestTxns(Savings savings) {
		if (savings != null) {
			Session session = sessionFactory.openSession();	
			try {
				String qryStr = "from SavingsTxn A where" +
								" A.savings = :pSavings and" +
								" A.savingsTxnType = :pSavingsTxnType" +
			               		" order by A.txnDt";
				Query query = session.createQuery(qryStr)
						.setParameter("pSavings", savings)
						.setParameter("pSavingsTxnType", INTEREST);
				return query.list();
			} finally {
				session.close();
			}	
		}
		else {
			return null;
		}
	}
	
	@Override
	public SavingsTxn findByTxnTypeAndTxnDt (Savings savings, String savingsTxnType, Date txnDt) {
		if (savings != null) {
			Session session = sessionFactory.openSession();	
			try {
				String qryStr = "from SavingsTxn A where" +
								" A.savings = :pSavings and" +
								" A.savingsTxnType = :pSavingsTxnType and" +
			               		" A.txnDt = :pTxnDt";
				Query query = session.createQuery(qryStr)
						.setParameter("pSavings", savings)
						.setParameter("pSavingsTxnType", savingsTxnType)
						.setParameter("pTxnDt", txnDt);
				return (SavingsTxn) query.uniqueResult();
			} finally {
				session.close();
			}	
		}
		else {
			return null;
		}
	}
	
	@Override
	public void delete(int txnId) {
		Session session = sessionFactory.openSession();	
		Transaction tx = session.beginTransaction();
		try {
			SavingsTxn savTxn = (SavingsTxn) session.get(SavingsTxn.class, txnId);
			session.delete(savTxn);
			tx.commit();
		}catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}
		finally {
			session.close();
		}
	}

	@Override
	public void store(SavingsTxn savingsTxn) {
		if (savingsTxn != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(savingsTxn);
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

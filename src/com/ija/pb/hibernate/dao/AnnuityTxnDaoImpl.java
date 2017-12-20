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
public class AnnuityTxnDaoImpl implements AnnuityTxnDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;

	@Override
	public List<AnnuityTxn> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from AnnuityTxn");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public AnnuityTxn findById(Integer txnId) {
		if (txnId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (AnnuityTxn) session.get(AnnuityTxn.class, txnId);
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<AnnuityTxn> findWithinTxnDtRangeByType(Annuity annuity, Date txnDtMin, Date txnDtMax,
														String annuityTxnType, String orderStr)
	{
		if (annuity != null) {
			Session session = sessionFactory.openSession();	
			try {
				Query query = null;
				if (annuityTxnType != null && !annuityTxnType.equals("ALL")) {
					String qryStr = "from AnnuityTxn A where" +
			               			" A.annuity = :pAnnuity and" +
			               			" A.txnDt >= :ptxnDtMin and" +
			               			" A.txnDt <= :ptxnDtMax and" +
			               			" A.annuityTxnType = :pannuityTxnType";
					if (orderStr != null && (orderStr.trim().equalsIgnoreCase("desc") || orderStr.trim().equalsIgnoreCase("asc"))) 
					{
						qryStr += " order by A.txnDt " + orderStr;
					}
					query = session.createQuery(qryStr)
							.setParameter("pAnnuity", annuity)
							.setParameter("ptxnDtMin", txnDtMin)
							.setParameter("ptxnDtMax", txnDtMax)
							.setParameter("pannuityTxnType", annuityTxnType);
				}
				else {
					String qryStr = "from AnnuityTxn A where" +
	               			" A.annuity = :pAnnuity and" +
	               			" A.txnDt >= :ptxnDtMin and" +
	               			" A.txnDt <= :ptxnDtMax";
					if (orderStr != null && (orderStr.trim().equalsIgnoreCase("desc") || orderStr.trim().equalsIgnoreCase("asc"))) 
					{
						qryStr += " order by A.txnDt " + orderStr;
					}
					query = session.createQuery(qryStr)
							.setParameter("pAnnuity", annuity)
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
	public void store(AnnuityTxn annuityTxn) {
		if (annuityTxn != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(annuityTxn);
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

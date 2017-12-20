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
public class ChequingTxnDaoImpl implements ChequingTxnDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;

	@Override
	public List<ChequingTxn> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from ChequingTxn");
			return query.list();
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<ChequingTxn> findInDateRange(Chequing chequing, Date startDt, Date endDt) {
		Session session = sessionFactory.openSession();	
		try {
			String qryStr = "from ChequingTxn A where" +
		               " A.chequing = :pChequing" +
					   " and A.txnDt >= :pstartDt " +
					   " and A.txnDt <= :pendDt ";
			Query query = session.createQuery(qryStr)
					.setParameter("pChequing", chequing)
					.setParameter("pstartDt", startDt)
					.setParameter("pendDt", endDt);
			return query.list();
		} finally {
			session.close();
		}			
	}

	@Override
	public ChequingTxn findById(Integer txnId) {
		if (txnId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (ChequingTxn) session.get(ChequingTxn.class, txnId);
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<ChequingTxn> findWithinTxnDtRangeByType(Chequing chequing, Date txnDtMin, Date txnDtMax,
												String chequingTxnType, String orderStr)
	{
		if (chequing != null) {
			Session session = sessionFactory.openSession();	
			try {				
				Query query = null;
				if (chequingTxnType != null && !chequingTxnType.equals("ALL")) {
					String qryStr = "from ChequingTxn A where" +
			               			" A.chequing = :pChequing and" +
			               			" A.txnDt >= :ptxnDtMin and" +
			               			" A.txnDt <= :ptxnDtMax and" +
			               			" A.chequingTxnType = :pchequingTxnType";
					if (orderStr != null && (orderStr.trim().equalsIgnoreCase("desc") || orderStr.trim().equalsIgnoreCase("asc"))) 
					{
						qryStr += " order by A.txnDt " + orderStr + ", A.reversed DESC ";
					}
					query = session.createQuery(qryStr)
							.setParameter("pChequing", chequing)
							.setParameter("ptxnDtMin", txnDtMin)
							.setParameter("ptxnDtMax", txnDtMax)
							.setParameter("pchequingTxnType", chequingTxnType);
				}
				else {
					String qryStr = "from ChequingTxn A where" +
	               			" A.chequing = :pChequing and" +
	               			" A.txnDt >= :ptxnDtMin and" +
	               			" A.txnDt <= :ptxnDtMax";
					if (orderStr != null && (orderStr.trim().equalsIgnoreCase("desc") || orderStr.trim().equalsIgnoreCase("asc"))) 
					{
						qryStr += " order by A.txnDt " + orderStr + ", A.reversed DESC ";
					}
					query = session.createQuery(qryStr)
							.setParameter("pChequing", chequing)
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
	public void store(ChequingTxn chequingTxn) {
		if (chequingTxn != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(chequingTxn);
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

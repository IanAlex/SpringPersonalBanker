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
public class GICTxnDaoImpl implements GICTxnDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;

	@Override
	public List<GICTxn> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from GICTxn");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public GICTxn findById(Integer txnId) {
		if (txnId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (GICTxn) session.get(GICTxn.class, txnId);
		} finally {
			session.close();
		}
	}
	
	@Override
	public List<GICTxn> findWithinTxnDtRangeByType(GIC gic, Date txnDtMin, Date txnDtMax,
													String gicTxnType, String orderStr) 
	{
		if (gic != null) {
			Session session = sessionFactory.openSession();	
			try {
				Query query = null;
				if (gicTxnType != null && !gicTxnType.equals("ALL")) {
					String qryStr = "from GICTxn A where" +
			               			" A.gic = :pGIC and" +
			               			" A.txnDt >= :ptxnDtMin and" +
			               			" A.txnDt <= :ptxnDtMax and" +
			               			" A.gicTxnType = :pgicTxnType";
					if (orderStr != null && (orderStr.trim().equalsIgnoreCase("desc") || orderStr.trim().equalsIgnoreCase("asc"))) 
					{
						qryStr += " order by A.txnDt " + orderStr + ", A.reversed DESC ";
					}
					query = session.createQuery(qryStr)
							.setParameter("pGIC", gic)
							.setParameter("ptxnDtMin", txnDtMin)
							.setParameter("ptxnDtMax", txnDtMax)
							.setParameter("pgicTxnType", gicTxnType);
				}
				else {
					String qryStr = "from GICTxn A where" +
	               			" A.gic = :pGIC and" +
	               			" A.txnDt >= :ptxnDtMin and" +
	               			" A.txnDt <= :ptxnDtMax";
					if (orderStr != null && (orderStr.trim().equalsIgnoreCase("desc") || orderStr.trim().equalsIgnoreCase("asc"))) 
					{
						qryStr += " order by A.txnDt " + orderStr + ", A.reversed DESC ";
					}
					query = session.createQuery(qryStr)
							.setParameter("pGIC", gic)
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
	public void store(GICTxn gicTxn) {
		if (gicTxn != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(gicTxn);
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

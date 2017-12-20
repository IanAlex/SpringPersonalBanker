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
public class StockTxnDaoImpl implements StockTxnDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;

	@Override
	public List<StockTxn> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from StockTxn");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public StockTxn findById(Integer txnId) {
		if (txnId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (StockTxn) session.get(StockTxn.class, txnId);
		} finally {
			session.close();
		}
	}	
	
	@Override
	public List<StockTxn> findWithinTxnDtRangeByType(Stock stock, Date txnDtMin, Date txnDtMax,
													 String stockTxnType, String orderStr) {
		if (stock != null) {
			Session session = sessionFactory.openSession();	
			try {
				Query query = null;
				if (stockTxnType != null && !stockTxnType.equals("ALL")) {
					String qryStr = "from StockTxn A where" +
			               			" A.stock = :pStock and" +
			               			" A.txnDt >= :ptxnDtMin and" +
			               			" A.txnDt <= :ptxnDtMax and" +
				               		" A.stockTxnType = :pstockTxnType";
					if (orderStr != null && (orderStr.trim().equalsIgnoreCase("desc") || orderStr.trim().equalsIgnoreCase("asc"))) 
					{
						qryStr += " order by A.txnDt " + orderStr + ", A.reversed DESC ";
					}
					query = session.createQuery(qryStr)
							.setParameter("pStock", stock)
							.setParameter("ptxnDtMin", txnDtMin)
							.setParameter("ptxnDtMax", txnDtMax)
							.setParameter("pstockTxnType", stockTxnType);
				}
				else {
					String qryStr = "from StockTxn A where" +
	               			" A.stock = :pStock and" +
	               			" A.txnDt >= :ptxnDtMin and" +
	               			" A.txnDt <= :ptxnDtMax";
					if (orderStr != null && (orderStr.trim().equalsIgnoreCase("desc") || orderStr.trim().equalsIgnoreCase("asc"))) 
					{
						qryStr += " order by A.txnDt " + orderStr + ", A.reversed DESC ";
					}
					query = session.createQuery(qryStr)
							.setParameter("pStock", stock)
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
	public void store(StockTxn stockTxn) {
		if (stockTxn != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(stockTxn);
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

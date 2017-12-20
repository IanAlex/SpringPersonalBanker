package com.ija.pb.hibernate.dao;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ija.pb.hibernate.tables.*;

@Repository
public class SavingsRateDaoImpl implements SavingsRateDao {
	
	@Autowired
    @Qualifier("sessionFactoryBean")
	private SessionFactory sessionFactory;

	@Override
	public List<SavingsRate> findAll() {
		Session session = sessionFactory.openSession();	
		try {
			Query query = session.createQuery("from SavingsRate");
			return query.list();
		} finally {
			session.close();
		}
	}

	@Override
	public SavingsRate findById(Integer savingsRateId) {
		if (savingsRateId == null) {
			return null;
		}
		Session session = sessionFactory.openSession();		
		try {
			return (SavingsRate) session.get(SavingsRate.class, savingsRateId);
		} finally {
			session.close();
		}
	}
	
	@Override
	public SavingsRate findByEffectiveDt(Savings savings, Date effectiveDt) {
		if (effectiveDt != null) {
			Session session = sessionFactory.openSession();	
			try {
				String qryStr = "from SavingsRate A where" +
			               		" A.effectiveDt = :pEffectiveDt and" +
			               		" A.savings = :pSavings";
				Query query = session.createQuery(qryStr)
						.setParameter("pEffectiveDt", effectiveDt)
						.setParameter("pSavings", savings);
				return (SavingsRate) query.uniqueResult();
			} finally {
				session.close();
			}	
		}
		else {
			return null;
		}
	}
	
	@Override
	public List<SavingsRate> findOrderByEffectiveDt(Savings savings) {
		if (savings != null) {
			Session session = sessionFactory.openSession();	
			try {
				String qryStr = "from SavingsRate A where" +
			               		" A.savings = :pSavings" +
			               		" order by A.effectiveDt";
				Query query = session.createQuery(qryStr)
						.setParameter("pSavings", savings);
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
	public Map<Date, Double> findInEffectiveDtRange(Savings savings, Date dtMin, Date dtMax) {
		Map<Date, Double> retMap = new TreeMap<Date, Double>();
		if (savings != null) {
			Session session = sessionFactory.openSession();	
			try {
				String qryStr = "from SavingsRate A where" +
			               		" A.savings = :pSavings and" +
			               		" A.effectiveDt <= :pEffectiveDt and" +
			               		" order by A.effectiveDt desc ";
				Query query = session.createQuery(qryStr)
						.setParameter("pSavings", savings)
						.setParameter("pEffectiveDt", dtMax);						
				List<SavingsRate> savingsRates = query.list();
				boolean stopFlag = false;
				for(SavingsRate savRate: savingsRates) {
					if (savRate.getEffectiveDt().after(dtMin) || !stopFlag) {
						if (savRate.getEffectiveDt().after(dtMin)) {
							retMap.put(savRate.getEffectiveDt(), savRate.getInterestRate());
						}
						else {
							retMap.put(dtMin, savRate.getInterestRate());
							stopFlag = true;
						}
					}
					else {
						break;
					}
				}
				return retMap;
			} finally {
				session.close();
			}	
		}
		return null;
	}

	@Override
	public void store(SavingsRate savingsRate) {
		if (savingsRate != null) {
			Session session = sessionFactory.openSession();	
			Transaction tx = session.beginTransaction();
			try {
				session.saveOrUpdate(savingsRate);
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

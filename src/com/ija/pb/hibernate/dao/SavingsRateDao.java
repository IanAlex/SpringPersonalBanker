package com.ija.pb.hibernate.dao;

import java.util.*;

import com.ija.pb.hibernate.tables.*;

public interface SavingsRateDao {
	List<SavingsRate> findAll();
	SavingsRate findById(Integer savingsRateId);
	SavingsRate findByEffectiveDt(Savings savings, Date effectiveDt);
	List<SavingsRate> findOrderByEffectiveDt(Savings savings);
	Map<Date, Double> findInEffectiveDtRange(Savings savings, Date dtMin, Date dtMax);
	void store(SavingsRate savingsRate);	
}

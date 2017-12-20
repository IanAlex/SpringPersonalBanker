package com.ija.pb.aop;

import java.util.*;

import org.joda.time.MutableDateTime;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.ija.pb.hibernate.tables.*;
import com.ija.pb.hibernate.dao.*;
import com.ija.pb.nondaodata.CurrentScreenInfo;
import com.ija.pb.services.*;

@Configurable
@Aspect
public class SavingsGenInterestAspect {
	
	@Autowired
	CurrentScreenInfo currentScreenInfo;
	@Autowired 
	@Qualifier("savingsDao")
	private SavingsDao savingsDao;
	@Autowired 
	@Qualifier("savingsTxnDao")
	private SavingsTxnDao savingsTxnDao;
	@Autowired 
	@Qualifier("savingsService")
	private SavingsService savingsService;
	
	private static final String INTEREST = "I";
	
	@After("execution(* com.ija.jsf.mvc.LoginMVC.signin(..))")
	public void updateSavingsInterestTxns(JoinPoint joinpoint) {
		if (currentScreenInfo != null && currentScreenInfo.getClient() != null) {
			System.out.println("hi ian AAA");
			Date sysDt = new Date();
			Set<Savings> savingsSet = savingsDao.findForClient(currentScreenInfo.getClient(),Boolean.TRUE);
			if (savingsSet != null && !savingsSet.isEmpty()) {
				for (Savings savings : savingsSet) {
					List<SavingsTxn> savingsInterestTxnList = savingsTxnDao.findWithinTxnDtRangeByType(savings, savings.getOpenDt(), sysDt, INTEREST, "DESC");
					if (savingsInterestTxnList != null && !savingsInterestTxnList.isEmpty()) {
						Date lastInterestDt = savingsInterestTxnList.get(0).getTxnDt();
						// start accumulating interest from beginning of first month without an interest payment
						if (sysDt.after(lastInterestDt)) {
							MutableDateTime startDtTime = new MutableDateTime(lastInterestDt);	
							startDtTime.addDays(1);	
							Calendar cal = new GregorianCalendar();
							cal.setTimeInMillis(startDtTime.getMillis());
							Date startDt = cal.getTime();
							savingsService.generateInterestTxns(savings, startDt);
						} 
					}
					
				}
			}
		}		
	}

}

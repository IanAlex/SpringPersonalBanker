package com.ija.pb.aop;

import java.util.*;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.ija.pb.nondaodata.*;
import com.ija.pb.hibernate.dao.*;
import com.ija.pb.hibernate.tables.*;
import com.ija.pb.services.*;

@Configurable
@Aspect
public class AnnuityPayTxnAspect {	
	
	@Autowired
	CurrentScreenInfo currentScreenInfo;
	@Autowired 
	@Qualifier("annuityDao")
	private AnnuityDao annuityDao;
	@Autowired 
	@Qualifier("annuityTxnDao")
	private AnnuityTxnDao annuityTxnDao;
	@Autowired 
	@Qualifier("annuityService")
	private AnnuityService annuityService;
	@Autowired 
	@Qualifier("chequingService")
	private ChequingService chequingService;
	@Autowired 
	@Qualifier("savingsService")
	private SavingsService savingsService;
	
	private boolean annuityPayTxnsUpdated = false;
	
	private static final String PAYMENT = "P";
	private static final String TRANSFER_IN = "TI";
	
	
	@After("execution(* com.ija.jsf.mvc.LoginMVC.signin(..))")
	public void updateAnnuityPayTxn(JoinPoint joinpoint) {
		System.out.println("hi ian 89.01  YYYYY");
		if (currentScreenInfo != null) {
			System.out.println("hi ian 55.07");
		}
		else {
			System.out.println("hi ian 67.08");
		}
		if (currentScreenInfo != null && currentScreenInfo.getClient() != null) {
			Date sysDt = new Date();
			Set<Annuity> annuitySet = annuityDao.findForClient(currentScreenInfo.getClient(),Boolean.TRUE);
			if (annuitySet != null && !annuitySet.isEmpty()) {
				for (Annuity annuity : annuitySet) {
					List<AnnuityPayout> annuityPayouts = annuityService.calcAnnuityPayout(annuity, sysDt);
					List<AnnuityTxn> annuityTxns = annuityTxnDao.findWithinTxnDtRangeByType(annuity, annuity.getOpenDt(), sysDt,
																						"ALL", null);
					int txnNewCnt = 0;
					for (AnnuityPayout annuityPayout : annuityPayouts) {
						if (annuityPayout.getPaid().booleanValue()) {
							boolean txnExist = false;
							for (AnnuityTxn aTxn : annuityTxns) {
								if (aTxn.getAnnuityTxnType().equals(PAYMENT) && aTxn.getAmount().equals(annuity.getPeriodicPayment())  && 
										(aTxn.getTxnDt().compareTo(annuityPayout.getPayDate()) == 0) )
								{
									txnExist = true;
								}
							}
							if (!txnExist) {  //create annuity payment txn
								AnnuityTxn annuityTxnNew = annuityService.createTxn(annuity.getPeriodicPayment(), PAYMENT, annuity, 
																	annuityPayout.getPayDate(), null, annuity.getPayAccount());
								if (annuityTxnNew != null) {
									Txn offsetTxn = null;
									if (annuity.getPayAccount() != null) {
										if (annuity.getPayAccount() instanceof Chequing) {
											offsetTxn = chequingService.createTxn(annuity.getPeriodicPayment(), TRANSFER_IN, null,
																			(Chequing) annuity.getPayAccount(), 
																			annuityPayout.getPayDate(),
																			annuityTxnNew.getTxnId(), annuity);
										}
										else if (annuity.getPayAccount() instanceof Savings) {
											offsetTxn = savingsService.createTxn(annuity.getPeriodicPayment(), TRANSFER_IN, 
																			(Savings) annuity.getPayAccount(), 
																			annuityPayout.getPayDate(),
																			annuityTxnNew.getTxnId(), annuity);
										}
									}
									if (offsetTxn != null) {
										annuityTxnNew.setOffsetTxnId(offsetTxn.getTxnId());
									}
									annuity.getAnnuityTxns().add(annuityTxnNew);
									txnNewCnt++;
									System.out.println("hi ian 111.7 create annuity payment txn: " + txnNewCnt );
								}
						
							}
						}
					}
					if (txnNewCnt > 0 ) {
						annuityDao.store(annuity);
					}
				}
			}
		}
	}
	
}
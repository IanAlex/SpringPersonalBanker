package com.ija.pb.junit;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ija.pb.hibernate.dao.*;
import com.ija.pb.hibernate.tables.*;
import com.ija.pb.nondaodata.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:///C:/Users/Ian/SpringFinancial/SpringPersonalBanker/WebContent/WEB-INF/applicationContext.xml"})
public class AbstractSavingsAcctFromChequing {
	
	private String TEST_LOGIN_KEY = "xxyyaabb";
	Date sysDate = new Date();
	
	@Autowired 
	@Qualifier("loginDao") 
	private LoginDao loginDao;
	
	@Autowired 
	@Qualifier("clientDao") 
	private ClientDao clientDao;
	
	@Autowired 
	@Qualifier("chequingDao") 
	private ChequingDao chequingDao;
	
	@Autowired 
	@Qualifier("chequingTxnDao") 
	private ChequingTxnDao chequingTxnDao;
	
	@Autowired 
	@Qualifier("savingsDao") 
	private SavingsDao savingsDao;
	
	@Autowired 
	@Qualifier("savingsTxnDao") 
	private SavingsTxnDao savingsTxnDao;
	
	@Autowired 
	@Qualifier("savingsRateDao") 
	private SavingsRateDao savingsRateDao;
			
	@Test 
	public void runTest() {
		//getClient
		Client client = clientDao.findByLoginKey(TEST_LOGIN_KEY);
		assertNotNull(client);
		//openSavingsPreTxn	
		Savings savings = new Savings();
		savings.setAccountName("BMO savings A");
		savings.setAccountNo("33-563-A1");
		savings.setAccountType("SV");
		savings.setClient(client);
		savings.setJoint(Boolean.FALSE);
		savings.setOpen(Boolean.TRUE);
		savings.setRegistered(Boolean.FALSE);
		Calendar cal = new GregorianCalendar(2011, 2, 13);
		savings.setOpenDt(cal.getTime());
		savings.setDtChanged(sysDate);
		savings.setDtCreated(sysDate);
		savings.setUpdatedBy("SYSTEM");
		savingsDao.store(savings);
		assertNotNull(savings.getAccountId());
		// set up savingsRate 
		SavingsRate savingsRate = new SavingsRate();
		savingsRate.setInterestRate(0.025);
		savingsRate.setEffectiveDt(cal.getTime());
		savingsRate.setSavings(savings);
		savingsRate.setDtChanged(sysDate);
		savingsRate.setDtCreated(sysDate);
		savingsRate.setUpdatedBy("SYSTEM");
		savingsRateDao.store(savingsRate);
		assertNotNull(savingsRate.getSavingsRateId());
		savings.getSavingsRates().add(savingsRate);;
		// set up savingsTxn
		SavingsTxn savingsTxn = new SavingsTxn();
		savingsTxn.setAmount(905.88);
		savingsTxn.setDrCr("D");
		savingsTxn.setReversed(Boolean.FALSE);
		savingsTxn.setSavings(savings);
		savingsTxn.setTxnAcctType("SV");
		savingsTxn.setSavingsTxnType("O");
		//savingsTxn.setOffsetTxnAcctType("CH");
		savingsTxn.setOffsetTxnAcctInfo("CHEQUING");
		savingsTxn.setTxnDt(cal.getTime());
		savingsTxn.setDtChanged(sysDate);
		savingsTxn.setDtCreated(sysDate);
		savingsTxn.setUpdatedBy("SYSTEM");
		savingsTxnDao.store(savingsTxn);
		assertNotNull(savingsTxn.getTxnId());
		// set up offsetting chequingTxn
		ChequingTxn chequingTxn = new ChequingTxn();
		chequingTxn.setAmount(905.88);
		Chequing chequing = chequingDao.findById(1);
		chequingTxn.setChequing(chequing);
		chequingTxn.setChequingTxnType("TO");
		chequingTxn.setOffsetAccount(savings);
		chequingTxn.setOffsetTxnId(savingsTxn.getTxnId());
		chequingTxn.setOffsetTxnAcctInfo("SAVINGS");
		chequingTxn.setDrCr("C");
		chequingTxn.setReversed(Boolean.FALSE);
		chequingTxn.setTxnAcctType("CH");
		chequingTxn.setTxnDt(cal.getTime());
		chequingTxn.setDtChanged(sysDate);
		chequingTxn.setDtCreated(sysDate);
		chequingTxn.setUpdatedBy("SYSTEM");
		chequingTxnDao.store(chequingTxn);
		assertNotNull(chequingTxn.getTxnId());
		savingsTxn.setOffsetTxnId(chequingTxn.getTxnId());
		savingsTxn.setOffsetAccount(chequing);
		savings.getSavingsTxns().add(savingsTxn);
		savingsDao.store(savings);
		assertNotNull(savings);
		//obtainPortfolio
		//client = clientDao.findByLoginKey(TEST_LOGIN_KEY, loginDao);
		client.getAccounts().add(savings);
    	clientDao.store(client);
		assertNotNull(client.getClientId());
		
	}

}

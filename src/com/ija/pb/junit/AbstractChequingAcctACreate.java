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
public class AbstractChequingAcctACreate {
	
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
		
	@Test 
	public void runTest() {
		//getClient
		Client client = clientDao.findByLoginKey(TEST_LOGIN_KEY);
		assertNotNull(client);
		//openChequingPreTxn
		Chequing chequing = new Chequing();
		chequing.setAccountName("National Bank Chequing A");
		chequing.setAccountNo("89-075-D");
		chequing.setAccountType("CH");
		chequing.setClient(client);
		chequing.setJoint(Boolean.FALSE);
		chequing.setOpen(Boolean.TRUE);
		chequing.setRegistered(Boolean.FALSE);
		Calendar cal = new GregorianCalendar(2010, 8, 24);
		chequing.setOpenDt(cal.getTime());
		chequing.setDtChanged(sysDate);
		chequing.setDtCreated(sysDate);
		chequing.setUpdatedBy("SYSTEM");
		chequingDao.store(chequing);
		assertNotNull(chequing.getAccountId());
		//createChequingTxn
		ChequingTxn chequingTxn = new ChequingTxn();
		chequingTxn.setAmount(10502.83);
		chequingTxn.setChequing(chequing);
		chequingTxn.setChequingTxnType("O");
		chequingTxn.setOffsetTxnAcctInfo("CASH");
		chequingTxn.setDrCr("D");
		chequingTxn.setReversed(Boolean.FALSE);
		chequingTxn.setTxnAcctType("CH");
		chequingTxn.setTxnDt(cal.getTime());
		chequingTxn.setDtChanged(sysDate);
		chequingTxn.setDtCreated(sysDate);
		chequingTxn.setUpdatedBy("SYSTEM");
		chequingTxnDao.store(chequingTxn);
		assertNotNull(chequingTxn.getTxnId());
		//add to chequingTxn list in chequing account.
		chequing.getChequingTxns().add(chequingTxn);
		chequingDao.store(chequing);
		assertNotNull(chequing);
		//obtainPortfolio
		//client = clientDao.findByLoginKey(TEST_LOGIN_KEY, loginDao);
		client.getAccounts().add(chequing);
    	clientDao.store(client);
		assertNotNull(client.getClientId());
		Calendar cal1 = new GregorianCalendar(2010, 7, 25);
		Calendar cal1A = new GregorianCalendar(2010, 9, 14);
		Calendar cal2 = new GregorianCalendar(2011, 3, 13);
		List<ChequingTxn> chequingTxnByDts;
		chequingTxnByDts = chequingTxnDao.findInDateRange(chequing, cal1.getTime(), cal2.getTime());
		assertNotNull(chequingTxnByDts);
		chequingTxnByDts = chequingTxnDao.findInDateRange(chequing, cal1A.getTime(), cal2.getTime());
		//assertNull(chequingTxnByDts);
		
		
	};
	

}

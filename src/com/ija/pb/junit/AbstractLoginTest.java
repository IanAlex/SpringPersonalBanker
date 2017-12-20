package com.ija.pb.junit;

import java.util.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ija.pb.hibernate.dao.*;
import com.ija.pb.hibernate.tables.*;

@RunWith(SpringJUnit4ClassRunner.class)
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"file:///C:/Users/Ian/SpringFinancial/SpringPersonalBanker/src/com/ija/pb/junit/personalbanker-tests.xml"})
@ContextConfiguration(locations = {"file:///C:/Users/Ian/SpringFinancial/SpringPersonalBanker/WebContent/WEB-INF/applicationContext.xml"})
public class AbstractLoginTest {
	
	//private  String TEST_LOGIN_USERID = "testA";
	//private  String TEST_LOGIN_PSWD = "yep";
	//private String TEST_LOGIN_KEY = "xxyyaabb";
	//private  String TEST_LOGIN_USERID = "ianalex";
	//private  String TEST_LOGIN_PSWD = "kamala";
	//private String TEST_LOGIN_KEY = "nnui5eq";
	private  String TEST_LOGIN_USERID = "bobchristiani";
	private  String TEST_LOGIN_PSWD = "crazy";
	private String TEST_LOGIN_KEY = "5tye2W";
	
	private Login login;
	
	@Autowired 
	@Qualifier("loginDao") 
	private LoginDao loginDao;
	
	/*
	@Before
	public void init() {
		loginDao = (LoginDao) applicationContext.getBean("loginDao");
	}
	*/
	
	@Test
	public void loginCreate(){
		Login tLogin = loginDao.findForUserInfo(TEST_LOGIN_USERID, TEST_LOGIN_PSWD);
		if (tLogin != null) {
			System.out.println("hi ian loginCreate() --- the login already exists");
		}
		else {
			login = new Login();
			login.setName(TEST_LOGIN_USERID);
			login.setPassword(TEST_LOGIN_PSWD);
			login.setLoginkey(TEST_LOGIN_KEY);
			Date date = new Date();
			login.setDtCreated(date);
			login.setDtChanged(date);
			login.setUpdatedBy("SYSTEM");
			loginDao.store(login);
			System.out.println("hi ian loginCreate()-- after creating loginId is: " + login.getLoginId());
		}
	}
	
	@Test
	public void loginAttempt(){
		login = loginDao.findForUserInfo(TEST_LOGIN_USERID, TEST_LOGIN_PSWD);
		if (login != null) {
			System.out.println("hi ian loginAttempt()-- user logged in successfully");		
		}
		else {
			System.out.println("hi ian loginAttempt() -- login unsuccessful");
		}
	}
	

}

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
public class AbstractPersonCreate {
	
	private  String TEST_LOGIN_USERID = "testA";
	private  String TEST_LOGIN_PSWD = "yep";
	private String TEST_LOGIN_KEY = "xxyyaabb";
	
	
	
	@Autowired 
	@Qualifier("loginDao") 
	private LoginDao loginDao;
	
	@Autowired 
	@Qualifier("addressDao") 
	private AddressDao addressDao;	
	
	@Autowired 
	@Qualifier("personDao") 
	private PersonDao personDao;	
	
	private Date date = new Date();
	
		
	@Test
	public void runTest() {
		//dologin()
		Login login = new Login();
		login = loginDao.findForUserInfo(TEST_LOGIN_USERID, TEST_LOGIN_PSWD);
		assertNotNull(login);
		//createPerson()
		Person person = new Person();
		person.setCellPhone("647-222-8991");
		person.setClientType("P");
		person.setDtChanged(date);
		person.setDtCreated(date);
		person.setEmail("ian.alex2@gmail.com");
		person.setFirstName("Ian");
		person.setLastName("Alex");
		person.setMiddleName("J.");
		person.setGender("M");
		person.setLogin(login);
		person.setMaritalStatus("SI");
		person.setNamePrefix("Mr");
		person.setSsn("111-222-333");
		person.setTelephone("416-888-7777");
		person.setUpdatedBy("SYSTEM");
		//createAddress()
		Address address = new Address();
		address.setStreet("123 Main Street");
		address.setAptUnit("15A");
		address.setCountry("CA");
		address.setStateProv("ON");
		address.setPostalZip("M6Y 2G7");
		address.setUpdatedBy("SYSTEM");
		address.setDtChanged(date);
		address.setDtCreated(date);	
		//saveNewPersonInfo()
		assertNotNull(address);
		assertNotNull(person);
		addressDao.store(address);
		assertNotNull(address.getAddressId());
		person.setAddress(address);
		personDao.store(person);
		assertNotNull(person.getClientId());
	}
	
	
	

}

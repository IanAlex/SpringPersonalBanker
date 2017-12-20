package com.ija.pb.configuration;

import com.ija.pb.hibernate.dao.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class HibernateDaoBeans {
	
	@Bean @Qualifier("loginDao")
	public LoginDao loginDao() {
		return new LoginDaoImpl();
	}
	
	@Bean @Qualifier("addressDao")
	public AddressDao addressDao() {
		return new AddressDaoImpl();
	}
	
	@Bean @Qualifier("clientDao")
	public ClientDao clientDao() {
		return new ClientDaoImpl();
	}
	
	@Bean @Qualifier("personDao")
	public PersonDao personDao() {
		return new PersonDaoImpl();
	}
	
	@Bean @Qualifier("businessDao")
	public BusinessDao businessDao() {
		return new BusinessDaoImpl();
	}
	
		
	@Bean @Qualifier("accountDao")
	public AccountDao accountDao() {
		return new AccountDaoImpl();
	}
	
	@Bean @Qualifier("annuityDao")
	public AnnuityDao annuityDao() {
		return new AnnuityDaoImpl();
	}
	
	@Bean @Qualifier("chequingDao")
	public ChequingDao chequingDao() {
		return new ChequingDaoImpl();
	}
	
	@Bean @Qualifier("gicDao")
	public GICDao gicDao() {
		return new GICDaoImpl();
	}
	
	@Bean @Qualifier("savingsDao")
	public SavingsDao savingsDao() {
		return new SavingsDaoImpl();
	}
	
	@Bean @Qualifier("stockDao")
	public StockDao stockDao() {
		return new StockDaoImpl();
	}
	
	@Bean @Qualifier("annuityTxnDao")
	public AnnuityTxnDao annuityTxnDao() {
		return new AnnuityTxnDaoImpl();
	}
	
	@Bean @Qualifier("chequingTxnDao")
	public ChequingTxnDao chequingTxnDao() {
		return new ChequingTxnDaoImpl();
	}	
	
	@Bean @Qualifier("gicTxnDao")
	public GICTxnDao gicTxnDao() {
		return new GICTxnDaoImpl();
	}
	
	@Bean @Qualifier("savingsTxnDao")
	public SavingsTxnDao savingsTxnDao() {
		return new SavingsTxnDaoImpl();
	}
	
	@Bean @Qualifier("stockTxnDao")
	public StockTxnDao stockTxnDao() {
		return new StockTxnDaoImpl();
	}
	
	@Bean @Qualifier("txnDao")
	public TxnDao txnDao() {
		return new TxnDaoImpl();
	}
	
	@Bean @Qualifier("savingsRateDao")
	public SavingsRateDao savingsRateDao() {
		return new SavingsRateDaoImpl();
	}

}

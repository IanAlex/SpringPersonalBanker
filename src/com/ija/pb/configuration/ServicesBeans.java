package com.ija.pb.configuration;

import java.util.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ija.pb.services.*;

@Configuration
public class ServicesBeans {
	
	@Bean @Qualifier("accountService")
	public AccountService accountService() {
		return new AccountServiceImpl();
	}
	
	@Bean @Qualifier("chequingService")
	public ChequingService chequingService() {
		return new ChequingServiceImpl();
	}
	
	@Bean @Qualifier("savingsService")
	public SavingsService savingsService() {
		return new SavingsServiceImpl();
	}
	
	@Bean @Qualifier("stockService")
	public StockService stockService() {
		return new StockServiceImpl();
	}
	
	@Bean @Qualifier("gicService")
	public GICService gicService() {
		return new GICServiceImpl();
	}
	
	@Bean @Qualifier("annuityService")
	public AnnuityService annuityService() {
		return new AnnuityServiceImpl();
	}
	
	@Bean @Qualifier("txnService")
	public TxnService txnService() {
		return new TxnServiceImpl();
	}
	
	@Bean @Qualifier("utilityService")
	public UtilityService utilityService() {
		return new UtilityServiceImpl();
	}

}

package com.ija.jsf.mvc;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

import com.ija.pb.hibernate.dao.*;
import com.ija.pb.hibernate.tables.*;
import com.ija.pb.nondaodata.*;
import com.ija.pb.services.*;

@Component
//@ViewScoped
@Scope("request")
public class PortfolioMVC implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Autowired 
	@Qualifier("clientDao")
	private ClientDao clientDao;
	@Autowired 
	@Qualifier("accountDao")
	private AccountDao accountDao;
	@Autowired 
	@Qualifier("chequingDao")
	private ChequingDao chequingDao;
	@Autowired 
	@Qualifier("savingsDao")
	private SavingsDao savingsDao;
	@Autowired 
	@Qualifier("gicDao")
	private GICDao gicDao;
	@Autowired 
	@Qualifier("annuityDao")
	private AnnuityDao annuityDao;
	@Autowired 
	@Qualifier("stockDao")
	private StockDao stockDao;
	@Autowired 
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired 
	@Qualifier("chequingService")
	private ChequingService chequingService;
	@Autowired 
	@Qualifier("savingsService")
	private SavingsService savingsService;
	@Autowired 
	@Qualifier("stockService")
	private StockService stockService;
	@Autowired 
	@Qualifier("gicService")
	private GICService gicService;
	@Autowired 
	@Qualifier("annuityService")
	private AnnuityService annuityService;
	@Autowired 
	private SavingsDetailMVC savDetMVC;
	@Autowired 
	private ChequingDetailMVC chqDetMVC;
	@Autowired 
	private GICDetailMVC gicDetMVC;
	@Autowired 
	private AnnuityDetailMVC annuityDetMVC;
	@Autowired 
	private StockDetailMVC stockDetMVC;
	
	
	@Autowired
	CurrentScreenInfo currentScreenInfo;
	
	private static final String STOCK = "ST";
	private static final String GIC = "GC";
	private static final String ANNUITY = "AN";
	private static final String CHEQUING = "CH";
	private static final String SAVINGS = "SV";
	
	
	private List<Account> portfolioList;
	
	private Account selectedAccount;
	
	public PortfolioMVC() {
	}
	
	@PostConstruct
	public void init() {
		if (currentScreenInfo != null && currentScreenInfo.getClient() != null) {
			portfolioList = new ArrayList<Account>();
			currentScreenInfo.setClient(clientDao.findByLoginKey(currentScreenInfo.getLoginKey()));
			Set<Account> portfolioListA = currentScreenInfo.getClient().getAccounts();
			for (Account account : portfolioListA) {
				if (account instanceof Chequing) {
					account.setBalance(chequingService.calcBalance( ((Chequing) account).getChequingTxns()));
				}
				else if (account instanceof Savings) {
					account.setBalance(savingsService.calcBalance( ((Savings) account).getSavingsTxns()));
				}
				else if (account instanceof GIC) {
					account.setBalance(gicService.calcBalance( ((GIC) account).getGicTxns()));
				}
				else if (account instanceof Stock) {
					account.setBalance(new Double(stockService.calcNoShares( ((Stock) account).getStockTxns())));
				}
				else if (account instanceof Annuity) {
					account.setBalance(annuityService.calcPV((Annuity) account, new Date()));
				}
				account.setDisplay(accountService.makeGenericAccountDisplay(account, Boolean.TRUE));
				portfolioList.add(account);
			}
		}
		System.out.println("hi ian finished Portfolio.init()");
	}
	public List<Account> getPortfolioList() {
		return this.portfolioList;
	}
 
	public void setPortfolioList(List<Account> portfolioList) {
		this.portfolioList = portfolioList;
	}
	
	public Account getselectedAccount() {
		return this.selectedAccount;
	}
 
	public void setselectedAccount(Account selectedAccount) {
		this.selectedAccount = selectedAccount;
	}	
	
	public String doSelectedDetail() {	
		currentScreenInfo.setChequing(null);
		currentScreenInfo.setSavings(null);
		currentScreenInfo.setAnnuity(null);
		currentScreenInfo.setGic(null);
		currentScreenInfo.setStock(null);
		if (selectedAccount != null) {
			if (selectedAccount instanceof Chequing) {
				currentScreenInfo.setChequing((Chequing)selectedAccount);
				if (chqDetMVC != null) {
					chqDetMVC.init();
				}
			}
			else if (selectedAccount instanceof Savings) {
				currentScreenInfo.setSavings((Savings)selectedAccount);
				if (savDetMVC != null) {
					savDetMVC.init();
				}
			}
			else if (selectedAccount instanceof GIC) {
				currentScreenInfo.setGic((GIC)selectedAccount);
				if (gicDetMVC != null) {
					gicDetMVC.init();
				}
			}
			else if (selectedAccount instanceof Annuity) {
				currentScreenInfo.setAnnuity((Annuity)selectedAccount);
				if (annuityDetMVC != null) {
					annuityDetMVC.init();
				}
			}
			else if (selectedAccount instanceof Stock) {
				currentScreenInfo.setStock((Stock)selectedAccount);
				if (stockDetMVC != null) {
					stockDetMVC.init();
				}
			}
			return "account-detail.xhtml?faces-redirect=true";
		}
		else {
			return null;  //stay on page if no record was selected.
		}
	}
	
	
}

package com.ija.pb.services;

import java.util.*;
import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.apache.commons.lang.SerializationUtils;

import com.ija.pb.hibernate.tables.*;
import com.ija.pb.hibernate.dao.*;
import com.ija.pb.nondaodata.*;

@Service
public class AccountServiceImpl implements AccountService {

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
	@Qualifier("chequingService") 
	private ChequingService chequingService;
	@Autowired 
	@Qualifier("savingsService") 	
	private SavingsService savingsService;
	@Autowired 
	private Translations translations;
	
	private static final String CASH = "Cash";
	private static final String CHEQUING = "CH";
	private static final String SAVINGS = "SV";
	
	@Override
	// LinkedHashMap for dropdown in form <"Desc", "AcctId|AcctType"> (<label,value>)
	public Map<String, String> makeTransferCashChequeSavingsMap(Client client, Account curExcludeAccount, Boolean inclCash, Boolean transferOut) {
		Set<Account> DBaccounts = new HashSet<Account>();
		DBaccounts.addAll(accountDao.findForClientByAccountType(client, CHEQUING, Boolean.TRUE));
		DBaccounts.addAll(accountDao.findForClientByAccountType(client, SAVINGS, Boolean.TRUE));
		// set up non-binding accounts set in case we need to remove.		
		Set<Account> accounts = new HashSet<Account>();
		for (Account taccount : DBaccounts) {
			if (curExcludeAccount == null || !taccount.getAccountId().equals(curExcludeAccount.getAccountId())) {
				accounts.add(taccount);
			}
		}
		Map<String, String> acctMap = new LinkedHashMap<String, String>();
		if (inclCash) {
			acctMap.put(CASH, CASH);
		}
		DecimalFormat df = new DecimalFormat("0.##");
		for (Account account : accounts) {
			double balance;
			if (account.getOpen()) {
				if (account.getAccountType().equals(CHEQUING)) {
					Chequing chequing = chequingDao.findById(account.getAccountId());
					balance = chequingService.calcBalance(chequing.getChequingTxns());
				}
				else {
					Savings savings = savingsDao.findById(account.getAccountId());
					balance = savingsService.calcBalance(savings.getSavingsTxns());
				}
				if (balance > 0 || !transferOut) {
					String desc = translations.getAccounttypes().get(account.getAccountType()) + "   AcctNo: " +
									account.getAccountNo() + "  Balance:  "  + df.format(balance); 
					String acctIdType = account.getAccountId() + "|" + account.getAccountType();
					acctMap.put(desc, acctIdType);
				}
			}
		}		
		return acctMap;
	}
	
	@Override
	public String makeGenericAccountDisplay(Account account, Boolean checkRegistered) {
		String display =  translations.getAccounttypes().get(account.getAccountType()) +
				((checkRegistered && account.getRegistered()) ? " (Registered) " : " ") +
				(account.getJoint() ? " (Joint) " : " ");
		return display;
	}
	
	@Override
	public String makeSpecificAccountDisplay(Account account, Boolean checkRegistered) {
		String display =  translations.getAccounttypes().get(account.getAccountType()) +
				((checkRegistered && account.getRegistered()) ? " (Registered) " : " ") +
				" Account No.: " + account.getAccountNo();
		return display;
	}

}

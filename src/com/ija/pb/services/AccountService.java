package com.ija.pb.services;

import java.util.*;

import com.ija.pb.hibernate.tables.*;

public interface AccountService {
	
	Map<String, String> makeTransferCashChequeSavingsMap(Client client, Account curExcludeAccount, Boolean inclCash, Boolean transferOut );
	String makeGenericAccountDisplay(Account account, Boolean checkRegistered);
	String makeSpecificAccountDisplay(Account account, Boolean checkRegistered);

}

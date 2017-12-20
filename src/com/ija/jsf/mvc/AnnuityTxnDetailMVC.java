package com.ija.jsf.mvc;

import java.util.*;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.faces.component.UIComponent;

import com.ija.pb.hibernate.dao.*;
import com.ija.pb.hibernate.tables.*;
import com.ija.pb.nondaodata.*;
import com.ija.pb.services.*;

@Component
@Scope("request")
public class AnnuityTxnDetailMVC implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Autowired
	CurrentScreenInfo currentScreenInfo;
	@Autowired
	Translations translations;
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
	@Qualifier("txnService")
	private TxnService txnService;
	@Autowired 
	@Qualifier("accountDao")
	private AccountDao accountDao;
	
	private static final String TRANSFER_IN = "TI";
	private static final String TRANSFER_OUT = "TO";
	private static final String FUNDS_OUT = "W";
	private static final String CASH = "Cash";
	
	
	String txnDesc1, txnDesc2;
	private Map<String,String> transferAccountMap;
	private String selectedTransferAcctInfo;
	private String offsetAcctInfo;
	private AnnuityTxn viewAnnuityTxn;
	
	public AnnuityTxnDetailMVC() {
	}
	
	@PostConstruct
	public void init() {
		String txnType = "";
		if (currentScreenInfo.getTxn() != null  && currentScreenInfo.getTxn() instanceof AnnuityTxn) {
			txnType = ((AnnuityTxn)currentScreenInfo.getTxn()).getAnnuityTxnType();
			txnDesc2 = translations.getAnnuitytxntypes().get(txnType) + "  Transaction Detail";
			viewAnnuityTxn = (AnnuityTxn)currentScreenInfo.getTxn();
			if (viewAnnuityTxn.getOffsetAccount() != null){
				offsetAcctInfo = accountService.makeSpecificAccountDisplay(viewAnnuityTxn.getOffsetAccount(), Boolean.TRUE);
			}
			else {
				offsetAcctInfo = CASH;
			}
		}
		txnDesc1 = "Annuity ACCOUNT - Acct. No.: " + currentScreenInfo.getAnnuity().getAccountNo();
	}
	
	public String getTxnDesc1() {
		return this.txnDesc1;
	}
 
	public void setTxnDesc1(String txnDesc1) {
		this.txnDesc1 = txnDesc1;
	}
	
	public String getTxnDesc2() {
		return this.txnDesc2;
	}
 
	public void setTxnDesc2(String txnDesc2) {
		this.txnDesc2 = txnDesc2;
	}
	
	public Map<String,String> getTransferAccountMap() {
		return this.transferAccountMap;
	}
 
	public void setTransferAccountMap(Map<String,String> transferAccountMap) {
		this.transferAccountMap = transferAccountMap;
	}	
	
	public String getSelectedTransferAcctInfo() {
		return this.selectedTransferAcctInfo;
	}
 
	public void setSelectedTransferAcctInfo(String selectedTransferAcctInfo) {
		this.selectedTransferAcctInfo = selectedTransferAcctInfo;
	}
	
	public String getOffsetAcctInfo() {
		return this.offsetAcctInfo;
	}
 
	public void setOffsetAcctInfo(String offsetAcctInfo) {
		this.offsetAcctInfo = offsetAcctInfo;
	}
	
	public AnnuityTxn getViewAnnuityTxn() {
		return this.viewAnnuityTxn;
	}
 
	public void setViewAnnuityTxn(AnnuityTxn viewAnnuityTxn) {
		this.viewAnnuityTxn = viewAnnuityTxn;
	}
	
	public CurrentScreenInfo getcurrentScreenInfo() {
		return this.currentScreenInfo;
	}
 
	public void setcurrentScreenInfo(CurrentScreenInfo currentScreenInfo) {
		this.currentScreenInfo = currentScreenInfo;
	}	
	
	public String cancel() {
		///need to do navigation
		return null;
	}
	
	

}

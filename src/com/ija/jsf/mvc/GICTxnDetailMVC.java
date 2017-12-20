package com.ija.jsf.mvc;

import java.util.*;
import java.text.SimpleDateFormat;

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
import javax.faces.component.UIInput;
import javax.faces.event.ComponentSystemEvent;

import com.ija.pb.hibernate.dao.*;
import com.ija.pb.hibernate.tables.*;
import com.ija.pb.nondaodata.*;
import com.ija.pb.services.*;

@Component
@Scope("request")
public class GICTxnDetailMVC implements Serializable {
	
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
	@Qualifier("gicService")
	private GICService gicService;
	@Autowired 
	@Qualifier("txnService")
	private TxnService txnService;
	@Autowired 
	@Qualifier("accountDao")
	private AccountDao accountDao;	
	@Autowired 
	@Qualifier("gicDao")
	private GICDao gicDao;
	@Autowired
	GICDetailMVC gicDetailMVC;
	
	private static final String TRANSFER_IN = "TI";
	private static final String TRANSFER_OUT = "TO";
	private static final String FUNDS_OUT = "W";
	private static final String CASH = "Cash";
	
	
	String txnDesc1, txnDesc2;
	private Map<String,String> transferAccountMap;
	private String selectedTransferAcctInfo;
	private String offsetAcctInfo;
	private GICTxn viewGICTxn;
	
	boolean valAmountError;
	boolean valTxnDtError;
	
	public GICTxnDetailMVC() {
	}
	
	@PostConstruct
	public void init() {
		String txnType = "";
		// not used - there's now a dialog for existing Txn
		if (currentScreenInfo.getTxn() != null  && currentScreenInfo.getTxn() instanceof GICTxn) {
			txnType = ((GICTxn)currentScreenInfo.getTxn()).getGicTxnType();
			//txnDesc2 = translations.getGictxntypes().get(txnType) + "  Transaction Detail";
			viewGICTxn = (GICTxn)currentScreenInfo.getTxn();
			if (viewGICTxn.getOffsetAccount() != null){
				offsetAcctInfo = accountService.makeSpecificAccountDisplay(viewGICTxn.getOffsetAccount(), Boolean.TRUE);
			}
			else {
				offsetAcctInfo = CASH;
			}
		}
		//
		else if (currentScreenInfo.getNewTxn()) {
			txnType = currentScreenInfo.getTxnType();
			//txnDesc2 = " Create New " + translations.getGictxntypes().get(txnType) + "  Transaction Detail";
			viewGICTxn = new GICTxn();
		}
		transferAccountMap = accountService.makeTransferCashChequeSavingsMap(currentScreenInfo.getClient(), null, Boolean.TRUE, Boolean.FALSE);
		for(Map.Entry<String, String> entry : transferAccountMap.entrySet()) {
			selectedTransferAcctInfo = entry.getValue();
			break;
		}
		txnDesc2 = "Create new " + translations.getGictxntypes().get(txnType) + " transaction";
		txnDesc1 = "GIC/Term Deposit Account No.: " + currentScreenInfo.getGic().getAccountNo();
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
	
	public GICTxn getViewGICTxn() {
		return this.viewGICTxn;
	}
 
	public void setViewGICTxn(GICTxn viewGICTxn) {
		this.viewGICTxn = viewGICTxn;
	}
	
	public CurrentScreenInfo getcurrentScreenInfo() {
		return this.currentScreenInfo;
	}
 
	public void setcurrentScreenInfo(CurrentScreenInfo currentScreenInfo) {
		this.currentScreenInfo = currentScreenInfo;
	}
	
	public void validateAmount(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException  
    {
		Double amount = (Double) value;
		if (amount.doubleValue() <= 0) {
			valAmountError = true;
			FacesMessage message = new FacesMessage("Amount must be at least 1 cent");
			throw new ValidatorException(message);
		}
		double availGICBalance = gicService.calcBalance(currentScreenInfo.getGic().getGicTxns());
		if (amount.doubleValue() > availGICBalance) {
			valAmountError = true;
			String msgStr = translations.getGictxntypes().get(currentScreenInfo.getTxnType()) + 
					" amount cannot be greater than the $" +
					 String.format("%10.2f", availGICBalance) +
					 " in this GIC/Term Deposit account";
			FacesMessage message = new FacesMessage(msgStr);
			throw new ValidatorException(message);
		}
    }
	
	public void validateTransactionDt(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException  
    {
		Date enteredDt = (Date) value;
		Date sysDt = new Date();
		if (enteredDt.before(currentScreenInfo.getGic().getOpenDt())) {
			valTxnDtError = true;
			FacesMessage message = new FacesMessage("Transaction Date cannot come before GIC/Term Deposit Account Open Date (" +
														(new SimpleDateFormat("dd-MMM-yyyy")).format(currentScreenInfo.getGic().getOpenDt()) + ")");
			throw new ValidatorException(message);
		}
		if (enteredDt.after(sysDt)) {
			valTxnDtError = true;
			FacesMessage message = new FacesMessage("Transaction Date cannot come after System Date (today)");
			throw new ValidatorException(message);
		}
    }
	
	// used in postvalidate for amount/txnDt since we need the transfer account
	public void validateForTransfers(ComponentSystemEvent event) {
		FacesContext fc = FacesContext.getCurrentInstance();	
		boolean errFlag = false;
		System.out.println("hi ian 600A");
		UIComponent components = event.getComponent();
		UIInput uiAmount = (UIInput) components.findComponent("amount");
		Double amount = (Double) uiAmount.getLocalValue();
		String amountId = uiAmount.getClientId();
		System.out.println("hi ian 610 amount = " + amount);
		UIInput uiTxnDt = (UIInput) components.findComponent("txnDt");
		Date txnDt = (Date) uiTxnDt.getLocalValue();
		String txnDtId = uiTxnDt.getClientId();
		System.out.println("hi ian 620 txnDt = " + txnDt);
		// validation of amount/txnDt against transfer account
		if (amount != null && txnDt != null && !valAmountError && !valTxnDtError) {
			UIInput uiTransferInfo = (UIInput) components.findComponent("transferAcctInfo");
			String transferInfo = (String) uiTransferInfo.getLocalValue();
			if (transferInfo != null && !transferInfo.equals(CASH)) { 
				System.out.println("hi ian 630 amount = " + transferInfo);
				StringTokenizer st = new StringTokenizer(transferInfo, "|");
				Integer acctId = Integer.parseInt(st.nextToken());
				String acctType = st.nextToken();
				Account offsetAcct = accountDao.findById(acctId);
				if (offsetAcct != null) {
					if (txnDt.before(offsetAcct.getOpenDt())) {
						FacesMessage msg = new FacesMessage("Transaction Date cannot be before Transfer Account open date (" +
								(new SimpleDateFormat("dd-MMM-yyyy")).format(offsetAcct.getOpenDt()) + ")");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						fc.addMessage(txnDtId, msg);
						fc.renderResponse();
						errFlag = true;
					}
					if (errFlag) {
						//fc.renderResponse();
					}
				}
			}
		}
				valAmountError = false;
				valTxnDtError = false;
	}
	
	public String processNewGICTxn() {
		System.out.println("hi ian 710");
		GICTxn gicTxn;
		GIC gic = currentScreenInfo.getGic();
		Account offsetAcct = null;		
		Txn offsetTxn = null;
		if (!selectedTransferAcctInfo.equals(CASH)) {
			StringTokenizer st = new StringTokenizer(selectedTransferAcctInfo, "|");
			Integer acctId = Integer.parseInt(st.nextToken());
			String acctType = st.nextToken();
			offsetAcct = accountDao.findById(acctId);
		}
		gicTxn = gicService.createTxn(viewGICTxn.getAmount(), currentScreenInfo.getTxnType(), gic,
								viewGICTxn.getTxnDt(), null, offsetAcct);
		if (gicTxn != null) {
			if (offsetAcct != null) {
				System.out.println("there's an offset acct");
				if (offsetAcct instanceof Chequing) {
					System.out.println("offset is chequing");
					offsetTxn = chequingService.createTxn(viewGICTxn.getAmount(), TRANSFER_IN,
											null, (Chequing) offsetAcct, viewGICTxn.getTxnDt(),
											gicTxn.getTxnId(), gic);
				}
				else if (offsetAcct instanceof Savings) {
					System.out.println("offset is savings");
					//SavingsTxn createTxn(Double amount, String savingsTxnType, Savings savings, 
							//Date txnDt, Integer offsetTxnId, Account offsetAccount)
					offsetTxn = savingsService.createTxn(viewGICTxn.getAmount(), TRANSFER_IN,
										(Savings) offsetAcct, viewGICTxn.getTxnDt(), 
										gicTxn.getTxnId(), gic);
				}			
			}
			if (offsetTxn != null) {
				gicTxn.setOffsetTxnId(offsetTxn.getTxnId());
			}
			gic.getGicTxns().add(gicTxn);
			gicDao.store(gic);
			currentScreenInfo.setNewTxn(Boolean.FALSE);
			currentScreenInfo.setTxnType(null);
			if (gicDetailMVC != null) {
				gicDetailMVC.getGic().setBalance(gicService.calcBalance(gic.getGicTxns()));
				gicService.updateRunningTotals(gic);
				gicDetailMVC.retrieveTxnList();
			}
		}
		return "account-detail.xhtml?faces-redirect=true";
	}
	
	public String cancel() {
		currentScreenInfo.setNewTxn(Boolean.FALSE);
		return "account-detail.xhtml?faces-redirect=true";
	}	

}

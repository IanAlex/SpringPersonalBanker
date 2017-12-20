package com.ija.jsf.mvc;

import java.util.*;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.component.UIInput;
import javax.faces.event.ComponentSystemEvent;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ija.pb.hibernate.dao.*;
import com.ija.pb.hibernate.tables.*;
import com.ija.pb.nondaodata.*;
import com.ija.pb.services.*;

@Component
@Scope("request")
public class SavingsTxnDetailMVC implements Serializable {
	
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
	@Autowired 
	@Qualifier("savingsDao")
	private SavingsDao savingsDao;
	@Autowired 
	@Qualifier("savingsTxnDao")
	private SavingsTxnDao savingsTxnDao;	
	@Autowired
	SavingsDetailMVC savingsDetailMVC;
	
	private static final String TRANSFER_IN = "TI";
	private static final String TRANSFER_OUT = "TO";
	private static final String WITHDRAWAL = "W";
	
	private static final String SAVINGS = "SV";
	private static final String CHEQUING = "CH";
	
	String txnDesc1, txnDesc2;
	Boolean transferFlag;
	Boolean chequeIssueFlag;
	private Map<String,String> transferAccountMap;
	private String selectedTransferAcctInfo;
	private String offsetAcctInfo;
	private SavingsTxn viewSavingsTxn;
	
	private boolean valAmountError;
	private boolean valTxnDtError;
	
	public SavingsTxnDetailMVC() {
	}
	
	@PostConstruct
	public void init() {
		String txnType = "";
		// not used - there's now a dialog for existing Txn
		if (currentScreenInfo.getTxn() != null  && currentScreenInfo.getTxn() instanceof SavingsTxn) {
			txnType = ((SavingsTxn)currentScreenInfo.getTxn()).getSavingsTxnType();
			txnDesc2 = translations.getSavingstxntypes().get(txnType) + "  Transaction Detail";
			viewSavingsTxn = (SavingsTxn)currentScreenInfo.getTxn();
			if (viewSavingsTxn.getOffsetAccount() != null) {
				offsetAcctInfo = accountService.makeSpecificAccountDisplay(viewSavingsTxn.getOffsetAccount(), Boolean.TRUE);
			}
		}
		//
		else if (currentScreenInfo.getNewTxn()) {
			txnType = currentScreenInfo.getTxnType();
			//txnDesc2 = " Create New " + translations.getSavingstxntypes().get(txnType) + "  Transaction Detail";
			viewSavingsTxn = new SavingsTxn();
		}
		if (txnType.equals(TRANSFER_IN) || txnType.equals(TRANSFER_OUT)) {
			transferFlag = Boolean.TRUE;
			transferAccountMap = accountService.makeTransferCashChequeSavingsMap(currentScreenInfo.getClient(), currentScreenInfo.getSavings(), Boolean.FALSE, txnType.equals(TRANSFER_IN));
			for(Map.Entry<String, String> entry : transferAccountMap.entrySet()) {
				selectedTransferAcctInfo = entry.getValue();
				break;
			}	
		}
		else {
			transferFlag = Boolean.FALSE;
			transferAccountMap = new LinkedHashMap<String,String>();
		}
		txnDesc2 = "Create new " + translations.getSavingstxntypes().get(txnType) + " transaction";
		txnDesc1 = "Savings Account No.: " + currentScreenInfo.getSavings().getAccountNo();
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
	
	public Boolean getTransferFlag() {
		return this.transferFlag;
	}
 
	public void setTransferFlag(Boolean transferFlag) {
		this.transferFlag = transferFlag;
	}
	
	public Boolean getChequeIssueFlag() {
		return this.chequeIssueFlag;
	}
 
	public void setChequeIssueFlag(Boolean chequeIssueFlag) {
		this.chequeIssueFlag = chequeIssueFlag;
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
	
	public SavingsTxn getViewSavingsTxn() {
		return this.viewSavingsTxn;
	}
 
	public void setviewSavingsTxn(SavingsTxn viewSavingsTxn) {
		this.viewSavingsTxn = viewSavingsTxn;
	}
	
	public CurrentScreenInfo getcurrentScreenInfo() {
		return this.currentScreenInfo;
	}
 
	public void setcurrentScreenInfo(CurrentScreenInfo currentScreenInfo) {
		this.currentScreenInfo = currentScreenInfo;
	}
	
	// used in postvalidate for amount/txnDt since we need the transfer account
		public void validateForTransfers(ComponentSystemEvent event) {
			if (transferFlag) {
				FacesContext fc = FacesContext.getCurrentInstance();	
				boolean errFlag = false;
				System.out.println("hi ian 400A");
				UIComponent components = event.getComponent();
				UIInput uiAmount = (UIInput) components.findComponent("amount");
				Double amount = (Double) uiAmount.getLocalValue();
				String amountId = uiAmount.getClientId();
				System.out.println("hi ian 410 amount = " + amount);
				UIInput uiTxnDt = (UIInput) components.findComponent("txnDt");
				Date txnDt = (Date) uiTxnDt.getLocalValue();
				String txnDtId = uiTxnDt.getClientId();
				System.out.println("hi ian 420 txnDt = " + txnDt);
				// validation of amount/txnDt against transfer account
				if (amount != null && txnDt != null && !valAmountError && !valTxnDtError) {
					UIInput uiTransferInfo = (UIInput) components.findComponent("transferAcctInfo");
					String transferInfo = (String) uiTransferInfo.getLocalValue();
					System.out.println("hi ian 430 amount = " + transferInfo);
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
						if (currentScreenInfo.getTxnType().equals(TRANSFER_IN)) {
							double availOffsetBalance = 0;
							if (offsetAcct instanceof Chequing) {
								availOffsetBalance = chequingService.calcBalance(((Chequing) offsetAcct).getChequingTxns());
							}
							else if (offsetAcct instanceof Savings) {
								availOffsetBalance = savingsService.calcBalance(((Savings) offsetAcct).getSavingsTxns());
							}
							if (amount.doubleValue() > availOffsetBalance) {
								String msgStr = translations.getSavingstxntypes().get(currentScreenInfo.getTxnType()) + 
										   " amount cannot be greater than the $" +
										   String.format("%10.2f", availOffsetBalance) +
										   " in the Transfer account";
								FacesMessage msg = new FacesMessage(msgStr);
								msg.setSeverity(FacesMessage.SEVERITY_ERROR);
								fc.addMessage(amountId, msg);
								fc.renderResponse();
								errFlag = true;
							}
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
	
	public void validateAmount(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException  
    {
		Double amount = (Double) value;
		if (amount.doubleValue() <= 0) {
			valAmountError = true;
			FacesMessage message = new FacesMessage("Amount must be at least 1 cent");
			throw new ValidatorException(message);
		}
		if (currentScreenInfo.getTxnType().equals(TRANSFER_OUT) || 
				currentScreenInfo.getTxnType().equals(WITHDRAWAL))
		{
			double availSavBalance = savingsService.calcBalance(currentScreenInfo.getSavings().getSavingsTxns());
			if (amount.doubleValue() > availSavBalance) {
				valAmountError = true;
				String msgStr = translations.getSavingstxntypes().get(currentScreenInfo.getTxnType()) + 
						" amount cannot be greater than the $" +
						 String.format("%10.2f", availSavBalance) +
						 " in this Savings account";
				FacesMessage message = new FacesMessage(msgStr);
				throw new ValidatorException(message);
			}			
		}
    }
	
	public void validateTransactionDt(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException  
    {
		Date enteredDt = (Date) value;
		Date sysDt = new Date();
		if (enteredDt.before(currentScreenInfo.getSavings().getOpenDt())) {
			valTxnDtError = true;
			FacesMessage message = new FacesMessage("Transaction Date cannot come before Savings Account Open Date (" +
														(new SimpleDateFormat("dd-MMM-yyyy")).format(currentScreenInfo.getSavings().getOpenDt()) + ")");
			throw new ValidatorException(message);
		}
		if (enteredDt.after(sysDt)) {
			valTxnDtError = true;
			FacesMessage message = new FacesMessage("Transaction Date cannot after system date (today)");
			throw new ValidatorException(message);
		}
    }	
	
	public String processNewSavingsTxn() {
		System.out.println("hi ian 500C");
		SavingsTxn savingsTxn;
		Savings savings = currentScreenInfo.getSavings();
		Account offsetAcct = null;		
		Txn offsetTxn = null;
		if (transferFlag) {
			StringTokenizer st = new StringTokenizer(selectedTransferAcctInfo, "|");
			Integer acctId = Integer.parseInt(st.nextToken());
			offsetAcct = accountDao.findById(acctId);
		}
		savingsTxn = savingsService.createTxn(viewSavingsTxn.getAmount(), currentScreenInfo.getTxnType(), 
								savings, viewSavingsTxn.getTxnDt(), null, offsetAcct);
		if (savingsTxn != null) {
			if (offsetAcct != null) {
				System.out.println("there's an offset acct");
				if (offsetAcct instanceof Chequing) {
					System.out.println("offset is chequing");
					offsetTxn = chequingService.createTxn(viewSavingsTxn.getAmount(), (currentScreenInfo.getTxnType().equals(TRANSFER_OUT) ? TRANSFER_IN : TRANSFER_OUT),
											null, (Chequing) offsetAcct, viewSavingsTxn.getTxnDt(),
											savingsTxn.getTxnId(), savings);
				}
				else if (offsetAcct instanceof Savings) {
					System.out.println("offset is savings");
					//SavingsTxn createTxn(Double amount, String savingsTxnType, Savings savings, 
							//Date txnDt, Integer offsetTxnId, Account offsetAccount)
					offsetTxn = savingsService.createTxn(viewSavingsTxn.getAmount(), (currentScreenInfo.getTxnType().equals(TRANSFER_OUT) ? TRANSFER_IN : TRANSFER_OUT),
										(Savings) offsetAcct, viewSavingsTxn.getTxnDt(), 
										savingsTxn.getTxnId(), savings);
				}			
			}
			if (offsetTxn != null) {
				savingsTxn.setOffsetTxnId(offsetTxn.getTxnId());
				savingsTxnDao.store(savingsTxn);
			}			
			//refresh savings after int txns incoporated
			savings = savingsDao.findById(savings.getAccountId());
			//savingsDao.store(savings);
			currentScreenInfo.setSavings(savings);
			currentScreenInfo.setNewTxn(Boolean.FALSE);
			currentScreenInfo.setTxnType(null);
			if (savingsDetailMVC != null) {
				savingsDetailMVC.setSavings(savings);
				savingsDetailMVC.getSavings().setBalance(savingsService.calcBalance(savings.getSavingsTxns()));
				savingsService.updateRunningTotals(savingsDetailMVC.getSavings());
				savingsDetailMVC.retrieveTxnList();
			}
		}
		return "account-detail.xhtml?faces-redirect=true";
	}
	
	public String cancel() {
		currentScreenInfo.setNewTxn(Boolean.FALSE);
		return "account-detail.xhtml?faces-redirect=true";
	}
	
	

}

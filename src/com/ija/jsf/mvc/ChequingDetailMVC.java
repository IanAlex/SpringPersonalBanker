package com.ija.jsf.mvc;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.primefaces.event.SelectEvent;
import javax.faces.event.ActionEvent;

import com.ija.pb.hibernate.dao.*;
import com.ija.pb.hibernate.tables.*;
import com.ija.pb.nondaodata.*;
import com.ija.pb.services.*;

@Component
@ViewScoped
public class ChequingDetailMVC implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Autowired 
	@Qualifier("chequingDao")
	private ChequingDao chequingDao;
	@Autowired 
	@Qualifier("txnDao")
	private TxnDao txnDao;
	@Autowired 
	@Qualifier("chequingTxnDao")
	private ChequingTxnDao chequingTxnDao;
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
	@Qualifier("txnService")
	private TxnService txnService;
	@Autowired 
	@Qualifier("utilityService")
	private UtilityService utilityService;
	
	private static final String TRANSFER_IN = "TI";
	private static final String TRANSFER_OUT = "TO";
	private static final String OPEN_ACCOUNT = "O";
	private static final String ISSUE_CHEQUE = "C";
	private static final String OPEN_ACCOUNT_GEN_DESC = "Open Account";
	private static final String ALL = "ALL";

	
	private static final String CASH = "Cash";
	
	
	
	private List<ChequingTxn> txnList;
	private ChequingTxn selectedTxn;
	private Chequing chequing;
	private Map<String,String> chequingTxnTypesMap;
	private Map<String,String> chequingTxnTypesQueryMap;
	private String selectedChequingTxnType;
	private String selectedChequingTxnQueryType;
	
	private Boolean transferFlag;
	private Boolean chequeIssueFlag;
	private String offsetAcctInfo;
	private String statusDesc;
	private String drcrDesc;
	private String txntypeDesc;
	private String offsetTxnType;
	
	private Boolean useAllDates;
	private Date txnDtMin;
	private Date txnDtMax;
	
	public ChequingDetailMVC() {
	}
	
	@PostConstruct
	public void init() {
		if (currentScreenInfo != null) {
			chequing = currentScreenInfo.getChequing();		
		}
		if (chequing != null) {
			chequingService.updateRunningTotals(chequing);
			txnList = chequingTxnDao.findWithinTxnDtRangeByType(chequing, chequing.getOpenDt(), new Date(), ALL, "ASC");
			for (ChequingTxn chequingTxn : txnList ) {
				String display = translations.getChequingtxntypes().get(chequingTxn.getChequingTxnType()) +
					      	((chequingTxn.getReversed()) ? " (Reversed)" : " (Original)");
					         
				chequingTxn.setDisplay(display);
			}
			Map<String,String> chqSavTOMap = accountService.makeTransferCashChequeSavingsMap(currentScreenInfo.getClient(), currentScreenInfo.getChequing(), Boolean.FALSE, Boolean.TRUE);
			Map<String,String> chqSavTIMap = accountService.makeTransferCashChequeSavingsMap(currentScreenInfo.getClient(), currentScreenInfo.getChequing(), Boolean.FALSE, Boolean.FALSE);	
			Map<String,String> chqTxnOrigMap = new HashMap<String,String>();
			chqTxnOrigMap.putAll(translations.getChequingtxntypes());
			if (chqSavTOMap.isEmpty()) {
				chqTxnOrigMap.remove(TRANSFER_IN);
			}
			if (chqSavTIMap.isEmpty()) {
				chqTxnOrigMap.remove(TRANSFER_OUT);
			}
			chqTxnOrigMap.remove(OPEN_ACCOUNT);
			chequingTxnTypesMap = utilityService.createStandardGUIDisplayMap(chqTxnOrigMap);
			chqTxnOrigMap = new TreeMap<String,String>();
			chqTxnOrigMap.putAll(translations.getChequingtxntypes());
			chqTxnOrigMap.put(ALL, ALL);
			chequingTxnTypesQueryMap = utilityService.createStandardGUIDisplayMap(chqTxnOrigMap);
			useAllDates = Boolean.TRUE;
			selectedChequingTxnQueryType = ALL;
		}
		System.out.println("hi ian finished ChequingDetailMVC.init()");
	}
	
	public List<ChequingTxn> getTxnList() {
		return this.txnList;
	}
 
	public void setTxnList(List<ChequingTxn> txnList) {
		this.txnList = txnList;
	}
	
	public ChequingTxn getSelectedTxn() {
		return this.selectedTxn;
	}
 
	public void setSelectedTxn(ChequingTxn selectedTxn) {
		this.selectedTxn = selectedTxn;
	}
	
	public Chequing getChequing() {
		return this.chequing;
	}
 
	public void setChequing(Chequing chequing) {
		this.chequing = chequing;
	}
	
	public Map<String,String> getChequingTxnTypesMap() {
		return this.chequingTxnTypesMap;
	}
 
	public void setChequingTxnTypesMap(Map<String,String> chequingTxnTypesMap) {
		this.chequingTxnTypesMap = chequingTxnTypesMap;
	}
	
	public Map<String,String> getChequingTxnTypesQueryMap() {
		return this.chequingTxnTypesQueryMap;
	}
 
	public void setChequingTxnTypesQueryMap(Map<String,String> chequingTxnTypesQueryMap) {
		this.chequingTxnTypesQueryMap = chequingTxnTypesQueryMap;
	}
	
	public String getSelectedChequingTxnType() {
		return this.selectedChequingTxnType;
	}
 
	public void setSelectedChequingTxnType(String selectedChequingTxnType) {
		this.selectedChequingTxnType = selectedChequingTxnType;
	}
	
	public String getSelectedChequingTxnQueryType() {
		return this.selectedChequingTxnQueryType;
	}
 
	public void setSelectedChequingTxnQueryType(String selectedChequingTxnQueryType) {
		this.selectedChequingTxnQueryType = selectedChequingTxnQueryType;
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
	
	public String getOffsetAcctInfo() {
		return this.offsetAcctInfo;
	}
 
	public void setOffsetAcctInfo(String offsetAcctInfo) {
		this.offsetAcctInfo = offsetAcctInfo;
	}
	
	public String getStatusDesc() {
		return this.statusDesc;
	}
 
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	
	public String getDrcrDesc() {
		return this.drcrDesc;
	}
 
	public void setDrcrDesc(String drcrDesc) {
		this.drcrDesc = drcrDesc;
	}
	
	public String getTxntypeDesc() {
		return this.txntypeDesc;
	}
 
	public void setTxntypeDesc(String txntypeDesc) {
		this.txntypeDesc = txntypeDesc;
	}
	
	public String getOffsetTxnType() {
		return this.offsetTxnType;
	}
 
	public void setoffsetTxnType(String offsetTxnType) {
		this.offsetTxnType = offsetTxnType;
	}	
	
	public Boolean getUseAllDates() {
		return this.useAllDates;
	}
 
	public void setUseAllDates(Boolean useAllDates) {
		this.useAllDates = useAllDates;
	}
	
	public Date getTxnDtMin() {
		return this.txnDtMin;
	}
 
	public void setTxnDtMin(Date txnDtMin) {
		this.txnDtMin = txnDtMin;
	}
	
	public Date getTxnDtMax() {
		return this.txnDtMax;
	}
 
	public void setTxnDtMax(Date txnDtMax) {
		this.txnDtMax = txnDtMax;
	}
	
	public String doSelectedDetail() {	
		if (selectedTxn != null) {
			currentScreenInfo.setTxn(selectedTxn);
			currentScreenInfo.setNewTxn(Boolean.FALSE);
			currentScreenInfo.setTxnType(null);
			return "chequingtxn-detail.xhtml?faces-redirect=true";
		}
		else {
			return null;
		}
	}
	
	public void onRowSelect(SelectEvent event) {
		if (selectedTxn != null) {
			String txnType = selectedTxn.getChequingTxnType();
			if (txnType.equals(TRANSFER_IN) || txnType.equals(TRANSFER_OUT) || 
					txnType.equals(OPEN_ACCOUNT))
			{
				transferFlag = Boolean.TRUE;
			}
			else  {
				transferFlag = Boolean.FALSE;
			}
			if (txnType.equals(ISSUE_CHEQUE)) {
				chequeIssueFlag = Boolean.TRUE;
			}
			else {
				chequeIssueFlag = Boolean.FALSE;
			}
			if (selectedTxn.getOffsetAccount() != null){
				offsetAcctInfo = accountService.makeSpecificAccountDisplay(selectedTxn.getOffsetAccount(), Boolean.TRUE);
				Txn offsetTxn = txnDao.findById(selectedTxn.getOffsetTxnId());
				if (offsetTxn instanceof ChequingTxn) {
					offsetTxnType = ((ChequingTxn) offsetTxn).getChequingTxnType();
				}
				else if (offsetTxn instanceof SavingsTxn) {
					offsetTxnType = ((SavingsTxn) offsetTxn).getSavingsTxnType();
				}
				else if (offsetTxn instanceof GICTxn) {
					offsetTxnType = ((GICTxn) offsetTxn).getGicTxnType();
				}
				else if (offsetTxn instanceof StockTxn) {
					offsetTxnType = ((StockTxn) offsetTxn).getStockTxnType();
				}
				else if (offsetTxn instanceof AnnuityTxn) {
					offsetTxnType = ((AnnuityTxn) offsetTxn).getAnnuityTxnType();
				}
				if (offsetTxnType.equals(OPEN_ACCOUNT)) {
					offsetAcctInfo += " [" + OPEN_ACCOUNT_GEN_DESC + "]";
				}
			}
			else  {
				offsetAcctInfo = CASH;
			}
			statusDesc = selectedTxn.getReversed().booleanValue() ? Translations.REVERSED : Translations.ORIGINAL;
			drcrDesc = translations.getDrcr().get(selectedTxn.getDrCr());
			txntypeDesc = translations.getChequingtxntypes().get(selectedTxn.getChequingTxnType());
		}
		
	}
	
	public void reverseChequingTxn(ActionEvent event) {
		txnService.reverse(chequing, selectedTxn);
		chequing = chequingDao.findById(chequing.getAccountId());
		chequing.setBalance(new Double(chequingService.calcBalance(chequing.getChequingTxns())));
		chequingService.updateRunningTotals(chequing);
		retrieveTxnList();
		System.out.println("hi ian 90210");
	}
	
	public String doNewChequingTxnScreen() {
		currentScreenInfo.setTxn(null);
		currentScreenInfo.setNewTxn(Boolean.TRUE);
		currentScreenInfo.setTxnType(selectedChequingTxnType);
		return "chequingtxn-detail.xhtml?faces-redirect=true";
	}
	
	public void retrieveTxnList() {
		
		if (useAllDates.booleanValue()) {
			txnList = chequingTxnDao.findWithinTxnDtRangeByType(chequing, chequing.getOpenDt(), new Date(), selectedChequingTxnQueryType, "ASC");
		}
		else {
			txnList = chequingTxnDao.findWithinTxnDtRangeByType(chequing, txnDtMin, txnDtMax, selectedChequingTxnQueryType, "ASC");
		}
		for (ChequingTxn chequingTxn : txnList ) {
			String display = translations.getChequingtxntypes().get(chequingTxn.getChequingTxnType()) +
				      	((chequingTxn.getReversed()) ? " (Reversed)" : " (Original)");
				         
			chequingTxn.setDisplay(display);
		}
	}

}

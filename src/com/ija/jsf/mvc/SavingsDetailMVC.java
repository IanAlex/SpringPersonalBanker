package com.ija.jsf.mvc;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

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
public class SavingsDetailMVC implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Autowired 
	@Qualifier("savingsDao")
	private SavingsDao savingsDao;
	@Autowired 
	@Qualifier("savingsRateDao")
	private SavingsRateDao savingsRateDao;
	@Autowired 
	@Qualifier("txnDao")
	private TxnDao txnDao;
	@Autowired 
	@Qualifier("savingsTxnDao")
	private SavingsTxnDao savingsTxnDao;
	@Autowired
	CurrentScreenInfo currentScreenInfo;
	@Autowired
	Translations translations;
	@Autowired 
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired 
	@Qualifier("savingsService")
	private SavingsService savingsService;
	@Autowired 
	@Qualifier("txnService")
	private TxnService txnService;
	@Autowired 
	@Qualifier("utilityService")
	private UtilityService utilityService;
	
	private static final String TRANSFER_IN = "TI";
	private static final String TRANSFER_OUT = "TO";
	private static final String OPEN_ACCOUNT = "O";
	private static final String INTEREST = "I";
	private static final String OPEN_ACCOUNT_GEN_DESC = "Open Account";
	private static final String ALL = "ALL";
	
	private static final String CASH = "Cash";
		
	private List<SavingsTxn> txnList;
	private SavingsTxn selectedTxn;
	private Savings savings;
	private Map<String,String> savingsTxnTypesMap;
	private Map<String,String> savingsTxnTypesQueryMap;
	private String selectedSavingsTxnType;
	private String selectedSavingsTxnQueryType;
	private Boolean transferFlag;
	private String offsetAcctInfo;
	private String statusDesc;
	private String drcrDesc;
	private String txntypeDesc;
	private String offsetTxnType;
		
	private Double newSRinterest;
	private Double currentSRinterest;
	private Date newSReffectiveDt;
	private List<SavingsRate> orderedSRList;
	
	private Boolean useAllDates;
	private Date txnDtMin;
	private Date txnDtMax;
	
	
	public SavingsDetailMVC() {
	}

	@PostConstruct
	public void init() {
		if (currentScreenInfo != null) {
			savings = currentScreenInfo.getSavings();
		}
		if (savings != null) {
			savingsService.updateRunningTotals(savings);
			txnList = savingsTxnDao.findWithinTxnDtRangeByType(savings, savings.getOpenDt(), new Date(), ALL, "ASC");
			for (SavingsTxn savingsTxn : txnList ) {
				String display = translations.getSavingstxntypes().get(savingsTxn.getSavingsTxnType()) +
					      	((savingsTxn.getReversed()) ? " (Reversed)" : " (Original)");
					         
				savingsTxn.setDisplay(display);
			}
			orderedSRList = savingsRateDao.findOrderByEffectiveDt(savings);
			currentSRinterest = orderedSRList.get(orderedSRList.size() - 1).getInterestRate();
			Map<String,String> chqSavTOMap = accountService.makeTransferCashChequeSavingsMap(currentScreenInfo.getClient(), currentScreenInfo.getSavings(), Boolean.FALSE, Boolean.TRUE);
			Map<String,String> chqSavTIMap = accountService.makeTransferCashChequeSavingsMap(currentScreenInfo.getClient(), currentScreenInfo.getSavings(), Boolean.FALSE, Boolean.FALSE);	
			Map<String,String> savTxnOrigMap = new HashMap<String,String>();
			savTxnOrigMap.putAll(translations.getSavingstxntypes());
			if (chqSavTOMap.isEmpty()) {
				savTxnOrigMap.remove(TRANSFER_IN);
			}
			if (chqSavTIMap.isEmpty()) {
				savTxnOrigMap.remove(TRANSFER_OUT);
			}
			savTxnOrigMap.remove(OPEN_ACCOUNT);
			savTxnOrigMap.remove(INTEREST);
			savingsTxnTypesMap = utilityService.createStandardGUIDisplayMap(savTxnOrigMap);
			savTxnOrigMap = new TreeMap<String,String>();
			savTxnOrigMap.putAll(translations.getSavingstxntypes());
			savTxnOrigMap.put(ALL, ALL);
			savingsTxnTypesQueryMap = utilityService.createStandardGUIDisplayMap(savTxnOrigMap);
			useAllDates = Boolean.TRUE;
			selectedSavingsTxnQueryType = ALL;
		}
		System.out.println("hi ian finished SavingsDetailMVC.init()");
	}
	
	public List<SavingsTxn> getTxnList() {
		return this.txnList;
	}
 
	public void setTxnList(List<SavingsTxn> txnList) {
		this.txnList = txnList;
	}
	
	public SavingsTxn getselectedTxn() {
		return this.selectedTxn;
	}
 
	public void setselectedTxn(SavingsTxn selectedTxn) {
		this.selectedTxn = selectedTxn;
	}
	
	public Savings getSavings() {
		return this.savings;
	}
 
	public void setSavings(Savings savings) {
		this.savings = savings;
	}
	
	public Map<String,String> getSavingsTxnTypesMap() {
		return this.savingsTxnTypesMap;
	}
 
	public void setSavingsTxnTypesMap(Map<String,String> savingsTxnTypesMap) {
		this.savingsTxnTypesMap = savingsTxnTypesMap;
	}
	
	public Map<String,String> getSavingsTxnTypesQueryMap() {
		return this.savingsTxnTypesQueryMap;
	}
 
	public void setSavingsTxnTypesQueryMap(Map<String,String> savingsTxnTypesQueryMap) {
		this.savingsTxnTypesQueryMap = savingsTxnTypesQueryMap;
	}
	
	public String getSelectedSavingsTxnType() {
		return this.selectedSavingsTxnType;
	}
 
	public void setSelectedSavingsTxnType(String selectedSavingsTxnType) {
		this.selectedSavingsTxnType = selectedSavingsTxnType;
	}
	
	public String getSelectedSavingsTxnQueryType() {
		return this.selectedSavingsTxnQueryType;
	}
 
	public void setSelectedSavingsTxnQueryType(String selectedSavingsTxnQueryType) {
		this.selectedSavingsTxnQueryType = selectedSavingsTxnQueryType;
	}
	
	public Boolean getTransferFlag() {
		return this.transferFlag;
	}
 
	public void setTransferFlag(Boolean transferFlag) {
		this.transferFlag = transferFlag;
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
	
	public Date getNewSReffectiveDt() {
		return this.newSReffectiveDt;
	}
 
	public void setNewSReffectiveDt(Date newSReffectiveDt) {
		this.newSReffectiveDt = newSReffectiveDt;
	}
	
	public Double getNewSRinterest() {
		return this.newSRinterest;
	}
 
	public void setNewSRinterest(Double newSRinterest) {
		this.newSRinterest = newSRinterest;
	}
	
	public Double getCurrentSRinterest() {
		return this.currentSRinterest;
	}
 
	public void setCurrentSRinterest(Double currentSRinterest) {
		this.currentSRinterest = currentSRinterest;
	}
	
	public List<SavingsRate> getOrderedSRList() {
		return this.orderedSRList;
	}
 
	public void setOrderedSRList(List<SavingsRate> orderedSRList) {
		this.orderedSRList = orderedSRList;
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
			return "savingstxn-detail.xhtml?faces-redirect=true";
		}
		else {
			return null;
		}
	}
	
	public void onRowSelect(SelectEvent event) {
		if (selectedTxn != null) {
			String txnType = selectedTxn.getSavingsTxnType();
			if (txnType.equals(TRANSFER_IN) || txnType.equals(TRANSFER_OUT) || 
					txnType.equals(OPEN_ACCOUNT)	)
			{
				transferFlag = Boolean.TRUE;
			}
			else  {
				transferFlag = Boolean.FALSE;
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
			else {
				offsetAcctInfo = CASH;
			}
			statusDesc = selectedTxn.getReversed().booleanValue() ? Translations.REVERSED : Translations.ORIGINAL;
			drcrDesc = translations.getDrcr().get(selectedTxn.getDrCr());
			txntypeDesc = translations.getSavingstxntypes().get(selectedTxn.getSavingsTxnType());
		}
		
	}
	
	public void reverseSavingsTxn(ActionEvent event) {
		txnService.reverse(savings, selectedTxn);
		savings = savingsDao.findById(savings.getAccountId());
		savings.setBalance(new Double(savingsService.calcBalance(savings.getSavingsTxns())));
		savingsService.updateRunningTotals(savings);
		retrieveTxnList();
	}
	
	public String doNewSavingsTxnScreen() {
		currentScreenInfo.setTxn(null);
		currentScreenInfo.setNewTxn(Boolean.TRUE);
		currentScreenInfo.setTxnType(selectedSavingsTxnType);
		return "savingstxn-detail.xhtml?faces-redirect=true";
	}
	
	public void validateSavingsEffDt(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException  
    {
		if (value == null || !(value instanceof Date)) {
			FacesMessage message = new FacesMessage("You must enter a valid date (dd/mm/yy) for Savings Effective Start Date");
			throw new ValidatorException(message);
		}
		Date enteredDt = (Date) value;
		Date sysDt = new Date();
		if (enteredDt.after(sysDt)) {
			FacesMessage message = new FacesMessage("Savings Effective/Start Date cannot come after System Date (today)");
			throw new ValidatorException(message);
		}
		if (enteredDt.before(savings.getOpenDt())) {
			FacesMessage message = new FacesMessage("Savings Effective/Start Date cannot come before Savings Account open date");
			throw new ValidatorException(message);
		}
    }
	
	public void validateSavingsInterest(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException  
    {
		if (value == null || !(value instanceof Double)) {
			FacesMessage message = new FacesMessage("You must enter a valid (numeric) Effective Annual Int. Rate");
			throw new ValidatorException(message);
		}
		Double intRate = (Double) value;
		if (intRate.intValue() < .01 || intRate.intValue() > 100.0) {
			FacesMessage message = new FacesMessage("Effective Annual Int. Rate must be a valid percentage (0.01% to 100%)");
			throw new ValidatorException(message);
		}
		
    }
	
	public String saveNewSavingsRate() {
		boolean updateFlag = false;
		SavingsRate savRate = null;
		Double intRate = new Double(newSRinterest.doubleValue() / 100.0);
		for (SavingsRate sr : savings.getSavingsRates()) {
			//int fac = sr.getEffectiveDt().compareTo(newSReffectiveDt);
			if (sr.getEffectiveDt().compareTo(newSReffectiveDt) == 0) {
				savRate = sr;
				break;
			}
		}
		if (savRate != null) {
			if (savRate.getInterestRate().doubleValue() != intRate.doubleValue()) {
				savRate.setInterestRate(intRate);
				savRate.setDtChanged(new Date());
				savRate.setUpdatedBy("SYSTEM");
				savingsRateDao.store(savRate);
				updateFlag = true;
			}
		}
		else {
			savRate = new SavingsRate();
			savRate.setSavings(savings);
			savRate.setEffectiveDt(newSReffectiveDt);
			savRate.setInterestRate(intRate);
			savRate.setDtChanged(new Date());
			savRate.setDtCreated(new Date());
			savRate.setUpdatedBy("SYSTEM");
			savings.getSavingsRates().add(savRate);
			savingsDao.store(savings);
			updateFlag = true;
		}
		if (updateFlag) {
			orderedSRList = savingsRateDao.findOrderByEffectiveDt(savings);
			currentSRinterest = orderedSRList.get(orderedSRList.size() - 1).getInterestRate();
			savings = savingsDao.findById(savings.getAccountId());
			savings = savingsService.generateInterestTxns(savings, newSReffectiveDt);
			savings.setBalance(new Double(savingsService.calcBalance(savings.getSavingsTxns())));
			savingsService.updateRunningTotals(savings);
			retrieveTxnList();
		}
		newSRinterest = null;
		newSReffectiveDt = null;
		return "account-detail.xhtml?faces-redirect=true";
	}
	
	public String cancelNewSavingsRate() {
		newSRinterest = null;
		newSReffectiveDt = null;
		return "account-detail.xhtml?faces-redirect=true";
	}
	
	public void retrieveTxnList() {
		if (useAllDates.booleanValue()) {
			txnList = savingsTxnDao.findWithinTxnDtRangeByType(savings, savings.getOpenDt(), new Date(), selectedSavingsTxnQueryType, "ASC");
		}
		else {
			txnList = savingsTxnDao.findWithinTxnDtRangeByType(savings, txnDtMin, txnDtMax, selectedSavingsTxnQueryType, "ASC");
		}
		for (SavingsTxn savingsTxn : txnList ) {
			String display = translations.getSavingstxntypes().get(savingsTxn.getSavingsTxnType()) +
				      	((savingsTxn.getReversed()) ? " (Reversed)" : " (Original)");
				         
			savingsTxn.setDisplay(display);
		}
	}

}

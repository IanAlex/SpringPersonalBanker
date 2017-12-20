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

import com.ija.pb.hibernate.dao.*;
import com.ija.pb.hibernate.tables.*;
import com.ija.pb.nondaodata.*;
import com.ija.pb.services.*;

@Component
@ViewScoped
public class AnnuityDetailMVC implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Autowired 
	@Qualifier("gicDao")
	private GICDao gicDao;
	@Autowired 
	@Qualifier("annuityTxnDao")
	private AnnuityTxnDao annuityTxnDao;
	@Autowired
	CurrentScreenInfo currentScreenInfo;
	@Autowired
	Translations translations;
	@Autowired 
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired 
	@Qualifier("annuityService")
	private AnnuityService annuityService;
	@Autowired 
	@Qualifier("utilityService")
	private UtilityService utilityService;
		
	private static final String CASH = "Cash";
	
	private static final String ALL = "ALL";
	
	private List<AnnuityTxn> txnList;
	private AnnuityTxn selectedTxn;
	private Annuity annuity;
	private Double annuityPV;
	private String payAcctDesc;
	private Double interestRatePct;
	private String payFreqDesc;
	private String intCompFreqDesc;
	
	private String offsetAcctInfo;
	private String statusDesc;
	private String drcrDesc;
	private String txntypeDesc;
	
	private Boolean useAllDates;
	private Date txnDtMin;
	private Date txnDtMax;
	
	private Map<String,String> annuityTxnTypesQueryMap;
	private String selectedAnnuityTxnQueryType;
	
	public AnnuityDetailMVC() {
	}
	
	@PostConstruct
	public void init() {
		if (currentScreenInfo != null) {
			annuity = currentScreenInfo.getAnnuity();
		}
		if (annuity != null) {
			annuityService.updateRunningTotals(annuity);
			txnList = annuityTxnDao.findWithinTxnDtRangeByType(annuity, annuity.getOpenDt(), new Date(), ALL, "ASC");
			for (AnnuityTxn annuityTxn : txnList ) {
				String display = translations.getAnnuitytxntypes().get(annuityTxn.getAnnuityTxnType()) +
					      	((annuityTxn.getReversed()) ? " (Reversed)" : " (Original)");
					         
				annuityTxn.setDisplay(display);
			}
			annuityPV = annuityService.calcPV(currentScreenInfo.getAnnuity(), new Date());	
			if (annuity.getPayAccount() != null) {
				payAcctDesc = accountService.makeSpecificAccountDisplay(annuity.getPayAccount(), Boolean.TRUE);
			}
			else {
				payAcctDesc = CASH;
			}
			interestRatePct = new Double(annuity.getInterestRate() * 100.0);
			payFreqDesc = translations.getAnnuitypayfreqcodes().get(annuity.getPayfrequency().toString());
			intCompFreqDesc = translations.getIntcompoundfreqcodes().get(annuity.getIntCompoundfrequency().toString());
			Map<String,String> annuityTxnOrigMap = new TreeMap<String,String>();
			annuityTxnOrigMap.putAll(translations.getAnnuitytxntypes());
			annuityTxnOrigMap.put(ALL, ALL);
			annuityTxnTypesQueryMap = utilityService.createStandardGUIDisplayMap(annuityTxnOrigMap);
			useAllDates = Boolean.TRUE;
			selectedAnnuityTxnQueryType = ALL;
		}
		System.out.println("hi ian XXX finished AnnuityDetailMVC.init()");
	}
	
	public List<AnnuityTxn> getTxnList() {
		return this.txnList;
	}
 
	public void setTxnList(List<AnnuityTxn> txnList) {
		this.txnList = txnList;
	}
	
	public AnnuityTxn getSelectedTxn() {
		return this.selectedTxn;
	}
 
	public void setSelectedTxn(AnnuityTxn selectedTxn) {
		this.selectedTxn = selectedTxn;
	}
	
	public Annuity getAnnuity() {
		return this.annuity;
	}
 
	public void setAnnuity(Annuity annuity) {
		this.annuity = annuity;
	}
	
	public String getPayAcctDesc() {
		return this.payAcctDesc;
	}	
 
	public void setPayAcctDesc(String payAcctDesc) {
		this.payAcctDesc = payAcctDesc;
	}
	
	public String getPayFreqDesc() {
		return this.payFreqDesc;
	}
	
	public void setPayFreqDesc(String payFreqDesc) {
		this.payFreqDesc = payFreqDesc;
	}
	
	public String getIntCompFreqDesc() {
		return this.intCompFreqDesc;
	}
 
	public void setIntCompFreqDesc(String intCompFreqDesc) {
		this.intCompFreqDesc = intCompFreqDesc;
	}
	
	public Double getAnnuityPV() {
		return this.annuityPV;
	}
 
	public void setAnnuityPV(Double annuityPV) {
		this.annuityPV = annuityPV;
	}
	
	public Double getInterestRatePct() {
		return this.interestRatePct;
	}
 
	public void setInterestRatePct(Double interestRatePct) {
		this.interestRatePct = interestRatePct;
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
	
	public Map<String,String> getAnnuityTxnTypesQueryMap() {
		return this.annuityTxnTypesQueryMap;
	}
 
	public void setAnnuityTxnTypesQueryMap(Map<String,String> annuityTxnTypesQueryMap) {
		this.annuityTxnTypesQueryMap = annuityTxnTypesQueryMap;
	}
	
	public String getSelectedAnnuityTxnQueryType() {
		return this.selectedAnnuityTxnQueryType;
	}
 
	public void setSelectedAnnuityTxnQueryType(String selectedAnnuityTxnQueryType) {
		this.selectedAnnuityTxnQueryType = selectedAnnuityTxnQueryType;
	}
	
	public void onRowSelect(SelectEvent event) {
		if (selectedTxn != null) {
			if (selectedTxn.getOffsetAccount() != null){
				offsetAcctInfo = accountService.makeSpecificAccountDisplay(selectedTxn.getOffsetAccount(), Boolean.TRUE);
			}
			else {
				offsetAcctInfo = CASH;
			}
			statusDesc = selectedTxn.getReversed().booleanValue() ? Translations.REVERSED : Translations.ORIGINAL;
			drcrDesc = translations.getDrcr().get(selectedTxn.getDrCr());
			txntypeDesc = translations.getAnnuitytxntypes().get(selectedTxn.getAnnuityTxnType());
		}
		
	}
	
	public String doSelectedDetail() {	
		if (selectedTxn != null) {
			currentScreenInfo.setTxn(selectedTxn);
			currentScreenInfo.setNewTxn(Boolean.FALSE);
			currentScreenInfo.setTxnType(null);
			return "annuitytxn-detail.xhtml?faces-redirect=true";
		}
		else {
			return null;
		}
	}
	
	public void retrieveTxnList() {
		if (useAllDates.booleanValue()) {
			txnList = annuityTxnDao.findWithinTxnDtRangeByType(annuity, annuity.getOpenDt(), new Date(), selectedAnnuityTxnQueryType, "ASC");
		}
		else {
			txnList = annuityTxnDao.findWithinTxnDtRangeByType(annuity, txnDtMin, txnDtMax, selectedAnnuityTxnQueryType, "ASC");
		}
		for (AnnuityTxn annuityTxn : txnList ) {
			String display = translations.getAnnuitytxntypes().get(annuityTxn.getAnnuityTxnType()) +
				      	((annuityTxn.getReversed()) ? " (Reversed)" : " (Original)");
				         
			annuityTxn.setDisplay(display);
		}
	}

}

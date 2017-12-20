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
public class GICDetailMVC implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Autowired 
	@Qualifier("gicDao")
	private GICDao gicDao;
	@Autowired 
	@Qualifier("gicTxnDao")
	private GICTxnDao gicTxnDao;
	@Autowired
	CurrentScreenInfo currentScreenInfo;
	@Autowired
	Translations translations;
	@Autowired 
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired 
	@Qualifier("gicService")
	private GICService gicService;
	@Autowired 
	@Qualifier("txnService")
	private TxnService txnService;
	@Autowired 
	@Qualifier("utilityService")
	private UtilityService utilityService;
	
	private static final String FUNDS_OUT = "W";
	private static final String OPEN_ACCOUNT = "O";
	private static final String MATURITY = "M";
	private static final String ALL = "ALL";
	
	private static final String CASH = "Cash";
		
	private List<GICTxn> txnList;
	private GICTxn selectedTxn;
	private GIC gic;
	private Map<String,String> gicTxnTypesMap;
	private Map<String,String> gicTxnTypesQueryMap;
	private String selectedGICTxnType;
	private String selectedGICTxnQueryType;
	private Double gicMaturityValue;
	private String payAcctDesc;
	private Double interestRatePct;
	private String intCompFreqDesc;
	private String offsetAcctInfo;
	private String statusDesc;
	private String drcrDesc;
	private String txntypeDesc;
	
	private Boolean useAllDates;
	private Date txnDtMin;
	private Date txnDtMax;
	
	public GICDetailMVC() {
	}
	
	@PostConstruct
	public void init() {
		if (currentScreenInfo != null) {
			gic = currentScreenInfo.getGic();
		}
		if (gic != null) {
			gicService.updateRunningTotals(gic);
			txnList = gicTxnDao.findWithinTxnDtRangeByType(gic, gic.getOpenDt(), new Date(), ALL, "ASC");
			for (GICTxn gicTxn : txnList ) {
				String display = translations.getGictxntypes().get(gicTxn.getGicTxnType()) +
					      	((gicTxn.getReversed()) ? " (Reversed)" : " (Original)");
					         
				gicTxn.setDisplay(display);
			}
			Map<String,String> gicTxnOrigMap = new HashMap<String,String>();
			gicTxnOrigMap.putAll(translations.getGictxntypes());
			gicTxnOrigMap.remove(OPEN_ACCOUNT);
			gicTxnOrigMap.remove(MATURITY);
			gicTxnTypesMap = utilityService.createStandardGUIDisplayMap(gicTxnOrigMap);
			gicTxnOrigMap = new TreeMap<String,String>();
			gicTxnOrigMap.putAll(translations.getGictxntypes());
			gicTxnOrigMap.put(ALL, ALL);
			gicTxnTypesQueryMap = utilityService.createStandardGUIDisplayMap(gicTxnOrigMap);
			gicMaturityValue = gicService.calcMaturityValue(currentScreenInfo.getGic());	
			if (gic.getPayAccount() != null) {
				payAcctDesc = accountService.makeSpecificAccountDisplay(gic.getPayAccount(), Boolean.TRUE);
			}
			else {
				payAcctDesc = CASH;
			}
			interestRatePct = new Double(gic.getInterestRate() * 100.0);
			intCompFreqDesc = translations.getIntcompoundfreqcodes().get(gic.getIntCompoundfrequency().toString());
			useAllDates = Boolean.TRUE;
			selectedGICTxnQueryType = ALL;
		}
		System.out.println("hi ian finished GICDetailMVC.init()");
	}
	
	public List<GICTxn> getTxnList() {
		return this.txnList;
	}
 
	public void setTxnList(List<GICTxn> txnList) {
		this.txnList = txnList;
	}
	
	public GICTxn getSelectedTxn() {
		return this.selectedTxn;
	}
 
	public void setSelectedTxn(GICTxn selectedTxn) {
		this.selectedTxn = selectedTxn;
	}
	
	public GIC getGic() {
		return this.gic;
	}
 
	public void setGic(GIC gic) {
		this.gic = gic;
	}
	
	public Map<String,String> getGicTxnTypesMap() {
		return this.gicTxnTypesMap;
	}
 
	public void setGicTxnTypesMap(Map<String,String> gicTxnTypesMap) {
		this.gicTxnTypesMap = gicTxnTypesMap;
	}
	
	public Map<String,String> getGicTxnTypesQueryMap() {
		return this.gicTxnTypesQueryMap;
	}
 
	public void setGicTxnTypesQueryMap(Map<String,String> gicTxnTypesQueryMap) {
		this.gicTxnTypesQueryMap = gicTxnTypesQueryMap;
	}
	
	public String getSelectedGICTxnType() {
		return this.selectedGICTxnType;
	}
 
	public void setSelectedGICTxnType(String selectedGICTxnType) {
		this.selectedGICTxnType = selectedGICTxnType;
	}
	
	public String getSelectedGICTxnQueryType() {
		return this.selectedGICTxnQueryType;
	}
 
	public void setSelectedGICTxnQueryType(String selectedGICTxnQueryType) {
		this.selectedGICTxnQueryType = selectedGICTxnQueryType;
	}
	
	public String getPayAcctDesc() {
		return this.payAcctDesc;
	}
 
	public void setPayAcctDesc(String payAcctDesc) {
		this.payAcctDesc = payAcctDesc;
	}
	
	public Double getGicMaturityValue() {
		return this.gicMaturityValue;
	}
 
	public void setGicMaturityValue(Double gicMaturityValue) {
		this.gicMaturityValue = gicMaturityValue;
	}
	
	public Double getInterestRatePct() {
		return this.interestRatePct;
	}
 
	public void setInterestRatePct(Double interestRatePct) {
		this.interestRatePct = interestRatePct;
	}
	
	public String getIntCompFreqDesc() {
		return this.intCompFreqDesc;
	}
 
	public void setIntCompFreqDesc(String intCompFreqDesc) {
		this.intCompFreqDesc = intCompFreqDesc;
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
	
	public String doSelectedDetail() {	
		if (selectedTxn != null) {
			currentScreenInfo.setTxn(selectedTxn);
			currentScreenInfo.setNewTxn(Boolean.FALSE);
			currentScreenInfo.setTxnType(null);
			return "gictxn-detail.xhtml?faces-redirect=true";
		}
		else {
			return null;
		}
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
			txntypeDesc = translations.getGictxntypes().get(selectedTxn.getGicTxnType());
		}
		
	}
	
	public void reverseGicTxn(ActionEvent event) {
		txnService.reverse(gic, selectedTxn);
		gic = gicDao.findById(gic.getAccountId());
		gic.setBalance(new Double(gicService.calcBalance(gic.getGicTxns())));
		gicService.updateRunningTotals(gic);
		retrieveTxnList();
	}
	
	public String doNewGICTxnScreen() {
		currentScreenInfo.setTxn(null);
		currentScreenInfo.setNewTxn(Boolean.TRUE);
		currentScreenInfo.setTxnType(selectedGICTxnType);
		return "gictxn-detail.xhtml?faces-redirect=true";
	}
	
	public void retrieveTxnList() {
		if (useAllDates.booleanValue()) {
			txnList = gicTxnDao.findWithinTxnDtRangeByType(gic, gic.getOpenDt(), new Date(), selectedGICTxnQueryType, "ASC");
		}
		else {
			txnList = gicTxnDao.findWithinTxnDtRangeByType(gic, txnDtMin, txnDtMax, selectedGICTxnQueryType, "ASC");
		}
		for (GICTxn gicTxn : txnList ) {
			String display = translations.getGictxntypes().get(gicTxn.getGicTxnType()) +
				      	((gicTxn.getReversed()) ? " (Reversed)" : " (Original)");
				         
			gicTxn.setDisplay(display);
		}
	}

}

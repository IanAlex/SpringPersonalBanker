package com.ija.jsf.mvc;

import java.util.*;
import java.text.SimpleDateFormat;


import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.faces.component.UIComponent;
import org.primefaces.context.RequestContext;

import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ija.pb.hibernate.tables.*;
import com.ija.pb.hibernate.dao.*;
import com.ija.pb.nondaodata.*;
import com.ija.pb.services.*;

import com.ija.stockserv.*;

@Component
@ManagedBean
@ViewScoped
public class AccountSetUpMVC implements Serializable { 
	
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
	@Qualifier("annuityDao")
	private AnnuityDao annuityDao;
	@Autowired 
	@Qualifier("gicDao")
	private GICDao gicDao;
	@Autowired 
	@Qualifier("stockDao")
	private StockDao stockDao;
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
	@Qualifier("annuityService")
	private AnnuityService annuityService;
	@Autowired 
	@Qualifier("gicService")
	private GICService gicService;
	@Autowired 
	@Qualifier("stockService")
	private StockService stockService;
	@Autowired
	private StockWSEndpointImpl stockWebServ;
	@Autowired 
	@Qualifier("utilityService")
	private UtilityService utilityService;
	@Autowired
	private TabNavigator tabNavigator;
	
	private static final String STOCK = "ST";
	private static final String GIC = "GC";
	private static final String ANNUITY = "AN";
	private static final String CHEQUING = "CH";
	private static final String SAVINGS = "SV";
	
	private static final String CASH = "Cash";
	
	private static final String OPEN_ACCOUNT = "O";
	private static final String TRANSFER_OUT = "TO";
	
	private static final int PROCESS_GOOD = -10;
	
	private Map<String,String> acctTypesMap;
	private Map<String,String> annuityPayFreqCodesMap;
	private Map<String,String> annuityTermMonthsMap;
	private Map<String,String> annuityTermYearsMap;
	private Map<String,String> annuityIntCompFreqCodesMap;
	private Map<String,String> gicIntCompFreqCodesMap;
	private String oldselectedNewAcctType;
	private String selectedNewAcctType;
	
	private String stockSymbol;
	private boolean annuityTermMonthsChosen;
	private boolean annuityTermYearsChosen;
	private String selectedAnnuityTermMonthsCode;
	private String selectedAnnuityTermYearsCode;
	private Double annuityPeriodicPayment;
	private String selectedAnnuityPayFreqCode;
	private String selectedAnnuityIntCompFreqCode;
	private String selectedGicIntCompFreqCode;
	private Double annuityInterestRate;
	private Date gicMaturityDt;
	private Boolean gicCashable;
	private Double gicInterestRate;
	private Double gicMaturityValue;
	private Double savingsRateInterest;
	
	private Double txnAmount;
	private Integer stockTxnUnits;
	private Double stockTxnPrice;
	private Double stockTxnFee;
	
	private Map<String,String> transferAccountMap;
	private Map<String,String> annuityPayAccountMap;
	private Map<String,String> gicPayAccountMap;
	private String selectedTransferAcctInfo;
	private String selectedAnnuityPayAcctInfo;
	private String selectedGicPayAcctInfo;
	
	private String annuityPayFrequency;
	private String annuityIntCompFrequency;
	private String gicIntCompFrequency;
	private Account transferAcct;
	private String transferAcctDesc;
	private Account annuityPayAcct;
	private String annuityPayAcctDesc;
	private Account gicPayAcct;
	private String gicPayAcctDesc;
	
	private StockInfo stockInfo;
	private Boolean useTicker;
	private Double manualStockPrice;
	private String tmpMsgA;
	private String tmpMsgB;
	
	private Boolean renderTxnHeadErr;
	private Date tmpStockOpenDt;
	private boolean dispStockOpenAmt;
	
	
	public AccountSetUpMVC() {		
	}
	
	@PostConstruct
	public void init() {
		acctTypesMap = utilityService.createStandardGUIDisplayMap(translations.getAccounttypes());
		annuityPayFreqCodesMap = utilityService.createStandardGUIDisplayMap(translations.getAnnuitypayfreqcodes());
		annuityIntCompFreqCodesMap = utilityService.createStandardGUIDisplayMap(translations.getIntcompoundfreqcodes());
		gicIntCompFreqCodesMap = utilityService.createStandardGUIDisplayMap(translations.getIntcompoundfreqcodes());
		generateAnnuityTermMonthsMap();
		generateAnnuityTermYearsMap();
		oldselectedNewAcctType = "";
		if (currentScreenInfo != null) {
			clearfields();
		}
	}
	
	public Map<String,String> getAcctTypesMap() {
		return this.acctTypesMap;
	}
 
	public void setAcctTypesMap(Map<String,String> acctTypesMap) {
		this.acctTypesMap = acctTypesMap;
	}
	
	public Map<String,String> getAnnuityPayFreqCodesMap() {
		
		if (selectedAnnuityTermMonthsCode == null && selectedAnnuityTermYearsCode == null) {
			annuityPayFreqCodesMap = utilityService.createStandardGUIDisplayMap(translations.getAnnuitypayfreqcodes());
		}
		else {
			int termMonths;
			if (annuityTermMonthsChosen) {
				termMonths = Integer.parseInt(selectedAnnuityTermMonthsCode);
			}
			else {
				termMonths = Integer.parseInt(selectedAnnuityTermYearsCode);
			}
			annuityPayFreqCodesMap = new LinkedHashMap<String,String>();
			for (Map.Entry<String,String> mapEntry : translations.getAnnuitypayfreqcodes().entrySet()) {
				int payIntervalMths = 12 / Integer.parseInt(mapEntry.getKey());
				if (termMonths >= payIntervalMths && ((termMonths % payIntervalMths) == 0) ) {
					annuityPayFreqCodesMap.put(mapEntry.getValue(),  mapEntry.getKey()); 
				}
			}
		}
		return this.annuityPayFreqCodesMap;
	}
 
	public void setAnnuityPayFreqCodesMap(Map<String,String> annuityPayFreqCodesMap) {
		this.annuityPayFreqCodesMap = annuityPayFreqCodesMap;
	}
	
	public Map<String,String> getAnnuityTermMonthsMap() {
		return this.annuityTermMonthsMap;
	}
 
	public void setAnnuityTermMonthsMap(Map<String,String> annuityTermMonthsMap) {
		this.annuityTermMonthsMap = annuityTermMonthsMap;
	}
	
	public Map<String,String> getAnnuityTermYearsMap() {
		return this.annuityTermYearsMap;
	}
 
	public void setAnnuityTermYearsMap(Map<String,String> annuityTermYearsMap) {
		this.annuityTermYearsMap = annuityTermYearsMap;
	}
	
	public Map<String,String> getAnnuityIntCompFreqCodesMap() {
		return this.annuityIntCompFreqCodesMap;
	}
 
	public void setAnnuityIntCompFreqCodesMap(Map<String,String> annuityIntCompFreqCodesMap) {
		this.annuityIntCompFreqCodesMap = annuityIntCompFreqCodesMap;
	}
	
	public Map<String,String> getGicIntCompFreqCodesMap() {
		return this.gicIntCompFreqCodesMap;
	}
 
	public void setGicIntCompFreqCodesMap(Map<String,String> gicIntCompFreqCodesMap) {
		this.gicIntCompFreqCodesMap = gicIntCompFreqCodesMap;
	}
	
	public String getSelectedNewAcctType() {
		return this.selectedNewAcctType;
	}
 
	public void setSelectedNewAcctType(String selectedNewAcctType) {
		this.selectedNewAcctType = selectedNewAcctType;
	}
	
	public String getSelectedAnnuityPayFreqCode() {
		return this.selectedAnnuityPayFreqCode;
	}
 
	public void setSelectedAnnuityPayFreqCode(String selectedAnnuityPayFreqCode) {
		this.selectedAnnuityPayFreqCode = selectedAnnuityPayFreqCode;
	}	
	
	
	public String getSelectedAnnuityTermMonthsCode() {
		return this.selectedAnnuityTermMonthsCode;
	}
 
	public void setSelectedAnnuityTermMonthsCode(String selectedAnnuityTermMonthsCode) {
		this.selectedAnnuityTermMonthsCode = selectedAnnuityTermMonthsCode;
	}
	
	public String getSelectedAnnuityTermYearsCode() {
		return this.selectedAnnuityTermYearsCode;
	}
 
	public void setSelectedAnnuityTermYearsCode(String selectedAnnuityTermYearsCode) {
		this.selectedAnnuityTermYearsCode = selectedAnnuityTermYearsCode;
	}
	
	public String getSelectedAnnuityIntCompFreqCode() {
		return this.selectedAnnuityIntCompFreqCode;
	}
 
	public void setSelectedAnnuityIntCompFreqCode(String selectedAnnuityIntCompFreqCode) {
		this.selectedAnnuityIntCompFreqCode = selectedAnnuityIntCompFreqCode;
	}
	
	public String getSelectedGicIntCompFreqCode() {
		return this.selectedGicIntCompFreqCode;
	}
 
	public void setSelectedGicIntCompFreqCode(String selectedGicIntCompFreqCode) {
		this.selectedGicIntCompFreqCode = selectedGicIntCompFreqCode;
	}
	
	public CurrentScreenInfo getcurrentScreenInfo() {
		return this.currentScreenInfo;
	}
 
	public void setcurrentScreenInfo(CurrentScreenInfo currentScreenInfo) {
		this.currentScreenInfo = currentScreenInfo;
	}
	
	public String getStockSymbol() {
		return this.stockSymbol;
	}
 
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}	
		
	public boolean getAnnuityTermMonthsChosen() {
		return this.annuityTermMonthsChosen;
	}	
 
	public void setAnnuityTermMonthsChosen(boolean annuityTermMonthsChosen) {
		this.annuityTermMonthsChosen = annuityTermMonthsChosen;
	}
	
	public Boolean getAnnuityTermYearsChosen() {
		return this.annuityTermYearsChosen;
	}	
 
	public void setAnnuityTermYearsChosen(Boolean annuityTermYearsChosen) {
		this.annuityTermYearsChosen = annuityTermYearsChosen;
	}
	
	public Double getAnnuityPeriodicPayment() {
		return this.annuityPeriodicPayment;
	}
 
	public void setAnnuityPeriodicPayment(Double annuityPeriodicPayment) {
		this.annuityPeriodicPayment = annuityPeriodicPayment;
	}
			
	public Double getAnnuityInterestRate() {
		return this.annuityInterestRate;
	}
 
	public void setAnnuityInterestRate(Double annuityInterestRate) {
		this.annuityInterestRate = annuityInterestRate;
	}
	
	public Date getGicMaturityDt() {
		return this.gicMaturityDt;
	}
 
	public void setGicMaturityDt(Date gicMaturityDt) {
		this.gicMaturityDt = gicMaturityDt;
	}
	
	public Boolean getGicCashable() {
		return this.gicCashable;
	}
 
	public void setGicCashable(Boolean gicCashable) {
		this.gicCashable = gicCashable;
	}
	
	public Double getGicInterestRate() {
		return this.gicInterestRate;
	}
 
	public void setGicInterestRate(Double gicInterestRate) {
		this.gicInterestRate = gicInterestRate;
	}
	
	public Double getGicMaturityValue() {
		return this.gicMaturityValue;
	}
 
	public void setGicMaturityValue(Double gicMaturityValue) {
		this.gicMaturityValue = gicMaturityValue;
	}
	
	public Double getSavingsRateInterest() {
		return this.savingsRateInterest;
	}
 
	public void setSavingsRateInterest(Double savingsRateInterest) {
		this.savingsRateInterest = savingsRateInterest;
	}
	
	public Double getTxnAmount() {
		return this.txnAmount;
	}
 
	public void setTxnAmount(Double txnAmount) {
		this.txnAmount = txnAmount;
	}
	
	public Integer getStockTxnUnits() {
		return this.stockTxnUnits;
	}
 
	public void setStockTxnUnits(Integer stockTxnUnits) {
		this.stockTxnUnits = stockTxnUnits;
	}
	
	public Double getStockTxnPrice() {
		return this.stockTxnPrice;
	}
 
	public void setStockTxnPrice(Double stockTxnPrice) {
		this.stockTxnPrice = stockTxnPrice;
	}
	
	public Double getStockTxnFee() {
		return this.stockTxnFee;
	}
 
	public void setStockTxnFee(Double stockTxnFee) {
		this.stockTxnFee = stockTxnFee;
	}
	
	public Map<String,String> getTransferAccountMap() {
		return this.transferAccountMap;
	}
 
	public void setTransferAccountMap(Map<String,String> transferAccountMap) {
		this.transferAccountMap = transferAccountMap;
	}	
	
	public Map<String,String> getAnnuityPayAccountMap() {
		return this.annuityPayAccountMap;
	}
 
	public void setAnnuityPayAccountMap(Map<String,String> annuityPayAccountMap) {
		this.annuityPayAccountMap = annuityPayAccountMap;
	}	
	
	public Map<String,String> getGicPayAccountMap() {
		return this.gicPayAccountMap;
	}
 
	public void setGicPayAccountMap(Map<String,String> gicPayAccountMap) {
		this.gicPayAccountMap = gicPayAccountMap;
	}	
	
	public String getSelectedTransferAcctInfo() {
		return this.selectedTransferAcctInfo;
	}
 
	public void setSelectedTransferAcctInfo(String selectedTransferAcctInfo) {
		this.selectedTransferAcctInfo = selectedTransferAcctInfo;
	}
	
	public String getSelectedAnnuityPayAcctInfo() {
		return this.selectedAnnuityPayAcctInfo;
	}
 
	public void setSelectedAnnuityPayAcctInfo(String selectedAnnuityPayAcctInfo) {
		this.selectedAnnuityPayAcctInfo = selectedAnnuityPayAcctInfo;
	}
	
	public String getSelectedGicPayAcctInfo() {
		return this.selectedGicPayAcctInfo;
	}
 
	public void setSelectedGicPayAcctInfo(String selectedGicPayAcctInfo) {
		this.selectedGicPayAcctInfo = selectedGicPayAcctInfo;
	}
	
	public String getAnnuityPayFrequency() {
		return this.annuityPayFrequency;
	}
 
	public void setAnnuityPayFrequency(String annuityPayFrequency) {
		this.annuityPayFrequency = annuityPayFrequency;
	}
	
	public String getAnnuityIntCompFrequency() {
		return this.annuityIntCompFrequency;
	}
 
	public void setAnnuityIntCompFrequency(String annuityIntCompFrequency) {
		this.annuityIntCompFrequency = annuityIntCompFrequency;
	}
	
	public String getGicIntCompFrequency() {
		return this.gicIntCompFrequency;
	}
 
	public void setGicIntCompFrequency(String gicIntCompFrequency) {
		this.gicIntCompFrequency = gicIntCompFrequency;
	}
	
	public Account getTransferAcct() {
		return this.transferAcct;
	}
 
	public void setTransferAcct(Account transferAcct) {
		this.transferAcct = transferAcct;
	}
	
	public String getTransferAcctDesc() {
		return this.transferAcctDesc;
	}
 
	public void setTransferAcctDesc(String transferAcctDesc) {
		this.transferAcctDesc = transferAcctDesc;
	}
	
	public Account getAnnuityPayAcct() {
		return this.annuityPayAcct;
	}
 
	public void setAnnuityPayAcct(Account annuityPayAcct) {
		this.annuityPayAcct = annuityPayAcct;
	}
	
	public String getAnnuityPayAcctDesc() {
		return this.annuityPayAcctDesc;
	}
 
	public void setAnnuityPayAcctDesc(String annuityPayAcctDesc) {
		this.annuityPayAcctDesc = annuityPayAcctDesc;
	}
	
	public Account getGicPayAcct() {
		return this.gicPayAcct;
	}
 
	public void setGicPayAcct(Account gicPayAcct) {
		this.gicPayAcct = gicPayAcct;
	}
	
	public String getGicPayAcctDesc() {
		return this.gicPayAcctDesc;
	}
 
	public void setGicPayAcctDesc(String gicPayAcctDesc) {
		this.gicPayAcctDesc = gicPayAcctDesc;
	}
	
	public StockInfo getStockInfo() {
		return this.stockInfo;
	}
	
	public Boolean getUseTicker() {
		return this.useTicker;
	}
 
	public void setUseTicker(Boolean useTicker) {
		this.useTicker = useTicker;
	}
	
	public Double getManualStockPrice() {
		return this.manualStockPrice;
	}
 
	public void setManualStockPrice(Double manualStockPrice) {
		this.manualStockPrice = manualStockPrice;
	}
	
	public String getTmpMsgA() {
		return this.tmpMsgA;
	}
 
	public void setTmpMsgA(String tmpMsgA) {
		this.tmpMsgA = tmpMsgA;
	}
	
	public String gettmpMsgB() {
		return this.tmpMsgB;
	}
 
	public void settmpMsgB(String tmpMsgB) {
		this.tmpMsgB = tmpMsgB;
	}
	
	public Boolean getRenderTxnHeadErr() {
		return this.renderTxnHeadErr;
	}	
 
	public void setRenderTxnHeadErr(Boolean renderTxnHeadErr) {
		this.renderTxnHeadErr = renderTxnHeadErr;
	}
	
	public boolean getDispStockOpenAmt() {
		return this.dispStockOpenAmt;
	}
	
	public void generateAnnuityTermMonthsMap() {
		//generate for term of month to 2400 months (100 years)
		Map<String,String> prelimMap = new LinkedHashMap<String,String>();
		for (int i=1; i<=2400; i++) {
			if (i>=12 && (i % 3) == 0 ) {
				if ((i % 12) == 0) {
					prelimMap.put(String.valueOf(i) + " (" + String.valueOf(i/12) + " years)", String.valueOf(i) );
				}
				else {
					prelimMap.put(String.valueOf(i) + " (" + utilityService.round(new Double((i + 0.0)/12.0), 2).toString() + " years)", String.valueOf(i) );
				}
			}
			else {
				prelimMap.put(String.valueOf(i), String.valueOf(i));
			}
		}
		annuityTermMonthsMap = prelimMap;
	}
	
	public void generateAnnuityTermYearsMap() {
		//generate for 1 to 100 years
		Map<String,String> prelimMap = new LinkedHashMap<String,String>();
		for (int i=1; i<=100; i++) {
			prelimMap.put(String.valueOf(i), String.valueOf(i * 12));
		}
		annuityTermYearsMap = prelimMap;
	}
		
	public void clearfields() {
		currentScreenInfo.setWizAnnuity(null);
		currentScreenInfo.setWizChequing(null);
		currentScreenInfo.setWizGic(null);
		currentScreenInfo.setWizSavings(null);
		currentScreenInfo.setWizStock(null);
		currentScreenInfo.setNewAccount(null);
		currentScreenInfo.setTxn(null);
		selectedAnnuityPayFreqCode=null;
		selectedAnnuityTermMonthsCode="1";
		selectedAnnuityTermYearsCode="12";
		selectedAnnuityIntCompFreqCode=null;
		selectedGicIntCompFreqCode=null;
		stockSymbol=null;
		annuityTermMonthsChosen=false;
		annuityTermYearsChosen=true;
		annuityPeriodicPayment=null;
		annuityInterestRate=null;
		gicMaturityDt=null;
		gicCashable=Boolean.FALSE;
		gicInterestRate=null;
		gicMaturityValue=null;
		savingsRateInterest=null;
		txnAmount=null;
		stockTxnUnits=null;
		stockTxnPrice=null;
		stockTxnFee=null;
		selectedTransferAcctInfo=null;
		selectedAnnuityPayAcctInfo=null;
		selectedGicPayAcctInfo=null;
		annuityPayFrequency=null;
		annuityIntCompFrequency=null;
		gicIntCompFrequency=null;
		transferAcct=null;
		transferAcctDesc=null;
		annuityPayAcct=null;
		annuityPayAcctDesc=null;
		gicPayAcct=null;
		gicPayAcctDesc=null;
		stockInfo=null;
		useTicker=Boolean.TRUE;
		manualStockPrice=null;
		tmpMsgA=null;
		tmpMsgB=null;
	}
	
	public String onFlowProcess(FlowEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean isError = false;
		String currentWizScreen = event.getOldStep();
		String nextWizScreen = event.getNewStep();
		System.out.println("current wizard screen: " + currentWizScreen );
		System.out.println("next wizard screen: " + nextWizScreen );
		if (nextWizScreen.equals("chooseActType")) {
			//currentScreenInfo.setinWizAcctValidation(Boolean.FALSE);
			//do Nothing			
		}
		/*
		else if (nextWizScreen.equals("enterAcctInfo")) {
			currentScreenInfo.setinWizAcctValidation(Boolean.TRUE);
		}
		else if (nextWizScreen.equals("enterTxnInfo")) {
			currentScreenInfo.setinWizAcctValidation(Boolean.TRUE);
		}
		else if (nextWizScreen.equals("newAcctConfirm")) {
			currentScreenInfo.setinWizAcctValidation(Boolean.FALSE);
		}
		*/
		else if (nextWizScreen.equals("enterAcctInfo") && currentWizScreen.equals("chooseActType")) {	
			renderTxnHeadErr = Boolean.FALSE;
			if (!selectedNewAcctType.equals(oldselectedNewAcctType)) {
				clearfields();
				transferAccountMap = accountService.makeTransferCashChequeSavingsMap(currentScreenInfo.getClient(), null, Boolean.TRUE, Boolean.TRUE);
				if (selectedNewAcctType.equals(CHEQUING)) {
					Chequing chequing = new Chequing();
					chequing.setAccountType(CHEQUING);
					chequing.setClient(currentScreenInfo.getClient());
					currentScreenInfo.setWizChequing(chequing);
					currentScreenInfo.setNewAccount(chequing);
				}
				if (selectedNewAcctType.equals(SAVINGS)) {
					Savings savings = new Savings();
					savings.setAccountType(SAVINGS);
					savings.setClient(currentScreenInfo.getClient());
					currentScreenInfo.setWizSavings(savings);
					currentScreenInfo.setNewAccount(savings);
				}
				if (selectedNewAcctType.equals(GIC)) {
					GIC gic = new GIC();
					gic.setAccountType(GIC);
					gic.setClient(currentScreenInfo.getClient());
					currentScreenInfo.setWizGic(gic);
					currentScreenInfo.setNewAccount(gic);
					gicPayAccountMap = accountService.makeTransferCashChequeSavingsMap(currentScreenInfo.getClient(), null, Boolean.TRUE, Boolean.FALSE);
				}
				if (selectedNewAcctType.equals(ANNUITY)) {
					Annuity annuity = new Annuity();
					annuity.setAccountType(ANNUITY);
					annuity.setClient(currentScreenInfo.getClient());
					currentScreenInfo.setWizAnnuity(annuity);
					currentScreenInfo.setNewAccount(annuity);
					annuityPayAccountMap = accountService.makeTransferCashChequeSavingsMap(currentScreenInfo.getClient(), null, Boolean.TRUE, Boolean.FALSE);
				}
				if (selectedNewAcctType.equals(STOCK)) {
					Stock stock = new Stock();
					stock.setAccountType(STOCK);
					stock.setClient(currentScreenInfo.getClient());
					currentScreenInfo.setWizStock(stock);
					currentScreenInfo.setNewAccount(stock);
				}
				oldselectedNewAcctType = selectedNewAcctType;
			}
		}
		else if (nextWizScreen.equals("enterTxnInfo") && currentWizScreen.equals("enterAcctInfo")) {
			renderTxnHeadErr = Boolean.FALSE;
			dispStockOpenAmt = false;
			if (currentScreenInfo.isthereWizAnnuity()) {
					Annuity annuity = new Annuity();
					annuity.setOpenDt(currentScreenInfo.getNewAccount().getOpenDt());
					annuity.setPeriodicPayment(annuityPeriodicPayment);
					annuity.setPayfrequency(Integer.parseInt(selectedAnnuityPayFreqCode));
					annuity.setInterestRate(new Double(annuityInterestRate.doubleValue()/100.0));
					annuity.setIntCompoundfrequency(Integer.parseInt(selectedAnnuityIntCompFreqCode));
					if (annuityTermMonthsChosen) {
						annuity.setTermMonths(Integer.parseInt(selectedAnnuityTermMonthsCode));
					}
					else {
						annuity.setTermMonths(Integer.parseInt(selectedAnnuityTermYearsCode));
					}
					txnAmount = new Double(annuityService.calcPV(annuity, annuity.getOpenDt()));
			}
		}
		else if (nextWizScreen.equals("newAcctConfirm") && currentWizScreen.equals("enterTxnInfo")) {
			renderTxnHeadErr = Boolean.FALSE;
			dispStockOpenAmt = false;
			if (currentScreenInfo.getNewAccount() instanceof Annuity) {
				annuityPayFrequency = translations.getAnnuitypayfreqcodes().get(selectedAnnuityPayFreqCode);
				annuityIntCompFrequency = translations.getIntcompoundfreqcodes().get(selectedAnnuityIntCompFreqCode);
			}
			else if (currentScreenInfo.getNewAccount() instanceof GIC) { 
				gicIntCompFrequency = translations.getIntcompoundfreqcodes().get(selectedGicIntCompFreqCode);
				GIC gic = new GIC();
				gic.setOpenDt(currentScreenInfo.getNewAccount().getOpenDt());
				gic.setMaturityDt(gicMaturityDt);
				gic.setInterestRate(new Double(gicInterestRate.doubleValue()/100.0));
				gic.setIntCompoundfrequency(Integer.parseInt(selectedGicIntCompFreqCode));
				gicMaturityValue = gicService.calcMaturityValue(gic, txnAmount);
			}
			else if (currentScreenInfo.getNewAccount() instanceof Stock) {
				stockTxnFee = new Double((stockTxnFee != null) ? stockTxnFee.doubleValue() : 0.0);
				// Use ticker price for Txn if this was checked off
				if (useTicker) {
					stockTxnPrice = stockInfo.getLatestPrice();
				}
				else {
					stockTxnPrice = manualStockPrice;
				}
				txnAmount = utilityService.round(new Double((stockTxnUnits.doubleValue() * stockTxnPrice.doubleValue()) + stockTxnFee.doubleValue()), 2);
			}
			if (selectedTransferAcctInfo.equals(CASH)) {
				transferAcctDesc = CASH;
			}
			else {
				String msgStr = "";
				StringTokenizer st = new StringTokenizer(selectedTransferAcctInfo, "|");
				Integer acctId = Integer.parseInt(st.nextToken());
				transferAcct = accountDao.findById(acctId);
				transferAcctDesc = accountService.makeSpecificAccountDisplay(transferAcct, Boolean.TRUE);
				double availBalance = 0;
				Date transferOpenDt = new Date();
				if (transferAcct instanceof Chequing) {
					Chequing chequing = (Chequing) transferAcct;
					availBalance = chequingService.calcBalance(chequing.getChequingTxns());
					transferOpenDt = chequing.getOpenDt();
				}
				else if (transferAcct instanceof Savings) {
					Savings savings = (Savings) transferAcct;
					availBalance = savingsService.calcBalance(savings.getSavingsTxns());
					transferOpenDt = savings.getOpenDt();
				}
				System.out.println("here we are");
				if (txnAmount.doubleValue() > availBalance) {
					if (currentScreenInfo.getNewAccount() instanceof Stock) {
						dispStockOpenAmt = true;
						msgStr = "Insufficient transfer funds to open new Stock Account " +
					              "[(units * price) + fee]";
					}
					else {
						msgStr = "Insufficient transfer funds to cover opening amount in New Account";
					}
					context.addMessage(null, new FacesMessage(msgStr));
				}
				if (currentScreenInfo.getNewAccount().getOpenDt().before(transferOpenDt)) {
					msgStr = "New Acct Open Date cannot be prior to transfer acct open (" +
							new SimpleDateFormat("dd-MMM-yyyy").format(transferOpenDt) + ")";
					context.addMessage(null, new FacesMessage(msgStr));			
				}
				if (context.getMessageList() != null && !context.getMessageList().isEmpty()) {
					renderTxnHeadErr = Boolean.TRUE;
					return currentWizScreen;
				}
			}
			if (selectedNewAcctType.equals(ANNUITY)) {
				if (selectedAnnuityPayAcctInfo.equals(CASH)) {
					annuityPayAcctDesc = CASH;
				}
				else {
					StringTokenizer st = new StringTokenizer(selectedAnnuityPayAcctInfo, "|");
					Integer acctId = Integer.parseInt(st.nextToken());
					annuityPayAcct = accountDao.findById(acctId);
					annuityPayAcctDesc = accountService.makeSpecificAccountDisplay(annuityPayAcct, Boolean.TRUE);					
				}
				
			}
			if (selectedNewAcctType.equals(GIC)) {
				if (selectedGicPayAcctInfo.equals(CASH)) {
					gicPayAcctDesc = CASH;
				}
				else {
					StringTokenizer st = new StringTokenizer(selectedGicPayAcctInfo, "|");
					Integer acctId = Integer.parseInt(st.nextToken());
					gicPayAcct = accountDao.findById(acctId);
					gicPayAcctDesc = accountService.makeSpecificAccountDisplay(gicPayAcct, Boolean.TRUE);					
				}
				
			}
		}
		else if (nextWizScreen.equals("enterTxnInfo") && currentWizScreen.equals("newAcctConfirm")) {
			dispStockOpenAmt = true;
		}
		return nextWizScreen;
	}
	
	public void validateAccountNo(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException 
    {
		String accountNo = (String) value;
		if (accountNo.trim().length() > 20) {
			FacesMessage message = new FacesMessage("Account No. cannot exceed 20 characters");
			throw new ValidatorException(message);
		}
		List<Account> existingAccounts = accountDao.findForClient(currentScreenInfo.getClient());
		for (Account tacct : existingAccounts) {
			if(accountNo.trim().equals(tacct.getAccountNo())) {
				FacesMessage message = new FacesMessage("This Account No. is used by an existing/existed Account");
				throw new ValidatorException(message);
			}
		}		
    }
	
	public void validateAccountName(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException 
    {
		String accountName = (String) value;
		if (accountName.trim().length() > 25) {
			FacesMessage message = new FacesMessage("Account Name cannot exceed 25 characters");
			throw new ValidatorException(message);
		}
		List<Account> existingAccounts = accountDao.findForClient(currentScreenInfo.getClient());
		for (Account tacct : existingAccounts) {
			if(accountName.trim().equals(tacct.getAccountName())) {
				FacesMessage message = new FacesMessage("This Account Name is used by an existing/existed Account");
				throw new ValidatorException(message);
			}
		}		
    }
	
	public void validateOpenDt(FacesContext context, UIComponent componentToValidate, Object value)
	                       throws ValidatorException 
	{
		Date enteredDt = (Date) value;
		Date sysDt = new Date();
		tmpStockOpenDt = null;
		if (enteredDt.after(sysDt)) {
			FacesMessage message = new FacesMessage("Open Date cannot come after System Date (today)");
			throw new ValidatorException(message);
		}
		else {
			tmpStockOpenDt = enteredDt;
		}
	}
	
	public void validateStockSymbol(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException 
    {
		String stockSymbol = (String) value;
		Set<Stock> existingStockAccounts = stockDao.findForClient(currentScreenInfo.getClient(), Boolean.FALSE);
		for (Stock tstock : existingStockAccounts) {
			if(stockSymbol.trim().equals(tstock.getSymbol())) {
				FacesMessage message = new FacesMessage("An account exists/existed for this stock symbol");
				throw new ValidatorException(message);
			}
		}
		if (tmpStockOpenDt != null) {
			stockInfo = stockWebServ.getWebServiceStockQuote(stockSymbol, tmpStockOpenDt);
			if (stockInfo == null || stockInfo.getLatestPrice() == null) {
				FacesMessage message = new FacesMessage("Stock with symbol entered did not trade on Open Date entered");
				throw new ValidatorException(message);
			}
		}
		tmpStockOpenDt = null;
    }
	
	public void validateGICMaturityDt(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException  
    {
		Date enteredDt = (Date) value;
		Date sysDt = new Date();
		if (!enteredDt.after(sysDt)) {
			FacesMessage message = new FacesMessage("GIC Maturity Date must come after System Date (today)");
			throw new ValidatorException(message);
		}
    }
	
	public void onClickTermMonths() {
		annuityTermYearsChosen = !annuityTermMonthsChosen;
	}
	
	public void onClickTermYears() {
		annuityTermMonthsChosen = !annuityTermYearsChosen;
	}
	
	
	public String createNewAccount() {
		currentScreenInfo.getNewAccount().setOpen(Boolean.TRUE);
		currentScreenInfo.getNewAccount().setDtCreated(new Date());
		currentScreenInfo.getNewAccount().setDtChanged(new Date());
		currentScreenInfo.getNewAccount().setUpdatedBy("SYSTEM");
		if (currentScreenInfo.getNewAccount() instanceof Chequing) {
			Chequing chequing = (Chequing) currentScreenInfo.getNewAccount();
			chequingDao.store(chequing);
			ChequingTxn chequingTxn = chequingService.createTxn(txnAmount, OPEN_ACCOUNT, 
					null, chequing, chequing.getOpenDt(), null, transferAcct);
			if (chequingTxn != null) {
				if (transferAcct != null) {
					Txn offsetTxn = null;
					if (transferAcct instanceof Chequing) {
						offsetTxn = chequingService.createTxn(txnAmount, TRANSFER_OUT, null, 
								(Chequing) transferAcct, chequing.getOpenDt(), 
								 chequingTxn.getTxnId(), chequing);
					}
					else if (transferAcct instanceof Savings) {
						offsetTxn = savingsService.createTxn(txnAmount, TRANSFER_OUT,
								(Savings) transferAcct, chequing.getOpenDt(), 
								chequingTxn.getTxnId(), chequing);
					}
					if (offsetTxn != null) {
						chequingTxn.setOffsetTxnId(offsetTxn.getTxnId());
					}
				}
				chequing.getChequingTxns().add(chequingTxn);
				chequingDao.store(chequing);
				currentScreenInfo.getClient().getAccounts().add(chequing);				
			}
		}
		else if (currentScreenInfo.getNewAccount() instanceof Savings) {
			Savings savings = (Savings) currentScreenInfo.getNewAccount();
			savingsDao.store(savings);
			SavingsRate savingsRate = new SavingsRate();
			savingsRate.setInterestRate(new Double(savingsRateInterest.doubleValue()/100.0));
			savingsRate.setEffectiveDt(savings.getOpenDt());
			savingsRate.setDtCreated(savings.getDtCreated());
			savingsRate.setDtChanged(savings.getDtChanged());
			savingsRate.setUpdatedBy(savings.getUpdatedBy());
			savingsRate.setSavings(savings);
			savings.getSavingsRates().add(savingsRate);
			savingsDao.store(savings);
			SavingsTxn savingsTxn = savingsService.createTxn(txnAmount, OPEN_ACCOUNT, 
							savings, savings.getOpenDt(), null, transferAcct);
			if (savingsTxn != null) {
				if (transferAcct != null) {
					Txn offsetTxn = null;
					if (transferAcct instanceof Chequing) {
						offsetTxn = chequingService.createTxn(txnAmount, TRANSFER_OUT, null, 
								(Chequing) transferAcct, savings.getOpenDt(), 
								 savingsTxn.getTxnId(), savings);
					}
					else if (transferAcct instanceof Savings) {
						offsetTxn = savingsService.createTxn(txnAmount, TRANSFER_OUT,
								(Savings) transferAcct, savings.getOpenDt(), 
								savingsTxn.getTxnId(), savings);
					}
					if (offsetTxn != null) {
						savingsTxn.setOffsetTxnId(offsetTxn.getTxnId());
					}
				}
				savings.getSavingsTxns().add(savingsTxn);
				savingsDao.store(savings);
				currentScreenInfo.getClient().getAccounts().add(savings);
			}			
		}
		else if (currentScreenInfo.getNewAccount() instanceof Annuity) {
			Annuity annuity = (Annuity) currentScreenInfo.getNewAccount();
			annuity.setOpenDt(currentScreenInfo.getNewAccount().getOpenDt());
			annuity.setPeriodicPayment(annuityPeriodicPayment);
			annuity.setPayfrequency(Integer.parseInt(selectedAnnuityPayFreqCode));
			annuity.setInterestRate(new Double(annuityInterestRate.doubleValue()/100.0));
			annuity.setIntCompoundfrequency(Integer.parseInt(selectedAnnuityIntCompFreqCode));
			if (annuityTermMonthsChosen) {
				annuity.setTermMonths(Integer.parseInt(selectedAnnuityTermMonthsCode));
			}
			else {
				annuity.setTermMonths(Integer.parseInt(selectedAnnuityTermYearsCode));
			}
			annuity.setPayAccount(annuityPayAcct);
			annuityDao.store(annuity);
			AnnuityTxn annuityTxn = annuityService.createTxn(txnAmount, OPEN_ACCOUNT, 
								annuity, annuity.getOpenDt(), null, transferAcct);
			if (annuityTxn != null) {
				if (transferAcct != null) {
					Txn offsetTxn = null;
					if (transferAcct instanceof Chequing) {
						offsetTxn = chequingService.createTxn(txnAmount, TRANSFER_OUT, null, 
								(Chequing) transferAcct, annuity.getOpenDt(), 
								 annuityTxn.getTxnId(), annuity);
					}
					else if (transferAcct instanceof Savings) {
						offsetTxn = savingsService.createTxn(txnAmount, TRANSFER_OUT,
								(Savings) transferAcct, annuity.getOpenDt(), 
								annuityTxn.getTxnId(), annuity);
					}
					if (offsetTxn != null) {
						annuityTxn.setOffsetTxnId(offsetTxn.getTxnId());
					}
				}
				annuity.getAnnuityTxns().add(annuityTxn);
				annuityDao.store(annuity);
				//create payment transactions between open date and system date (today)
				annuityService.updateForAnnuityPayments(annuity, new Date());
				currentScreenInfo.getClient().getAccounts().add(annuity);
			}
		}
		else if (currentScreenInfo.getNewAccount() instanceof GIC) {
			GIC gic = (GIC) currentScreenInfo.getNewAccount();
			gic.setMaturityDt(gicMaturityDt);
			gic.setCashable(gicCashable);
			gic.setInterestRate(new Double(gicInterestRate.doubleValue()/100.0));
			gic.setIntCompoundfrequency(Integer.parseInt(selectedGicIntCompFreqCode));
			gic.setPayAccount(gicPayAcct);
			gicDao.store(gic);
			GICTxn gicTxn = gicService.createTxn(txnAmount, OPEN_ACCOUNT, 
								gic, gic.getOpenDt(), null, transferAcct);
			if (gicTxn != null) {
				if (transferAcct != null) {
					Txn offsetTxn = null;
					if (transferAcct instanceof Chequing) {
						offsetTxn = chequingService.createTxn(txnAmount, TRANSFER_OUT, null, 
								(Chequing) transferAcct, gic.getOpenDt(), 
								 gicTxn.getTxnId(), gic);
					}
					else if (transferAcct instanceof Savings) {
						offsetTxn = savingsService.createTxn(txnAmount, TRANSFER_OUT,
								(Savings) transferAcct, gic.getOpenDt(), 
								gicTxn.getTxnId(), gic);
					}
					if (offsetTxn != null) {
						gicTxn.setOffsetTxnId(offsetTxn.getTxnId());
					}
				}
				gic.getGicTxns().add(gicTxn);
				gicDao.store(gic);
				currentScreenInfo.getClient().getAccounts().add(gic);
			}
		}
		else if (currentScreenInfo.getNewAccount() instanceof Stock) {
			Stock stock = (Stock) currentScreenInfo.getNewAccount();
			stock.setSymbol(stockSymbol);
			stockDao.store(stock);
			StockTxn stockTxn = stockService.createTxn(stockTxnPrice, stockTxnUnits, stockTxnFee, null, null,
									OPEN_ACCOUNT, stock, stock.getOpenDt(), null, transferAcct);
			if (stockTxn != null) {
				if (transferAcct != null) {
					Txn offsetTxn = null;
					if (transferAcct instanceof Chequing) {
						offsetTxn = chequingService.createTxn(txnAmount, TRANSFER_OUT, null, 
								(Chequing) transferAcct, stock.getOpenDt(), 
								 stockTxn.getTxnId(), stock);
					}
					else if (transferAcct instanceof Savings) {
						offsetTxn = savingsService.createTxn(txnAmount, TRANSFER_OUT,
								(Savings) transferAcct, stock.getOpenDt(), 
								stockTxn.getTxnId(), stock);
					}
					if (offsetTxn != null) {
						stockTxn.setOffsetTxnId(offsetTxn.getTxnId());
					}
				}
				stock.getStockTxns().add(stockTxn);
				stockDao.store(stock);
				currentScreenInfo.getClient().getAccounts().add(stock);
			}
		}
		clientDao.store(currentScreenInfo.getClient());
		//currentScreenInfo.setClient(clientDao.findById(currentScreenInfo.getClient().getClientId()));
		//portfolioMVC.init();
		tabNavigator.setActiveTabIndex(PROCESS_GOOD);
		return "main.xhtml?faces-redirect=true";
		
	}
	
	public String quitNewAccount() {
		tabNavigator.setActiveTabIndex(-1);
		return "main.xhtml?faces-redirect=true";
	}
	

}

package com.ija.jsf.mvc;

import java.util.*;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import javax.faces.bean.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.primefaces.event.SelectEvent;

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

import com.ija.stockserv.*;

@Component
@ViewScoped
public class StockTxnDetailMVC implements Serializable {
	
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
	@Qualifier("stockService")
	private StockService stockService;
	@Autowired 
	@Qualifier("txnService")
	private TxnService txnService;
	@Autowired 
	@Qualifier("accountDao")
	private AccountDao accountDao;	
	@Autowired 
	@Qualifier("stockDao")
	private StockDao stockDao;
	@Autowired 
	@Qualifier("stockTxnDao")
	private StockTxnDao stockTxnDao;
	@Autowired
	private StockWSEndpointImpl stockWebServ;
	@Autowired
	StockDetailMVC stockDetailMVC;
	@Autowired 
	@Qualifier("utilityService")
	private UtilityService utilityService;
	
	private static final String TRANSFER_IN = "TI";
	private static final String TRANSFER_OUT = "TO";
	private static final String BUY = "B";
	private static final String SELL = "S";
	private static final String CASH = "Cash";
	private static final String GOOD = "Good";
	private static final String BAD = "Bad";
	
	
	String txnDesc1, txnDesc2;
	private Map<String,String> transferAccountMap;
	private String selectedTransferAcctInfo;
	private String offsetAcctInfo;
	private StockTxn viewStockTxn;
	
	private StockInfo stockInfo;
	private Boolean useTicker;
	private boolean valUnitsError;
	
	public StockTxnDetailMVC() {
	}
		
	public void init() {
		if(currentScreenInfo != null) {
			String txnType = "";
			// not used - there's now a dialog for existing Txn
			if (currentScreenInfo.getTxn() != null  && currentScreenInfo.getTxn() instanceof StockTxn) {
				txnType = ((StockTxn)currentScreenInfo.getTxn()).getStockTxnType();
				//txnDesc2 = translations.getStocktxntypes().get(txnType) + "  Transaction Detail";
				viewStockTxn = (StockTxn)currentScreenInfo.getTxn();
				if (viewStockTxn.getOffsetAccount() != null){
					offsetAcctInfo = accountService.makeSpecificAccountDisplay(viewStockTxn.getOffsetAccount(), Boolean.TRUE);
				}
				else {
					offsetAcctInfo = CASH;
				}
			}
			//
			else if (currentScreenInfo.getNewTxn()) {
				txnType = currentScreenInfo.getTxnType();
				//txnDesc2 = " Create New " + translations.getStocktxntypes().get(txnType) + "  Transaction Detail";
				viewStockTxn = new StockTxn();
			}
			transferAccountMap = accountService.makeTransferCashChequeSavingsMap(currentScreenInfo.getClient(), null, Boolean.TRUE, Boolean.FALSE);
			for(Map.Entry<String, String> entry : transferAccountMap.entrySet()) {
				selectedTransferAcctInfo = entry.getValue();
				break;
			}
			useTicker = Boolean.TRUE;
			stockInfo = new StockInfo();
			txnDesc2 = "Create new " + translations.getStocktxntypes().get(txnType) + " transaction";
			txnDesc1 = "Stock Account No.: " + currentScreenInfo.getStock().getAccountNo();
		}
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
	
	public StockTxn getViewStockTxn() {
		return this.viewStockTxn;
	}
 
	public void setViewStockTxn(StockTxn viewStockTxn) {
		this.viewStockTxn = viewStockTxn;
	}
		
	public CurrentScreenInfo getcurrentScreenInfo() {
		return this.currentScreenInfo;
	}
 
	public void setcurrentScreenInfo(CurrentScreenInfo currentScreenInfo) {
		this.currentScreenInfo = currentScreenInfo;
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
	
	public void validateUnits(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException  
    {
		Integer units = (Integer) value;
		if (units.intValue() <= 0) {
			valUnitsError = true;
			FacesMessage message = new FacesMessage("Must enter at least 1 share");
			throw new ValidatorException(message);
		}
		if (currentScreenInfo.getTxnType().equals(SELL)) {
			int availStockUnits = stockService.calcNoShares(currentScreenInfo.getStock().getStockTxns());
			if (units.intValue() > availStockUnits) {
				valUnitsError = true;
				String msgStr = translations.getStocktxntypes().get(currentScreenInfo.getTxnType()) + 
						" shares cannot be greater than the " + availStockUnits +
						" shares in this Stock account";
				FacesMessage message = new FacesMessage(msgStr);
				throw new ValidatorException(message);
			}
		}
    }


	
	public void doTransactionDtChange(SelectEvent e) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(viewStockTxn.getTxnDt());
		System.out.println("hi ian MONTH = " + cal.get(Calendar.MONTH) + 
				                   " DAY_OF_MONTH = " + cal.get(Calendar.DAY_OF_MONTH) +
				                   " YEAR = " + cal.get(Calendar.YEAR));
		stockInfo = stockWebServ.getWebServiceStockQuote(currentScreenInfo.getStock().getSymbol(), viewStockTxn.getTxnDt());		
		System.out.println("hi ian 1977-1983");
	}
	
	public void postJSFStockValidation(ComponentSystemEvent event) {
		FacesContext fc = FacesContext.getCurrentInstance();	
		boolean dtErrFlag = false;
		boolean notTradedFlag = false;
		System.out.println("hi ian 800A");
		UIComponent components = event.getComponent();
		UIInput uiUnits = (UIInput) components.findComponent("units");
		Integer units = (Integer) uiUnits.getLocalValue();
		String unitsId = uiUnits.getClientId();
		UIInput uiFee = (UIInput) components.findComponent("txnStockFee");
		Double fee = (Double) uiFee.getLocalValue();
		System.out.println("hi ian 810 units = " + units);
		UIInput uiPrice = (UIInput) components.findComponent("txnStockPrice");
		Double price = (Double) uiPrice.getLocalValue();
		UIInput uiTxnDt = (UIInput) components.findComponent("txnDt");
		Date txnDt = (Date) uiTxnDt.getLocalValue();
		String txnDtId = uiTxnDt.getClientId();
		System.out.println("hi ian 820 txnDt = " + txnDt);
		// validation of amount/txnDt against transfer account
		if (units != null && txnDt != null && fee != null && !valUnitsError && 
				(useTicker || price != null) ) 
		{
			// validate txnDt without reference to transfers
			if (txnDt.before(currentScreenInfo.getStock().getOpenDt())) {
				FacesMessage msg = new FacesMessage("Transaction Date cannot come before Stock Account Open Date (" +
						(new SimpleDateFormat("dd-MMM-yyyy")).format(currentScreenInfo.getStock().getOpenDt()) + ")");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(txnDtId, msg);
				fc.renderResponse();
				dtErrFlag = true;
			}
			else if (txnDt.after(new Date())) {
				FacesMessage msg = new FacesMessage("Transaction Date cannot come after System Date (today)");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(txnDtId, msg);
				fc.renderResponse();
				dtErrFlag = true;
			}
			else {
				Stock stock = currentScreenInfo.getStock();
				List<StockTxn> txnList = stockTxnDao.findWithinTxnDtRangeByType(stock, stock.getOpenDt(), new Date(), SELL, "DESC");
				if (txnList != null && !txnList.isEmpty()) {
					boolean unreversedFlag = false;
					for (int i=0; i<txnList.size(); i++) {
						if (unreversedFlag) {
							break;
						}
						if (txnList.get(i).getReversed().booleanValue()) {
							continue;
						}
						else {
							unreversedFlag = true;
						}
						if (!txnDt.after(txnList.get(i).getTxnDt())) {
							FacesMessage msg = new FacesMessage("Transaction Date must be after latest unreversed Stock Account Sell Date (" +
									(new SimpleDateFormat("dd-MMM-yyyy")).format(txnList.get(i).getTxnDt()) + ")");
							msg.setSeverity(FacesMessage.SEVERITY_ERROR);
							fc.addMessage(txnDtId, msg);
							fc.renderResponse();
							dtErrFlag = true;
						}
					}
				}
			}
			if (stockInfo == null || stockInfo.getLatestPrice() == null) {
				if (!dtErrFlag) {
					FacesMessage msg = new FacesMessage("This stock was/is not traded on the transaction date entered");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					fc.addMessage(txnDtId, msg);
					fc.renderResponse();
					dtErrFlag = true;
				}
				notTradedFlag = true;
			}	
			//Now work with account transfer
			UIInput uiTransferInfo = (UIInput) components.findComponent("transferAcctInfo");
			String transferInfo = (String) uiTransferInfo.getLocalValue();
			System.out.println("hi ian 830 transferInfo = " + transferInfo);
			if (!transferInfo.equals(CASH)) {
				StringTokenizer st = new StringTokenizer(transferInfo, "|");
				Integer acctId = Integer.parseInt(st.nextToken());
				String acctType = st.nextToken();
				Account offsetAcct = accountDao.findById(acctId);
				if (offsetAcct != null) {
					// test the txn date vs. transfer account if there isnt already a txnDt error
					if (!dtErrFlag && txnDt.before(offsetAcct.getOpenDt())) {
						FacesMessage msg = new FacesMessage("Transaction Date cannot be before Transfer Account open date (" +
								(new SimpleDateFormat("dd-MMM-yyyy")).format(offsetAcct.getOpenDt()) + ")");
						msg.setSeverity(FacesMessage.SEVERITY_ERROR);
						fc.addMessage(txnDtId, msg);
						fc.renderResponse();
					}
					if (currentScreenInfo.getTxnType().equals(BUY) && !notTradedFlag) {
						double availOffsetBalance = 0;
						if (offsetAcct instanceof Chequing) {
							availOffsetBalance = chequingService.calcBalance(((Chequing) offsetAcct).getChequingTxns());
						}
						else if (offsetAcct instanceof Savings) {
							availOffsetBalance = savingsService.calcBalance(((Savings) offsetAcct).getSavingsTxns());
						}
						double tPrice = (useTicker ? stockInfo.getLatestPrice() : price);
						double buyAmount = (units.doubleValue() * tPrice) + fee.doubleValue();
						if (buyAmount > availOffsetBalance) {
							String msgStr = translations.getStocktxntypes().get(currentScreenInfo.getTxnType()) + 
									   " amount cannot be greater than the $" +
									   String.format("%10.2f", availOffsetBalance) +
									   " in the Transfer account";
							FacesMessage msg = new FacesMessage(msgStr);
							msg.setSeverity(FacesMessage.SEVERITY_ERROR);
							fc.addMessage(unitsId, msg);
							fc.renderResponse();
						}
					}
				}	
			}
		}
		valUnitsError = false;
	}
	
	public String processNewStockTxn() {
		System.out.println("hi ian 910");
		StockTxn stockTxn;
		Stock stock = currentScreenInfo.getStock();
		Account offsetAcct = null;		
		Txn offsetTxn = null;
		// Use ticker price for Txn if this was checked off
		if (useTicker) {
			viewStockTxn.setPrice(stockInfo.getLatestPrice());
		}
		if (!selectedTransferAcctInfo.equals(CASH)) {
			StringTokenizer st = new StringTokenizer(selectedTransferAcctInfo, "|");
			Integer acctId = Integer.parseInt(st.nextToken());
			String acctType = st.nextToken();
			offsetAcct = accountDao.findById(acctId);
		}
		double capitalGain = 0;
		double feeNum = ((viewStockTxn.getFee() != null) ? viewStockTxn.getFee() : new Double(0)).doubleValue();
		if (currentScreenInfo.getTxnType().equals(SELL)) {
			capitalGain = stockService.calcCapitalGain(viewStockTxn.getPrice(), viewStockTxn.getUnits(),
													stockService.calcNoShares(stock.getStockTxns()), 
													new Double(feeNum),stockService.calcAverageCostBase(stock, viewStockTxn.getTxnDt()));
		}
		
		stockTxn = stockService.createTxn(viewStockTxn.getPrice(), viewStockTxn.getUnits(), viewStockTxn.getFee(),									 
									((currentScreenInfo.getTxnType().equals(SELL)) ? capitalGain : null), 
									 stockService.calcAverageCostBase(stock, viewStockTxn.getTxnDt()),
									 currentScreenInfo.getTxnType(), stock,
									 viewStockTxn.getTxnDt(), null, offsetAcct);
		if (stockTxn != null) {
			if (offsetAcct != null) {
				System.out.println("there's an offset acct");
				if (offsetAcct instanceof Chequing) {
					System.out.println("offset is chequing");
					offsetTxn = chequingService.createTxn( new Double((viewStockTxn.getUnits().doubleValue() * viewStockTxn.getPrice().doubleValue()) + feeNum * (currentScreenInfo.getTxnType().equals(BUY) ? 1 : -1)),
												(currentScreenInfo.getTxnType().equals(BUY) ? TRANSFER_OUT : TRANSFER_IN),
												null, (Chequing) offsetAcct, viewStockTxn.getTxnDt(),
												stockTxn.getTxnId(), stock);
				}
				else if (offsetAcct instanceof Savings) {
					System.out.println("offset is savings");
					//SavingsTxn createTxn(Double amount, String savingsTxnType, Savings savings, 
							//Date txnDt, Integer offsetTxnId, Account offsetAccount)
					offsetTxn = savingsService.createTxn( new Double((viewStockTxn.getUnits().doubleValue() * viewStockTxn.getPrice().doubleValue()) + feeNum * (currentScreenInfo.getTxnType().equals(BUY) ? 1 : -1)),
												(currentScreenInfo.getTxnType().equals(BUY) ? TRANSFER_OUT : TRANSFER_IN),
												(Savings) offsetAcct, viewStockTxn.getTxnDt(), 
												stockTxn.getTxnId(), stock);
				}			
			}
			if (offsetTxn != null) {
				stockTxn.setOffsetTxnId(offsetTxn.getTxnId());
			}
			stock.getStockTxns().add(stockTxn);
			stockDao.store(stock);
			currentScreenInfo.setNewTxn(Boolean.FALSE);
			currentScreenInfo.setTxnType(null);
			if (stockDetailMVC != null) {
				stockDetailMVC.setNumShares(new Integer(stockService.calcNoShares(stock.getStockTxns())));
				stockDetailMVC.setAverageCostBase(utilityService.round(stockService.calcAverageCostBase(stock, new Date()), 2).toString());
				stockDetailMVC.setMarketValue(utilityService.round(stockService.calcNoShares(stock.getStockTxns()) * Double.parseDouble(stockDetailMVC.getCurSharePrice()), 2).toString());
				stockService.updateRunningUnits(stock);
				stockDetailMVC.retrieveTxnList();
			}
		}
		return "account-detail.xhtml?faces-redirect=true";
	}
	
	public String cancel() {
		currentScreenInfo.setNewTxn(Boolean.FALSE);
		return "account-detail.xhtml?faces-redirect=true";
	}	
	

}

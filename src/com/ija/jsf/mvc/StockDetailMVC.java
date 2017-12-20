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

import com.ija.stockserv.*;

@Component
@ViewScoped
public class StockDetailMVC implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Autowired 
	@Qualifier("stockDao")
	private StockDao stockDao;
	@Autowired 
	@Qualifier("stockTxnDao")
	private StockTxnDao stockTxnDao;
	@Autowired
	CurrentScreenInfo currentScreenInfo;
	@Autowired
	Translations translations;
	@Autowired 
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired 
	@Qualifier("stockService")
	private StockService stockService;
	@Autowired 
	@Qualifier("txnService")
	private TxnService txnService;
	@Autowired 
	@Qualifier("utilityService")
	private UtilityService utilityService;
	@Autowired
	private StockWSEndpointImpl stockWebServ;
	@Autowired 
	private StockTxnDetailMVC stockTxnDetMVC;
	
	private static final String BUY = "B";
	private static final String SELL = "S";
	private static final String OPEN_ACCOUNT = "O";
	private static final String ALL = "ALL";
	
	private static final String CASH = "Cash";
	
	private static final Integer DATE_RANGE = 2;
	
	private List<StockTxn> txnList;
	private StockTxn selectedTxn;
	private Stock stock;
	private Map<String,String> stockTxnTypesMap;
	private Map<String,String> stockTxnTypesQueryMap;
	private String selectedStockTxnType;
	private String selectedStockTxnQueryType;
	private Integer numShares;
	private String averageCostBase;
	private String marketValue;
	private String curSharePrice;
	
	private String offsetAcctInfo;
	private String statusDesc;
	private String drcrDesc;
	private String txntypeDesc;
	
	private Boolean useAllDates;
	private Boolean selTxnAfterLatestSell;
	private Date txnDtMin;
	private Date txnDtMax;
	
	private StockInfo stockInfo;
	
		
	public StockDetailMVC() {
	}
	
	@PostConstruct
	public void init() {
		if (currentScreenInfo != null) {
			stock = currentScreenInfo.getStock();
		}
		if (stock != null) {
			stockService.updateRunningUnits(stock);
			txnList = stockTxnDao.findWithinTxnDtRangeByType(stock, stock.getOpenDt(), new Date(), ALL, "ASC");
			for (StockTxn stockTxn : txnList ) {
				String display = translations.getStocktxntypes().get(stockTxn.getStockTxnType()) +
					      	((stockTxn.getReversed()) ? " (Reversed)" : " (Original)");
					         
				stockTxn.setDisplay(display);
			}
			Map<String,String> stockTxnOrigMap = new HashMap<String,String>();
			stockTxnOrigMap.putAll(translations.getStocktxntypes());
			stockTxnOrigMap.remove(OPEN_ACCOUNT);
			stockTxnTypesMap = utilityService.createStandardGUIDisplayMap(stockTxnOrigMap);
			stockTxnOrigMap = new TreeMap<String,String>();
			stockTxnOrigMap.putAll(translations.getStocktxntypes());
			stockTxnOrigMap.put(ALL, ALL);
			stockTxnTypesQueryMap = utilityService.createStandardGUIDisplayMap(stockTxnOrigMap);
			numShares = new Integer(stockService.calcNoShares(stock.getStockTxns()));
			stockInfo = stockWebServ.getWebServiceStockQuote(stock.getSymbol(), new Date());
			curSharePrice = utilityService.round(stockInfo.getLatestPrice(), 2).toString();
			averageCostBase = utilityService.round(new Double(stockService.calcAverageCostBase(stock, new Date())), 2).toString();
			marketValue = utilityService.round(new Double(numShares.doubleValue() * utilityService.round(stockInfo.getLatestPrice(), 2).doubleValue()), 2).toString();
			useAllDates = Boolean.TRUE;
			selectedStockTxnQueryType = ALL;
		}
		System.out.println("hi ian finished StockDetailMVC.init()");
	}
	
	public List<StockTxn> getTxnList() {
		return this.txnList;
	}
 
	public void setTxnList(List<StockTxn> txnList) {
		this.txnList = txnList;
	}
	
	public StockTxn getSelectedTxn() {
		return this.selectedTxn;
	}
 
	public void setSelectedTxn(StockTxn selectedTxn) {
		this.selectedTxn = selectedTxn;
	}
	
	public Stock getStock() {
		return this.stock;
	}
 
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	
	public Map<String,String> getStockTxnTypesMap() {
		return this.stockTxnTypesMap;
	}
 
	public void setStockTxnTypesMap(Map<String,String> stockTxnTypesMap) {
		this.stockTxnTypesMap = stockTxnTypesMap;
	}
	
	public Map<String,String> getStockTxnTypesQueryMap() {
		return this.stockTxnTypesQueryMap;
	}
 
	public void setStockTxnTypesQueryMap(Map<String,String> stockTxnTypesQueryMap) {
		this.stockTxnTypesQueryMap = stockTxnTypesQueryMap;
	}
	
	public String getSelectedStockTxnType() {
		return this.selectedStockTxnType;
	}
 
	public void setSelectedStockTxnType(String selectedStockTxnType) {
		this.selectedStockTxnType = selectedStockTxnType;
	}
	
	public String getSelectedStockTxnQueryType() {
		return this.selectedStockTxnQueryType;
	}
 
	public void setSelectedStockTxnQueryType(String selectedStockTxnQueryType) {
		this.selectedStockTxnQueryType = selectedStockTxnQueryType;
	}
	
	public Integer getNumShares() {
		return this.numShares;
	}
 
	public void setNumShares(Integer numShares) {
		this.numShares = numShares;
	}	
	
	public String getAverageCostBase() {
		return this.averageCostBase;
	}
 
	public void setAverageCostBase(String averageCostBase) {
		this.averageCostBase = averageCostBase;
	}
	
	public String getMarketValue() {
		return this.marketValue;
	}
 
	public void setMarketValue(String marketValue) {
		this.marketValue = marketValue;
	}
	
	public String getCurSharePrice() {
		return this.curSharePrice;
	}
 
	public void setCurSharePrice(String curSharePrice) {
		this.curSharePrice = curSharePrice;
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
	
	public Boolean getSelTxnAfterLatestSell() {
		return this.selTxnAfterLatestSell;
	}
 
	public void setSelTxnAfterLatestSell(Boolean selTxnAfterLatestSell) {
		this.selTxnAfterLatestSell = selTxnAfterLatestSell;
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
	
	public StockInfo getStockInfo() {
		return this.stockInfo;
	}
 
	public void setStockInfo(StockInfo stockInfo) {
		this.stockInfo = stockInfo;
	}
	
	public String doSelectedDetail() {	
		if (selectedTxn != null) {
			currentScreenInfo.setTxn(selectedTxn);
			currentScreenInfo.setNewTxn(Boolean.FALSE);
			currentScreenInfo.setTxnType(null);
			return "stocktxn-detail.xhtml?faces-redirect=true";
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
			txntypeDesc = translations.getStocktxntypes().get(selectedTxn.getStockTxnType());
			List<StockTxn> txnList = stockTxnDao.findWithinTxnDtRangeByType(stock, stock.getOpenDt(), new Date(), SELL, "DESC");
			// For sells--> can only reverse latest one
			// For buys--> can reverse only those after unreversed latest sell
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
					if (selectedTxn.getStockTxnType().equals(SELL)) {
						selTxnAfterLatestSell = new Boolean(!selectedTxn.getTxnDt().before(txnList.get(i).getTxnDt()));
					}
					else if (selectedTxn.getStockTxnType().equals(BUY)) {
						selTxnAfterLatestSell = new Boolean(selectedTxn.getTxnDt().after(txnList.get(i).getTxnDt()));
					}
				}
			}
			else {
				selTxnAfterLatestSell = Boolean.TRUE;
			}
		}
		
	}
	
	public void reverseStockTxn(ActionEvent event) {		
		txnService.reverse(stock, selectedTxn);
		stock = stockDao.findById(stock.getAccountId());
		numShares = new Integer(stockService.calcNoShares(stock.getStockTxns()));
		averageCostBase = utilityService.round(stockService.calcAverageCostBase(stock, new Date()), 2).toString();
		marketValue = utilityService.round(stockService.calcNoShares(stock.getStockTxns()) * Double.parseDouble(curSharePrice), 2).toString();
		stockService.updateRunningUnits(stock);
		retrieveTxnList();
	}
	
	public String doNewStockTxnScreen() {
		currentScreenInfo.setTxn(null);
		currentScreenInfo.setNewTxn(Boolean.TRUE);
		currentScreenInfo.setTxnType(selectedStockTxnType);
		stockTxnDetMVC.init();
		return "stocktxn-detail.xhtml?faces-redirect=true";
	}
	
	public void retrieveTxnList() {
		if (useAllDates.booleanValue()) {
			txnList = stockTxnDao.findWithinTxnDtRangeByType(stock, stock.getOpenDt(), new Date(), selectedStockTxnQueryType, "ASC");
		}
		else {
			txnList = stockTxnDao.findWithinTxnDtRangeByType(stock, txnDtMin, txnDtMax, selectedStockTxnQueryType, "ASC");
		}
		for (StockTxn stockTxn : txnList ) {
			String display = translations.getStocktxntypes().get(stockTxn.getStockTxnType()) +
				      	((stockTxn.getReversed()) ? " (Reversed)" : " (Original)");
				         
			stockTxn.setDisplay(display);
		}
	}

}

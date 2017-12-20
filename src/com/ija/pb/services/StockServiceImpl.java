package com.ija.pb.services;

import java.util.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.net.URL;
import javax.xml.namespace.QName;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ija.pb.hibernate.tables.*;
import com.ija.pb.hibernate.dao.*;

import com.ija.stockserv.*;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	@Qualifier("stockTxnDao")
	private StockTxnDao stockTxnDao;
	//@Autowired
	//private StockInfo stockInfo;
	
	private static final String OPEN_ACCOUNT = "O";
	private static final String BUY = "B";
	private static final String SELL = "S";
	private static final String ALL = "ALL";
	
	private static final String DEBIT = "D";
	private static final String CREDIT = "C";
	
	@Override
	public int calcNoShares(Set<StockTxn> stockTxns) {
		if (stockTxns != null & !stockTxns.isEmpty()) {
			int total = 0;
			for (StockTxn stockTxn: stockTxns) {
				if (!stockTxn.getReversed()) {
					total += (stockTxn.getDrCr().equals(DEBIT) ? 1 : -1) * stockTxn.getUnits().intValue();
				}
				
			}
			return total;
		}
		else {
			return (0);
		}
	}
	
	@Override
	public double calcAverageCostBase(Stock stock, Date valDt) {
		List<StockTxn> stockTxns = stockTxnDao.findWithinTxnDtRangeByType(stock, stock.getOpenDt(), valDt, ALL, "ASC");
		if (stockTxns != null & !stockTxns.isEmpty()) {
			double acb = 0;
			int units = 0;
			for (StockTxn stockTxn: stockTxns) {
				if (!stockTxn.getReversed()) {
					if (stockTxn.getStockTxnType().equals(BUY) || stockTxn.getStockTxnType().equals(OPEN_ACCOUNT)) {
						units += stockTxn.getUnits().intValue();
						acb += (stockTxn.getUnits().doubleValue() * stockTxn.getPrice().doubleValue()) + stockTxn.getFee().doubleValue();
					}
					else {
						acb -= (acb * (stockTxn.getUnits().doubleValue()/(units + 0.0)));
						units -= stockTxn.getUnits().intValue();
					}
				}
			}
			return acb;
		}
		else {
			return (0);
		}
	}


	@Override
	public double calcCapitalGain(double price, int unitsSold, int unitsOnHand, double fee, double acb) {
		return (price * (unitsSold + 0.0)) - fee - (acb * ((unitsSold + 0.0)/(unitsOnHand +0.0)));
	}
	
	@Override
	public void updateRunningUnits(Stock stock) {
		List<StockTxn> stockTxns = stockTxnDao.findWithinTxnDtRangeByType(stock, stock.getOpenDt(), new Date(), ALL, "ASC");
		StockTxn oldTxn = null;
		int runningUnits = 0;
		boolean updateFlag = false;
		for (StockTxn aStockTxn : stockTxns) {
			updateFlag = false;
			if (oldTxn != null && oldTxn.getReversed().booleanValue() ) {
				if (oldTxn.getRunningUnits() != null) {
					oldTxn.setRunningUnits(null);
					updateFlag = true;
				}
			}
			else if (oldTxn != null && aStockTxn.getTxnDt().compareTo(oldTxn.getTxnDt()) != 0 && 
						(oldTxn.getRunningUnits() == null || oldTxn.getRunningUnits().doubleValue() != runningUnits)) 
			{
					oldTxn.setRunningUnits(new Integer(runningUnits));
					updateFlag = true;
			}
			else if (oldTxn != null && aStockTxn.getTxnDt().compareTo(oldTxn.getTxnDt()) == 0 && 
							 oldTxn.getRunningUnits() != null) 
			{
				oldTxn.setRunningUnits(null);
				updateFlag = true;
			}
			if (updateFlag) {
				stockTxnDao.store(oldTxn);
			}
			if (!aStockTxn.getReversed().booleanValue()) {
				runningUnits += (aStockTxn.getDrCr().equals("D") ? 1 : -1) * aStockTxn.getUnits().doubleValue();
			}
			oldTxn = aStockTxn;
		}
		if (oldTxn != null && oldTxn.getReversed().booleanValue() && oldTxn.getRunningUnits() != null) {
			oldTxn.setRunningUnits(null);
			updateFlag = true;
		}
		else if (oldTxn != null && !oldTxn.getReversed().booleanValue() && 
				(oldTxn.getRunningUnits() == null || oldTxn.getRunningUnits().doubleValue() != runningUnits))
		{
			oldTxn.setRunningUnits(new Integer(runningUnits));
			updateFlag = true;
		}
		if (updateFlag) {
			stockTxnDao.store(oldTxn);
		}
	}
	
	@Override
	public StockTxn createTxn(Double price, Integer units, Double fee, Double capitalGain, Double acbForTxn,
						String stockTxnType, Stock stock, Date txnDt, Integer offsetTxnId, Account offsetAccount)
	{
		StockTxn stockTxn = new StockTxn();
		Date sysDt = new Date();
		stockTxn.setPrice(price);
		stockTxn.setUnits(units);
		stockTxn.setFee(fee);
		stockTxn.setAcbForTxn(acbForTxn);
		stockTxn.setCapitalGain(capitalGain);
		stockTxn.setStockTxnType(stockTxnType);
		stockTxn.setStock(stock);
		stockTxn.setTxnAcctType("ST");
		stockTxn.setReversed(Boolean.FALSE);
		stockTxn.setTxnDt(txnDt);
		stockTxn.setDtCreated(sysDt);
		stockTxn.setDtChanged(sysDt);
		stockTxn.setUpdatedBy("SYSTEM");
		if (stockTxnType.equals(OPEN_ACCOUNT) || stockTxnType.equals(BUY)) {
			stockTxn.setDrCr(DEBIT);
		}
		else {
			stockTxn.setDrCr(CREDIT);
		}
		stockTxn.setOffsetAccount(offsetAccount);
		stockTxn.setOffsetTxnId(offsetTxnId);
		stockTxnDao.store(stockTxn);
		return stockTxn;		
	}
	

}

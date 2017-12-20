package com.ija.pb.services;

import java.util.*;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import com.ija.stockserv.*;

@Service
@Endpoint
public class StockWSEndpointImpl  {
	
	private static final String namespaceUri = "http://localhost:8080/StockQuoteJaxWs/services";
	
	@Autowired
	private GetQuote gqs;
	@Autowired 
	@Qualifier("utilityService")
	private UtilityService utilityService;

	public void setGetQuote(GetQuote gqs) {
		this.gqs = gqs;
	}

	//@Override	
	@PayloadRoot(
			 localPart = "getWebServiceStockQuote",
			 namespace = namespaceUri )
	public StockInfo getWebServiceStockQuote(String symbol, Date tradeDt) {
		StockInfo stockInfo = null;
		try {
			//GetQuote port = gqs.getGetQuotePort();  
			QuoteInputData quoteIn = new QuoteInputData();
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(tradeDt);
			XMLGregorianCalendar xcg = javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			quoteIn.setSymbol(symbol);
			quoteIn.setTradeDt(tradeDt);
			stockInfo = gqs.getQuote(quoteIn);
			//remove quotes (") from character info retrieved by web service
			stockInfo.setName(utilityService.removeQuotes(stockInfo.getName()));
			stockInfo.setStockExchange(utilityService.removeQuotes(stockInfo.getStockExchange()));
			stockInfo.setCurrency(utilityService.removeQuotes(stockInfo.getCurrency()));
			stockInfo.setYearRange(utilityService.removeQuotes(stockInfo.getYearRange()));
		} catch(Exception e) {
			e.printStackTrace();
		}	
		return stockInfo;
		
	}

}

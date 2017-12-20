package com.ija.pb.hibernate.tables;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "stock", schema="personalbanker")
@PrimaryKeyJoinColumn(name="AccountId")
public class Stock extends Account {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Column(name = "Symbol",  nullable = false, unique = true, length= 10, columnDefinition = "varchar(10) default '' ")
	private String symbol;
		
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "stock")
	private Set<StockTxn> stockTxns = new HashSet<StockTxn>(0);
	
	public Stock() {
	}
	
	public String getSymbol() {
		return this.symbol;
	}
 
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}	
	
	public Set<StockTxn> getStockTxns() {
		return this.stockTxns;
	}
	
	public void setStockTxns(Set<StockTxn> stockTxns) {
		this.stockTxns = stockTxns;
	}

}

package com.ija.pb.hibernate.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "stocktxn", schema="personalbanker")
@PrimaryKeyJoinColumn(name="TxnId")
public class StockTxn extends Txn {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Column(name = "Units",  nullable = false, columnDefinition = "int default 0 ")
	private Integer units;
	
	@Column(name = "RunningUnits",  nullable = true, columnDefinition = "int default 0 ")
	private Integer runningUnits;
	
	@Column(name = "Price",  nullable = false, columnDefinition = "double default 0 ")
	private Double price;
	
	@Column(name = "Fee",  nullable = false, columnDefinition = "double default 0 ")
	private Double fee;
	
	@Column(name = "AcbForTxn",  nullable = true, columnDefinition = "double default 0 ")
	private Double acbForTxn;
	
	@Column(name = "CapitalGain",  nullable = true, columnDefinition = "double default 0 ")
	private Double capitalGain;
	
	@Column(name = "StockTxnType",  nullable = false, length= 2, columnDefinition = "varchar(2) default '' ")
	private String stockTxnType;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="AccountId",nullable=false)
    private Stock stock;
	
	public StockTxn() {
	}
	
	public Integer getUnits() {
		return this.units;
	}
 
	public void setUnits(Integer units) {
		this.units = units;
	}
	
	public Integer getRunningUnits() {
		return this.runningUnits;
	}
 
	public void setRunningUnits(Integer runningUnits) {
		this.runningUnits = runningUnits;
	}
	
	public Double getFee() {
		return this.fee;
	}
 
	public void setFee(Double fee) {
		this.fee = fee;
	}
	
	public Double getPrice() {
		return this.price;
	}
 
	public void setPrice(Double price) {
		this.price = price;
	}	
	
	public Double getCapitalGain() {
		return this.capitalGain;
	}
 
	public void setCapitalGain(Double capitalGain) {
		this.capitalGain = capitalGain;
	}
	
	public Double getAcbForTxn() {
		return this.acbForTxn;
	}
 
	public void setAcbForTxn(Double acbForTxn) {
		this.acbForTxn = acbForTxn;
	}
	
	public String getStockTxnType() {
		return this.stockTxnType;
	}
 
	public void setStockTxnType(String stockTxnType) {
		this.stockTxnType = stockTxnType;
	}
	
	public Stock getStock() {
		return this.stock;
	}
 
	public void setStock(Stock stock) {
		this.stock = stock;
	}

}

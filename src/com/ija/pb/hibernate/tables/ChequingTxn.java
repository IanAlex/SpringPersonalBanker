package com.ija.pb.hibernate.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "chequingtxn", schema="personalbanker")
@PrimaryKeyJoinColumn(name="TxnId")
public class ChequingTxn extends Txn {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Column(name = "Amount",  nullable = false, columnDefinition = "double default 0 ")
	private Double amount;
	
	@Column(name = "ChequingTxnType",  nullable = false, length= 2, columnDefinition = "varchar(2) default '' ")
	private String chequingTxnType;
	
	@Column(name = "ChequeNo",  nullable = true, length = 10, columnDefinition = "varchar(10) default '' ")
	private String chequeNo;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="AccountId",nullable=false)
    private Chequing chequing;
	
	public ChequingTxn() {
	}
	
	public Double getAmount() {
		return this.amount;
	}
 
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getChequingTxnType() {
		return this.chequingTxnType;
	}
 
	public void setChequingTxnType(String chequingTxnType) {
		this.chequingTxnType = chequingTxnType;
	}
	
	public String getChequeNo() {
		return this.chequeNo;
	}
 
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	
	public Chequing getChequing() {
		return this.chequing;
	}
 
	public void setChequing(Chequing chequing) {
		this.chequing = chequing;
	}
	
}

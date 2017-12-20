package com.ija.pb.hibernate.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "savingstxn", schema="personalbanker")
@PrimaryKeyJoinColumn(name="TxnId")
public class SavingsTxn extends Txn {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Column(name = "Amount",  nullable = false, columnDefinition = "double default 0 ")
	private Double amount;
	
	@Column(name = "SavingsTxnType",  nullable = false, length= 2, columnDefinition = "varchar(2) default '' ")
	private String savingsTxnType;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="AccountId",nullable=false)
    private Savings savings;
	
	public SavingsTxn() {
	}
	
	public Double getAmount() {
		return this.amount;
	}
 
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getSavingsTxnType() {
		return this.savingsTxnType;
	}
 
	public void setSavingsTxnType(String savingsTxnType) {
		this.savingsTxnType = savingsTxnType;
	}
	
	public Savings getSavings() {
		return this.savings;
	}
 
	public void setSavings(Savings savings) {
		this.savings = savings;
	}

}

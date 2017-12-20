package com.ija.pb.hibernate.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "annuitytxn", schema="personalbanker")
@PrimaryKeyJoinColumn(name="TxnId")
public class AnnuityTxn extends Txn {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Column(name = "Amount",  nullable = false, columnDefinition = "double default 0 ")
	private Double amount;
	
	@Column(name = "AnnuityTxnType",  nullable = false, length= 2, columnDefinition = "varchar(2) default '' ")
	private String annuityTxnType;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="AccountId",nullable=false)
    private Annuity annuity;
	
	public AnnuityTxn() {
	}
	
	public Double getAmount() {
		return this.amount;
	}
 
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getAnnuityTxnType() {
		return this.annuityTxnType;
	}
 
	public void setAnnuityTxnType(String annuityTxnType) {
		this.annuityTxnType = annuityTxnType;
	}
	
	public Annuity getAnnuity() {
		return this.annuity;
	}
 
	public void setAnnuity(Annuity annuity) {
		this.annuity = annuity;
	}

}

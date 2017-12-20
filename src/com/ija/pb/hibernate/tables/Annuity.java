package com.ija.pb.hibernate.tables;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "annuity", schema="personalbanker")
@PrimaryKeyJoinColumn(name="AccountId")
public class Annuity extends Account {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Column(name = "TermMonths",  nullable = false, columnDefinition = "int default 0 ")
	private Integer termMonths;
	
	@Column(name = "PeriodicPayment",  nullable = false, columnDefinition = "double default 0 ")
	private Double periodicPayment;
	
	@Column(name = "PayFrequency",  nullable = false, columnDefinition = "int default 0 ")
	private Integer payfrequency;
	
	@Column(name = "IntCompoundFrequency",  nullable = false, columnDefinition = "int default 0 ")
	private Integer intCompoundfrequency;
	
	@Column(name = "InterestRate",  nullable = false, columnDefinition = "double default 0 ")
	private Double interestRate;
	
	@OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="PayAccountId", referencedColumnName="AccountId",
        insertable=true, updatable=true,nullable=true)
    private Account payAccount;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "annuity")
	private Set<AnnuityTxn> annuityTxns = new HashSet<AnnuityTxn>(0);
	
	public Annuity() {
	}
	
	public Integer getTermMonths() {
		return this.termMonths;
	}
 
	public void setTermMonths(Integer termMonths) {
		this.termMonths = termMonths;
	}
	
	public Double getPeriodicPayment() {
		return this.periodicPayment;
	}
 
	public void setPeriodicPayment(Double periodicPayment) {
		this.periodicPayment = periodicPayment;
	}
	
	public Integer getPayfrequency() {
		return this.payfrequency;
	}
 
	public void setPayfrequency(Integer payfrequency) {
		this.payfrequency = payfrequency;
	}
	
	public Integer getIntCompoundfrequency() {
		return this.intCompoundfrequency;
	}
 
	public void setIntCompoundfrequency(Integer intCompoundfrequency) {
		this.intCompoundfrequency = intCompoundfrequency;
	}	
	
	public Double getInterestRate() {
		return this.interestRate;
	}
 
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}	
	
	public Account getPayAccount() {
		return this.payAccount;
	}
 
	public void setPayAccount(Account payAccount) {
		this.payAccount = payAccount;
	}	
	
	public Set<AnnuityTxn> getAnnuityTxns() {
		return this.annuityTxns;
	}
	
	public void setAnnuityTxns(Set<AnnuityTxn> annuityTxns) {
		this.annuityTxns = annuityTxns;
	}

}

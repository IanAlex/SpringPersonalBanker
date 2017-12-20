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
@Table(name = "gic", schema="personalbanker")
@PrimaryKeyJoinColumn(name="AccountId")
public class GIC extends Account {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Column(name = "MaturityDt",  nullable = false, columnDefinition = "datetime")
	private Date maturityDt;
	
	@Column(name = "InterestRate",  nullable = false, columnDefinition = "double default 0 ")
	private Double interestRate;
	
	@Column(name = "IntCompoundFrequency",  nullable = false, columnDefinition = "int default 0 ")
	private Integer intCompoundfrequency;
	
	@Column(name = "Cashable",  nullable = false, columnDefinition = "int default 0 ")
	private Boolean cashable;
	
	@OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="PayAccountId", referencedColumnName="AccountId",
        insertable=true, updatable=true,nullable=true)
    private Account payAccount;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "gic")
	private Set<GICTxn> gicTxns = new HashSet<GICTxn>(0);
		
	public GIC() {
	}
	
	public Date getMaturityDt() {
		return this.maturityDt;
	}
 
	public void setMaturityDt(Date maturityDt) {
		this.maturityDt = maturityDt;
	}
		
	public Double getInterestRate() {
		return this.interestRate;
	}
 
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}
	
	public Integer getIntCompoundfrequency() {
		return this.intCompoundfrequency;
	}
 
	public void setIntCompoundfrequency(Integer intCompoundfrequency) {
		this.intCompoundfrequency = intCompoundfrequency;
	}	
	
	public Boolean getCashable() {
		return this.cashable;
	}
 
	public void setCashable(Boolean cashable) {
		this.cashable = cashable;
	}
	
	public Account getPayAccount() {
		return this.payAccount;
	}
 
	public void setPayAccount(Account payAccount) {
		this.payAccount = payAccount;
	}	
	
	public Set<GICTxn> getGicTxns() {
		return this.gicTxns;
	}
	
	public void setGicTxns(Set<GICTxn> gicTxns) {
		this.gicTxns = gicTxns;
	}

}

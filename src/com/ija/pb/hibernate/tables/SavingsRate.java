package com.ija.pb.hibernate.tables;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "savingsrate", schema="personalbanker")
public class SavingsRate implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "SavingsRateId", unique = true, nullable = false)
	private Integer savingsRateId;
	
	@Column(name = "EffectiveDt", unique = true, nullable = false, columnDefinition = "datetime")
	private Date effectiveDt;
	
	@Column(name = "InterestRate",  nullable = false, columnDefinition = "double default 0 ")
	private Double interestRate;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(referencedColumnName="AccountId", nullable=false)
    private Savings savings;
	
	@Column(name = "DtCreated",  nullable = false, columnDefinition = "datetime")
	private Date dtCreated;
	
	@Column(name = "DtChanged",  nullable = false, columnDefinition = "datetime")
	private Date dtChanged;
	
	@Column(name = "UpdatedBy",  nullable = false, length = 20, columnDefinition = "varchar(20) default '' ")
	private String updatedBy;
	
	public SavingsRate() {
	}
	
	public Integer getSavingsRateId() {
		return this.savingsRateId;
	}
 
	public void setSavingsRateId(Integer savingsRateId) {
		this.savingsRateId = savingsRateId;
	}
	
	public Date getEffectiveDt() {
		return this.effectiveDt;
	}
 
	public void setEffectiveDt(Date effectiveDt) {
		this.effectiveDt = effectiveDt;
	}
	
	public Double getInterestRate() {
		return this.interestRate;
	}
 
	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}
	
	public Savings getSavings() {
		return this.savings;
	}
 
	public void setSavings(Savings savings) {
		this.savings = savings;
	}
	
	public Date getDtCreated() {
		return this.dtCreated;
	}
 
	public void setDtCreated(Date dtCreated) {
		this.dtCreated = dtCreated;
	}
	
	public Date getDtChanged() {
		return this.dtChanged;
	}
 
	public void setDtChanged(Date dtChanged) {
		this.dtChanged = dtChanged;
	}
	
	public String getUpdatedBy() {
		return this.updatedBy;
	}
 
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}	

}

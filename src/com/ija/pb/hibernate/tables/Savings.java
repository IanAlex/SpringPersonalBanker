package com.ija.pb.hibernate.tables;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "savings", schema="personalbanker")
@PrimaryKeyJoinColumn(name="AccountId")
public class Savings extends Account {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval=true, mappedBy = "savings")
	private Set<SavingsTxn> savingsTxns = new HashSet<SavingsTxn>(0);
	
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval=true, mappedBy = "savings")
	private Set<SavingsRate> savingsRates = new HashSet<SavingsRate>(0);
	
	public Savings() {
	}
	
	public Set<SavingsTxn> getSavingsTxns() {
		return this.savingsTxns;
	}
	
	public void setSavingsTxns(Set<SavingsTxn> savingsTxns) {
		this.savingsTxns = savingsTxns;
	}
	
	public Set<SavingsRate> getSavingsRates() {
		return this.savingsRates;
	}
	
	public void setSavingsRates(Set<SavingsRate> savingsRates) {
		this.savingsRates = savingsRates;
	}

}

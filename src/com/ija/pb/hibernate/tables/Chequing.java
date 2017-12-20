package com.ija.pb.hibernate.tables;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "chequing", schema="personalbanker")
@PrimaryKeyJoinColumn(name="AccountId")
public class Chequing extends Account {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "chequing")
	private Set<ChequingTxn> chequingTxns = new HashSet<ChequingTxn>(0);
	
	public Chequing() {
	}
	
	public Set<ChequingTxn> getChequingTxns() {
		return this.chequingTxns;
	}
	
	public void setChequingTxns(Set<ChequingTxn> chequingTxns) {
		this.chequingTxns = chequingTxns;
	}

}

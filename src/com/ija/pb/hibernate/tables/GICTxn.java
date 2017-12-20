package com.ija.pb.hibernate.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "gictxn", schema="personalbanker")
@PrimaryKeyJoinColumn(name="TxnId")
public class GICTxn extends Txn {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Column(name = "Amount",  nullable = false, columnDefinition = "double default 0 ")
	private Double amount;
	
	@Column(name = "GicTxnType",  nullable = false, length= 2, columnDefinition = "varchar(2) default '' ")
	private String gicTxnType;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="AccountId",nullable=false)
    private GIC gic;
	
	public GICTxn() {
	}
	
	public Double getAmount() {
		return this.amount;
	}
 
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getGicTxnType() {
		return this.gicTxnType;
	}
 
	public void setGicTxnType(String gicTxnType) {
		this.gicTxnType = gicTxnType;
	}
	
	public GIC getGic() {
		return this.gic;
	}
 
	public void setGic(GIC gic) {
		this.gic = gic;
	}

}

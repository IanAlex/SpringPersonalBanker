package com.ija.pb.hibernate.tables;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "txn", schema="personalbanker")
@Inheritance(strategy=InheritanceType.JOINED)
public class Txn implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "TxnId", unique = true, nullable = false)
	private Integer txnId;
	
	@Column(name = "TxnAcctType",  nullable = false, length= 2, columnDefinition = "varchar(2) default '' ")
	private String txnAcctType;
	
	@Column(name = "TxnDt",  nullable = false, columnDefinition = "datetime")
	private Date txnDt;
	
	@Column(name = "DrCr",  nullable = false, length = 2, columnDefinition = "varchar(2) default '' ")
	private String drCr;
	
	@Column(name = "Reversed",  nullable = false, columnDefinition = "int default 0 ")
	private Boolean reversed;
	
	@Column(name = "OffsetTxnId", unique = true, nullable = true)
	private Integer offsetTxnId;
	
	@OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="AccountId", referencedColumnName="AccountId",
        insertable=true, updatable=true,nullable=true,unique=false)
    private Account offsetAccount;
	
	@Column(name = "OffsetTxnAcctInfo",  nullable = true, length= 25, columnDefinition = "varchar(25) default '' ")
	private String offsetTxnAcctInfo;
			
	@Column(name = "RunningAcctBalance",  nullable = true, columnDefinition = "double default 0 ")
	private Double runningAcctBalance;
	
	@Column(name = "DtCreated",  nullable = false, columnDefinition = "datetime")
	private Date dtCreated;
	
	@Column(name = "DtChanged",  nullable = false, columnDefinition = "datetime")
	private Date dtChanged;
	
	@Column(name = "UpdatedBy",  nullable = false, length = 20, columnDefinition = "varchar(20) default '' ")
	private String updatedBy;
	
	// computed  field -- stored value means nothing
	@Column(name = "Display", nullable = true, columnDefinition = "varchar(50)")
	private String display;
	
	public Txn() {
	}
	
	public Integer getTxnId() {
		return this.txnId;
	}
 
	public void setTxnId(Integer txnId) {
		this.txnId = txnId;
	}
	
	public String getTxnAcctType() {
		return this.txnAcctType;
	}
 
	public void setTxnAcctType(String txnAcctType) {
		this.txnAcctType = txnAcctType;
	}
	
	public Date getTxnDt() {
		return this.txnDt;
	}
 
	public void setTxnDt(Date txnDt) {
		this.txnDt = txnDt;
	}
	
	public String getDrCr() {
		return this.drCr;
	}
 
	public void setDrCr(String drCr) {
		this.drCr = drCr;
	}
	
	public Boolean getReversed() {
		return this.reversed;
	}
 
	public void setReversed(Boolean reversed) {
		this.reversed = reversed;
	}
	
	public Integer getOffsetTxnId() {
		return this.offsetTxnId;
	}
 
	public void setOffsetTxnId(Integer offsetTxnId) {
		this.offsetTxnId = offsetTxnId;
	}
	
	public Account getOffsetAccount() {
		return this.offsetAccount;
	}
 
	public void setOffsetAccount(Account offsetAccount) {
		this.offsetAccount = offsetAccount;
	}
	
	public String getOffsetTxnAcctInfo() {
		return this.offsetTxnAcctInfo;
	}
 
	public void setOffsetTxnAcctInfo(String offsetTxnAcctInfo) {
		this.offsetTxnAcctInfo = offsetTxnAcctInfo;
	}
	
	public Double getRunningAcctBalance() {
		return this.runningAcctBalance;
	}
 
	public void setRunningAcctBalance(Double runningAcctBalance) {
		this.runningAcctBalance = runningAcctBalance;
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
	
	public String getDisplay() {
		return this.display;
	}
 
	public void setDisplay(String display) {
		this.display = display;
	}	

}

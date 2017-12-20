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
import javax.persistence.Table;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "account", schema="personalbanker")
@Inheritance(strategy=InheritanceType.JOINED)
public class Account implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "AccountId", unique = true, nullable = false)
	private Integer accountId;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ClientId",nullable=false)
    private Client client;
	
	@Column(name = "AccountType",  nullable = false, length= 2, columnDefinition = "varchar(2) default '' ")
	private String accountType;
	
	@Column(name = "AccountName",  nullable = false, length= 25, columnDefinition = "varchar(25) default '' ")
	private String accountName;
	
	@Column(name = "AccountNo",  nullable = false, unique=true, length= 20, columnDefinition = "varchar(20) default '' ")
	private String accountNo;
	
	@Column(name = "Registered",  nullable = false, columnDefinition = "int default 0 ")
	private Boolean registered;
	
	@Column(name = "Joint",  nullable = false, columnDefinition = "int default 0 ")
	private Boolean joint;
	
	@Column(name = "Open",  nullable = false, columnDefinition = "int default 1 ")
	private Boolean open;
	
	@Column(name = "OpenDt",  nullable = false, columnDefinition = "datetime")
	private Date openDt;
	
	@Column(name = "closeDt",  nullable = true, columnDefinition = "datetime")
	private Date closeDt;
	
	@Column(name = "DtCreated",  nullable = false, columnDefinition = "datetime")
	private Date dtCreated;
	
	@Column(name = "DtChanged",  nullable = false, columnDefinition = "datetime")
	private Date dtChanged;
	
	@Column(name = "UpdatedBy",  nullable = false, length = 20, columnDefinition = "varchar(20) default '' ")
	private String updatedBy;
	
	// calculated value -- stored amount means nothing
	@Column(name = "Balance", nullable = true, columnDefinition = "double")
	private Double balance;
    
	// computed  field -- stored value means nothing
	@Column(name = "Display", nullable = true, columnDefinition = "varchar(50)")
	private String display;
	
	public Account() {
	}
	
	public Integer getAccountId() {
		return this.accountId;
	}
 
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	
	public Client getClient() {
		return this.client;
	}
 
	public void setClient(Client client) {
		this.client = client;
	}
	
	public String getAccountType() {
		return this.accountType;
	}
 
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getAccountName() {
		return this.accountName;
	}
 
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}	
	
	public String getAccountNo() {
		return this.accountNo;
	}
 
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}	
	
	public Boolean getRegistered() {
		return this.registered;
	}
 
	public void setRegistered(Boolean registered) {
		this.registered = registered;
	}
	
	public Boolean getJoint() {
		return this.joint;
	}
 
	public void setJoint(Boolean joint) {
		this.joint = joint;
	}	
	
	public Boolean getOpen() {
		return this.open;
	}
 
	public void setOpen(Boolean open) {
		this.open = open;
	}
	
	public Date getOpenDt() {
		return this.openDt;
	}
 
	public void setOpenDt(Date openDt) {
		this.openDt = openDt;
	}
	
	public Date getCloseDt() {
		return this.closeDt;
	}
 
	public void setCloseDt(Date closeDt) {
		this.closeDt = closeDt;
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
	
	public Double getBalance() {
		return this.balance;
	}
 
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	public String getDisplay() {
		return this.display;
	}
 
	public void setDisplay(String display) {
		this.display = display;
	}	

}

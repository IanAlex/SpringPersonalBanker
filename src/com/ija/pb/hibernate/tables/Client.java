package com.ija.pb.hibernate.tables;

import java.io.Serializable;
import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "client", schema="personalbanker")
@Inheritance(strategy=InheritanceType.JOINED)
public class Client implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ClientId", unique = true, nullable = false)
	private Integer clientId;
	
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="AddressId", referencedColumnName="AddressId",
        insertable=true, updatable=true,nullable=false,unique=true)
    private Address address;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="LoginId", referencedColumnName="LoginId", 
        insertable=true,updatable=true,nullable=false,unique=true)
    private Login login;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "client")
	private Set<Account> accounts = new HashSet<Account>(0);
    
	@Column(name = "ClientType",  nullable = false, length= 2, columnDefinition = "varchar(2) default '' ")
	private String clientType;
	
	@Column(name = "Email",  nullable = false, length= 40, columnDefinition = "varchar(40) default '' ")
	private String email;
	
	@Column(name = "Telephone",  nullable = false, length= 20, columnDefinition = "varchar(20) default '' ")
	private String telephone;
	
	@Column(name = "Cellphone",  nullable = true, length= 20, columnDefinition = "varchar(20) default '' ")
	private String cellPhone;
	
	@Column(name = "DtCreated",  nullable = false, columnDefinition = "datetime")
	private Date dtCreated;
	
	@Column(name = "DtChanged",  nullable = false, columnDefinition = "datetime")
	private Date dtChanged;
	
	@Column(name = "UpdatedBy",  nullable = false, length = 20, columnDefinition = "varchar(20) default '' ")
	private String updatedBy;
	
	public Client() {
	}
	
	public Integer getClientId() {
		return this.clientId;
	}
 
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}	
	
	public Address getAddress() {
		return this.address;
	}
 
	public void setAddress(Address address) {
		this.address = address;
	}	
	
	public Login getLogin() {
		return this.login;
	}
 
	public void setLogin(Login login) {
		this.login = login;
	}
	
	public String getClientType() {
		return this.clientType;
	}
 
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}	
	
	public String getEmail() {
		return this.email;
	}
 
	public void setEmail(String email) {
		this.email = email;
	}	
	
	public String getTelephone() {
		return this.telephone;
	}
 
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}	
	
	public String getCellPhone() {
		return this.cellPhone;
	}
 
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
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
	
	public Set<Account> getAccounts() {
		return this.accounts;
	}
	
	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

}
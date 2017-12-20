package com.ija.pb.nondaodata;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.ija.pb.hibernate.tables.*;

@Component
public class CurrentScreenInfo implements Serializable {

	private static final long serialVersionUID = -2912693560354598053L;
	
	private String loginKey;
	private Client client;
	private Person person;
	private Business business;
	private Address address;
	private Chequing chequing;
	private Annuity annuity;
	private GIC gic;
	private Stock stock;
	private Savings savings;
	private Chequing wizChequing;
	private Annuity wizAnnuity;
	private GIC wizGic;
	private Stock wizStock;
	private Savings wizSavings;
	private Account newAccount;
	private Boolean inWizAcctValidation;
	private Client wizClient;
	private Person wizPerson;
	private Business wizBusiness;
	private Address wizAddress;
	private Login wizLogin;
	private Txn txn;
	private Boolean newTxn;
	private String txnType;
	private Boolean clientEdit;
	private Boolean addressEdit;
	private Boolean loginEdit;
	
	public CurrentScreenInfo() {
		System.out.println("hi ian in currentScreenInfo ctor");
	}
	
	@PostConstruct
	public void init() {
		this.clientEdit = Boolean.TRUE;
		this.addressEdit = Boolean.TRUE;
		this.loginEdit = Boolean.FALSE;
	}
	
	public String getLoginKey() {
		return this.loginKey;
	}
 
	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}
	
	public Boolean isthereLoginKey() {
		return (loginKey != null);
	}
	
	public Client getClient() {
		return this.client;
	}
 
	public void setClient(Client client) {
		this.client = client;
	}
	
	public Boolean isthereClient() {
		return (client != null);
	}	
	
	public Person getPerson() {
		return this.person;
	}
 
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public Boolean istherePerson() {
		return (person != null);
	}
	
	public Business getBusiness() {
		return this.business;
	}
 
	public void setBusiness(Business business) {
		this.business = business;
	}
	
	public Boolean isthereBusiness() {
		return (business != null);
	}
	
	public Address getAddress() {
		return this.address;
	}
 
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Boolean isthereAddress() {
		return (address != null);
	}
		
	public Chequing getChequing() {
		return this.chequing;
	}
 
	public void setChequing(Chequing chequing) {
		this.chequing = chequing;
	}
	
	public Boolean isthereChequing() {
		return (chequing != null);
	}
	
	public Annuity getAnnuity() {
		return this.annuity;
	}
 
	public void setAnnuity(Annuity annuity) {
		this.annuity = annuity;
	}
	
	public Boolean isthereAnnuity() {
		return (annuity != null);
	}
	
	public GIC getGic() {
		return this.gic;
	}
 
	public void setGic(GIC gic) {
		this.gic = gic;
	}
	
	public Boolean isthereGic() {
		return (gic != null);
	}
	
	public Stock getStock() {
		return this.stock;
	}
 
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	
	public Boolean isthereStock() {
		return (stock != null);
	}
	
	public Savings getSavings() {
		return this.savings;
	}
 
	public void setSavings(Savings savings) {
		this.savings = savings;
	}
	
	public Boolean isthereSavings() {
		return (savings != null);
	}
	
	public Chequing getWizChequing() {
		return this.wizChequing;
	}
 
	public void setWizChequing(Chequing wizChequing) {
		this.wizChequing = wizChequing;
	}
	
	public Boolean isthereWizChequing() {
		return (wizChequing != null);
	}
	
	public Annuity getWizAnnuity() {
		return this.wizAnnuity;
	}
 
	public void setWizAnnuity(Annuity wizAnnuity) {
		this.wizAnnuity = wizAnnuity;
	}
	
	public Boolean isthereWizAnnuity() {
		return (wizAnnuity != null);
	}
	
	public GIC getWizGic() {
		return this.wizGic;
	}
 
	public void setWizGic(GIC wizGic) {
		this.wizGic = wizGic;
	}
	
	public Boolean isthereWizGic() {
		return (wizGic != null);
	}
	
	public Stock getWizStock() {
		return this.wizStock;
	}
 
	public void setWizStock(Stock wizStock) {
		this.wizStock = wizStock;
	}
	
	public Boolean isthereWizStock() {
		return (wizStock != null);
	}
	
	public Savings getWizSavings() {
		return this.wizSavings;
	}
 
	public void setWizSavings(Savings wizSavings) {
		this.wizSavings = wizSavings;
	}
	
	public Boolean isthereWizSavings() {
		return (wizSavings != null);
	}
	
	public Account getNewAccount() {
		return this.newAccount;
	}
 
	public void setNewAccount(Account newAccount) {
		this.newAccount = newAccount;
	}
	
	public Boolean getInWizAcctValidation() {
		return this.inWizAcctValidation;
	}
 
	public void setinWizAcctValidation(Boolean InWizAcctValidation) {
		this.inWizAcctValidation = inWizAcctValidation;
	}
	
	public Boolean isthereNewAccount() {
		return (newAccount != null);
	}
	
	public Client getWizClient() {
		return this.wizClient;
	}
 
	public void setWizClient(Client wizClient) {
		this.wizClient = wizClient;
	}
	
	public Boolean isthereWizClient() {
		return (wizClient != null);
	}
	
	public Person getWizPerson() {
		return this.wizPerson;
	}
 
	public void setWizPerson(Person wizPerson) {
		this.wizPerson = wizPerson;
	}
	
	public Boolean isthereWizPerson() {
		return (wizPerson != null);
	}
	
	public Business getWizBusiness() {
		return this.wizBusiness;
	}
 
	public void setWizBusiness(Business wizBusiness) {
		this.wizBusiness = wizBusiness;
	}
	
	public Boolean isthereWizBusiness() {
		return (wizBusiness != null);
	}	
	
	public Address getWizAddress() {
		return this.wizAddress;
	}
 
	public void setWizAddress(Address wizAddress) {
		this.wizAddress = wizAddress;
	}
	
	public Boolean isthereWizAddress() {
		return (wizAddress != null);
	}
	
	public Login getWizLogin() {
		return this.wizLogin;
	}
 
	public void setWizLogin(Login wizLogin) {
		this.wizLogin = wizLogin;
	}
	
	public Boolean isthereWizLogin() {
		return (wizLogin != null);
	}
	
	public Txn getTxn() {
		return this.txn;
	}
 
	public void setTxn(Txn txn) {
		this.txn = txn;
	}
	
	public Boolean getNewTxn() {
		return this.newTxn;
	}
 
	public void setNewTxn(Boolean newTxn) {
		this.newTxn = newTxn;
	}
	
	public String getTxnType() {
		return this.txnType;
	}
 
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	
	public Boolean getClientEdit() {
		return this.clientEdit;
	}
 
	public void setClientEdit(Boolean clientEdit) {
		this.clientEdit = clientEdit;
	}
	
	public Boolean getAddressEdit() {
		return this.addressEdit;
	}
 
	public void setAddressEdit(Boolean addressEdit) {
		this.addressEdit = addressEdit;
	}
	
	public Boolean getLoginEdit() {
		return this.loginEdit;
	}
 
	public void setLoginEdit(Boolean loginEdit) {
		this.loginEdit = loginEdit;
	}
	
}

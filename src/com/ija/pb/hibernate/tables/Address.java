package com.ija.pb.hibernate.tables;

import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "address", schema="personalbanker")
public class Address implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "AddressId", unique = true, nullable = false)
	private Integer addressId;
	
	@Column(name = "Street",  nullable = false, length= 80, columnDefinition = "varchar(80) default '' ")
	private String street;
	
	@Column(name = "Apt_Unit",  nullable = true, length = 7, columnDefinition = "varchar(7) default '' ")
	private String aptUnit;
	
	@Column(name = "City",  nullable = false, length = 25, columnDefinition = "varchar(25) default '' ")
	private String city;
	
	@Column(name = "Country",  nullable = false, length = 10, columnDefinition = "varchar(10) default '' ")
	private String country;
	
	@Column(name = "State_Prov",  nullable = false, length = 10, columnDefinition = "varchar(10) default '' ")
	private String stateProv;
	
	@Column(name = "Postal_Zip",  nullable = false, length = 10, columnDefinition = "varchar(10) default '' ")
	private String postalZip;
	
	@Column(name = "DtCreated",  nullable = false, columnDefinition = "datetime")
	private Date dtCreated;
	
	@Column(name = "DtChanged",  nullable = false, columnDefinition = "datetime")
	private Date dtChanged;
	
	@Column(name = "UpdatedBy",  nullable = false, length = 20, columnDefinition = "varchar(20) default '' ")
	private String updatedBy;
	
	public Address() {
	}
	
	public Integer getAddressId() {
		return this.addressId;
	}
 
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}	
	
	public String getStreet() {
		return this.street;
	}
 
	public void setStreet(String street) {
		this.street = street;
	}	
	
	public String getAptUnit() {
		return this.aptUnit;
	}
 
	public void setAptUnit(String aptUnit) {
		this.aptUnit = aptUnit;
	}	
	
	public String getCity() {
		return this.city;
	}
 
	public void setCity(String city) {
		this.city = city;
	}	
	
	public String getCountry() {
		return this.country;
	}
 
	public void setCountry(String country) {
		this.country = country;
	}	
	
	public String getStateProv() {
		return this.stateProv;
	}
 
	public void setStateProv(String stateProv) {
		this.stateProv = stateProv;
	}	
	
	public String getPostalZip() {
		return this.postalZip;
	}
 
	public void setPostalZip(String postalZip) {
		this.postalZip = postalZip;
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

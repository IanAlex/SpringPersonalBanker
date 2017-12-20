package com.ija.pb.hibernate.tables;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Table(name = "person", schema="personalbanker")
@PrimaryKeyJoinColumn(name="ClientId")
public class Person extends Client {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Column(name = "FirstName",  nullable = false, length= 30, columnDefinition = "varchar(30) default '' ")
	private String firstName;
	
	@Column(name = "LastName",  nullable = false, length= 30, columnDefinition = "varchar(30) default '' ")
	private String lastName;
	
	@Column(name = "MiddleName",  nullable = true, length= 30, columnDefinition = "varchar(30) default '' ")
	private String middleName;
	
	@Column(name = "NamePrefix",  nullable = true, length= 6, columnDefinition = "varchar(6) default '' ")
	private String namePrefix;
	
	@Column(name = "Gender",  nullable = false, length= 2, columnDefinition = "varchar(2) default '' ")
	private String gender;
	
	@Column(name = "MaritalStatus",  nullable = false, length= 2, columnDefinition = "varchar(2) default '' ")
	private String maritalStatus;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumn(name="ClientId",insertable=true,
        updatable=true,nullable=true,unique=true)
    private Person spouse;
	
	@Column(name = "SSN",  nullable = false, length= 20, columnDefinition = "varchar(20) default '' ")
	private String ssn;
	
	@Column(name = "DOB",  nullable = false, columnDefinition = "datetime")
	private Date dob;
	
	public Person() {
	}
	
	public String getFirstName() {
		return this.firstName;
	}
 
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}	
	
	public String getLastName() {
		return this.lastName;
	}
 
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}	
	
	public String getMiddleName() {
		return this.middleName;
	}
 
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}	
	
	public String getNamePrefix() {
		return this.namePrefix;
	}
 
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}	
	
	public String getGender() {
		return this.gender;
	}
 
	public void setGender(String gender) {
		this.gender = gender;
	}	
	
	public String getMaritalStatus() {
		return this.maritalStatus;
	}
 
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}	
	
	public Person getSpouse() {
		return this.spouse;
	}
 
	public void setSpouse(Person spouse) {
		this.spouse = spouse;
	}
	
	public String getSsn() {
		return this.ssn;
	}
 
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	public Date getDob() {
		return this.dob;
	}
 
	public void setDob(Date dob) {
		this.dob = dob;
	}

}

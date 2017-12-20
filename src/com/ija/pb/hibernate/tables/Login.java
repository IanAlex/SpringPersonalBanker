package com.ija.pb.hibernate.tables;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "login", schema="personalbanker")
public class Login implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "LoginId", unique = true, nullable = false)
	private Integer loginId;	
	
	@Column(name = "Name",  nullable = false, unique = true, length= 80, columnDefinition = "varchar(80) default '' ")
	private String name;
	
	@Column(name = "Password",  nullable = false, length= 16, columnDefinition = "varchar(16) default '' ")
	private String password;
	
	@Column(name = "LoginKey", unique = true, nullable = false, length= 25, updatable = false, columnDefinition = "varchar(25) default '' ")
	private String loginKey;
	
	@Column(name = "DtCreated",  nullable = false, columnDefinition = "datetime")
	private Date dtCreated;
	
	@Column(name = "DtChanged",  nullable = false, columnDefinition = "datetime")
	private Date dtChanged;
	
	@Column(name = "UpdatedBy",  nullable = false, length = 20, columnDefinition = "varchar(20) default '' ")
	private String updatedBy;
	
	public Login() {
	}
	
	public Integer getLoginId() {
		return this.loginId;
	}
 
	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}
	
	public String getName() {
		return this.name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return this.password;
	}
 
	public void setPassword(String password) {
		this.password = password;
	}	
	
	public String getLoginKey() {
		return this.loginKey;
	}
 
	public void setLoginkey(String loginKey) {
		this.loginKey = loginKey;
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

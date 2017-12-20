package com.ija.pb.hibernate.tables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Table(name = "business", schema="personalbanker")
@PrimaryKeyJoinColumn(name="ClientId")
public class Business extends Client {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Column(name = "Name",  nullable = false, length= 80, columnDefinition = "varchar(80) default '' ")
	private String name;
	
	@Column(name = "BusNo",  nullable = false, length= 30, columnDefinition = "varchar(30) default '' ")
	private String busNo;
	
	@Column(name = "WebsiteUrl",  nullable = false, length= 60, columnDefinition = "varchar(60) default '' ")
	private String websiteUrl;
	
	public Business() {
	}
	
	public String getName() {
		return this.name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBusNo() {
		return this.busNo;
	}
 
	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}	
	
	public String getWebsiteUrl() {
		return this.websiteUrl;
	}
 
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}	
	
}

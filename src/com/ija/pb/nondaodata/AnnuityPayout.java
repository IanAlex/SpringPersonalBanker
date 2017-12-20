package com.ija.pb.nondaodata;

import java.util.*;
import java.io.Serializable;

public class AnnuityPayout implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	Double pvAmount;
	Date payDate;
	Boolean paid;
	
	public AnnuityPayout() {		
	}
	
	public Double getPvAmount() {
		return this.pvAmount;
	}
 
	public void setPvAmount(Double pvAmount) {
		this.pvAmount = pvAmount;
	}
	
	public Date getPayDate() {
		return this.payDate;
	}
 
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	
	public Boolean getPaid() {
		return this.paid;
	}
 
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}
	
}

package com.ija.jsf.mvc;

import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ija.pb.nondaodata.*;
import com.ija.pb.services.*;

@Component
@ViewScoped
public class RegistrationMVC implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;

	@Autowired
	Translations translations;
	@Autowired 
	@Qualifier("utilityService")
	private UtilityService utilityService;
	
	private Map<String,String> potentialUserTypeMap;
	
	private String firstName;
	private String lastName;
	private String company;
	private String website;	
	private String email;
	private String telephone;
	private String contactReason;	
	private String selectedPotentialUserType;
	
	public RegistrationMVC() {
	}
	
	@PostConstruct
	public void init() {
		firstName = null;
		lastName = null;
		company = null;
		website = null;
		email = null;
		telephone = null;
		contactReason = null;
		potentialUserTypeMap = utilityService.createStandardGUIDisplayMap(translations.getPotentialusertypes());
		selectedPotentialUserType = "PC";
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
	
	public String getCompany() {
		return this.company;
	}
 
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getWebsite() {
		return this.website;
	}
 
	public void setWebsite(String website) {
		this.website = website;
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
	
	public String getContactReason() {
		return this.contactReason;
	}
 
	public void setContactReason(String contactReason) {
		this.contactReason = contactReason;
	}
	
	public Map<String,String> getPotentialUserTypeMap() {
		return this.potentialUserTypeMap;
	}
 
	public void setPotentialUserTypeMap(Map<String,String> potentialUserTypeMap) {
		this.potentialUserTypeMap = potentialUserTypeMap;
	}
	
	public String getSelectedPotentialUserType() {
		return this.selectedPotentialUserType;
	}
 
	public void setSelectedPotentialUserType(String selectedPotentialUserType) {
		this.selectedPotentialUserType = selectedPotentialUserType;
	}
	
	public void validateEmail(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException 
    {
		String enteredEmail = (String) value;
		if (enteredEmail.length() > 40) {
			FacesMessage message = new FacesMessage("EMail Address cannot exceed 40 characters");
			throw new ValidatorException(message);
		}
		if (!utilityService.determineEmailValid(enteredEmail)) {
			FacesMessage message = new FacesMessage("EMail Address is in invalid format");
			throw new ValidatorException(message);
		}
    }
	
	public String processRegistration() {
		System.out.println("hi ian 779");
		String msgTxt = "";
		msgTxt += "First Name: "  + firstName + "\n";
		msgTxt += "Last Name: "  + lastName + "\n";
		msgTxt += "Company/Organization: "  + company + "\n";
		msgTxt += "Web Site: "  + website + "\n";
		msgTxt += "Email: "  + email + "\n";
		msgTxt += "Telephone: "  + telephone + "\n";
		msgTxt += "User Type: "  + translations.getPotentialusertypes().get(selectedPotentialUserType) + "\n";
		msgTxt += "Contact Reason: "  + contactReason + "\n";
		utilityService.sendEmail("IanAlexTest@gmail.com", "IanAlexTest@gmail",
				                  "Request to Register for PeronalBanker", msgTxt,
				                  "IanAlexTest", "itsasecret00");
		return "registration-confirm.xhtml?faces-redirect=true";
	}
	
}

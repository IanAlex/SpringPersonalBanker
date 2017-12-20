package com.ija.jsf.mvc;

import java.util.*;


import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.faces.component.UIComponent;

import org.apache.commons.lang.SerializationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ija.pb.hibernate.tables.*;
import com.ija.pb.hibernate.dao.*;
import com.ija.pb.nondaodata.*;
import com.ija.pb.services.*;

@Component
@ViewScoped
public class ClientEditViewSettingsMVC implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Autowired 
	@Qualifier("loginDao")
	private LoginDao loginDao;
	@Autowired 
	@Qualifier("addressDao")
	private AddressDao addressDao;
	@Autowired 
	@Qualifier("personDao")
	private PersonDao personDao;
	@Autowired 
	@Qualifier("businessDao")
	private BusinessDao businessDao;
	@Autowired
	CurrentScreenInfo currentScreenInfo;
	@Autowired
	Translations translations;
	@Autowired 
	@Qualifier("utilityService")
	private UtilityService utilityService;
	
	private static final String CANADA = "CA";
	private static final String USA = "US";
	
	private static final String EDIT = "Edit";
	private static final String VIEW = "View";	
	
	private static final String PERSON_DET = "Person Detail";
	private static final String BUSINESS_DET = "Business Detail";
	
	private Map<String,String> maritalStatusMap;
	private Map<String,String> genderMap;
	private Map<String,String> namePrefixMap;
	private Map<String,String> countryMap;
	private Map<String,String> stateProvMap;
	private String selectedMaritalStatus;
	private String selectedNamePrefix;
	private String selectedGender;
	private String selectedCountry;
	private String selectedStateProv;
	private String selectedMaritalStatusDesc;
	private String selectedNamePrefixDesc;
	private String selectedGenderDesc;
	private String selectedCountryDesc;
	private String selectedStateProvDesc;
	private Boolean stateProvFlag;
	String clientEditDesc;
	String clientTypeDesc;
	String addressEditDesc;
	Client clientEditObj = null;
	Person personEditObj = null;
	Business businessEditObj = null;
	Address addressEditObj = null;
	
	public ClientEditViewSettingsMVC() {
	}
	
	@PostConstruct
	public void init() {
		stateProvFlag = Boolean.TRUE;
		clientEditObj = null;
		personEditObj = null;
		businessEditObj = null;
		addressEditObj = null;
		if (currentScreenInfo != null && translations != null) {
			currentScreenInfo.setAddressEdit(Boolean.TRUE);
			currentScreenInfo.setClientEdit(Boolean.TRUE);
			if (currentScreenInfo.isthereAddress().booleanValue()) {
				doAddressEditChange();
			}
			if (currentScreenInfo.isthereClient().booleanValue()) {
				doClientEditChange();			
			}
		}
		System.out.println("hi ian 90.01");
	}
	
	public CurrentScreenInfo getCurrentScreenInfo() {
		return this.currentScreenInfo;
	}
 
	public void setCurrentScreenInfo(CurrentScreenInfo currentScreenInfo) {
		this.currentScreenInfo = currentScreenInfo;
	}
	
	public Client getClientEditObj() {
		return this.clientEditObj;
	}
 
	public void setClientEditObj(Client clientEditObj) {
		this.clientEditObj = clientEditObj;
	}
	
	public Address getAddressEditObj() {
		return this.addressEditObj;
	}
 
	public void setAddressEditObj(Address addressEditObj) {
		this.addressEditObj = addressEditObj;
	}
	
	public Person getPersonEditObj() {
		return this.personEditObj;
	}
 
	public void setPersonEditObj(Person personEditObj) {
		this.personEditObj = personEditObj;
	}
	
	public Business getBusinessEditObj() {
		return this.businessEditObj;
	}
 
	public void setBusinessEditObj(Business businessEditObj) {
		this.businessEditObj = businessEditObj;
	}
	
	public String getClientEditDesc() {
		return this.clientEditDesc;
	}
 
	public void setClientEditDesc(String clientEditDesc) {
		this.clientEditDesc = clientEditDesc;
	}
	
	public String getAddressEditDesc() {
		return this.addressEditDesc;
	}
 
	public void setAddressEditDesc(String addressEditDesc) {
		this.addressEditDesc = addressEditDesc;
	}
	
	public String getClientTypeDesc() {
		return this.clientTypeDesc;
	}
 
	public void setClientTypeDesc(String clientTypeDesc) {
		this.clientTypeDesc = clientTypeDesc;
	}
	
	public Map<String,String> getMaritalStatusMap() {
		return this.maritalStatusMap;
	}
 
	public void setMaritalStatusMap(Map<String,String> maritalStatusMap) {
		this.maritalStatusMap = maritalStatusMap;
	}
	
	public String getSelectedMaritalStatus() {
		return this.selectedMaritalStatus;
	}
 
	public void setSelectedMaritalStatus(String selectedMaritalStatus) {
		this.selectedMaritalStatus = selectedMaritalStatus;
	}
	
	public String getSelectedMaritalStatusDesc() {
		return this.selectedMaritalStatusDesc;
	}
 
	public void setSelectedMaritalStatusDesc(String selectedMaritalStatusDesc) {
		this.selectedMaritalStatusDesc = selectedMaritalStatusDesc;
	}
	
	public Map<String,String> getGenderMap() {
		return this.genderMap;
	}
 
	public void setGenderMap(Map<String,String> genderMap) {
		this.genderMap = genderMap;
	}
	
	public String getSelectedGender() {
		return this.selectedGender;
	}
 
	public void setSelectedGender(String selectedGender) {
		this.selectedGender = selectedGender;
	}
	
	public String getSelectedGenderDesc() {
		return this.selectedGenderDesc;
	}
 
	public void setSelectedGenderDesc(String selectedGenderDesc) {
		this.selectedGenderDesc = selectedGenderDesc;
	}
	
	public Map<String,String> getNamePrefixMap() {
		return this.namePrefixMap;
	}
 
	public void setNamePrefixMap(Map<String,String> namePrefixMap) {
		this.namePrefixMap = namePrefixMap;
	}
	
	public String getSelectedNamePrefix() {
		return this.selectedNamePrefix;
	}
 
	public void setSelectedNamePrefix(String selectedNamePrefix) {
		this.selectedNamePrefix = selectedNamePrefix;
	}
	
	public String getSelectedNamePrefixDesc() {
		return this.selectedNamePrefixDesc;
	}
 
	public void setSelectedNamePrefixDesc(String selectedNamePrefixDesc) {
		this.selectedNamePrefixDesc = selectedNamePrefixDesc;
	}
	
	public Map<String,String> getCountryMap() {
		return this.countryMap;
	}
 
	public void setCountryMap(Map<String,String> countryMap) {
		this.countryMap = countryMap;
	}
	
	public String getSelectedCountry() {
		return this.selectedCountry;
	}
 
	public void setSelectedCountry(String selectedCountry) {
		this.selectedCountry = selectedCountry;
	}
	
	public String getSelectedCountryDesc() {
		return this.selectedCountryDesc;
	}
 
	public void setSelectedCountryDesc(String selectedCountryDesc) {
		this.selectedCountryDesc = selectedCountryDesc;
	}
	
	public Map<String,String> getStateProvMap() {
		if (selectedCountry == null || (!selectedCountry.equals(CANADA) && !selectedCountry.equals(USA))){
			stateProvMap = new HashMap<String,String>();
			selectedStateProv = null;
			stateProvFlag = Boolean.FALSE;
		}
		else if (selectedCountry.equals(CANADA)) {
			stateProvMap = utilityService.createSpecialGUIDisplayMap(translations.getCanadaprovinces());
			stateProvFlag = Boolean.TRUE;
		}
		else if (selectedCountry.equals(USA)) {
			stateProvMap = utilityService.createSpecialGUIDisplayMap(translations.getUsastates());
			stateProvFlag = Boolean.TRUE;
		}
		return this.stateProvMap;
	}
 
	public void setStateProvMap(Map<String,String> stateProvMap) {
		this.stateProvMap = stateProvMap;
	}
	
	public String getSelectedStateProv() { 		
		return this.selectedStateProv;
	}
 
	public void setSelectedStateProv(String selectedStateProv) {
		this.selectedStateProv = selectedStateProv;
	}
	
	public String getSelectedStateProvDesc() { 		
		return this.selectedStateProvDesc;
	}
 
	public void setSelectedStateProvDesc(String selectedStateProvDesc) {
		this.selectedStateProvDesc = selectedStateProvDesc;
	}
	
	public Boolean getStateProvFlag() {
		return this.stateProvFlag;
	}
 
	public void setStateProvFlag(Boolean stateProvFlag) {
		this.stateProvFlag = stateProvFlag;
	}
	
	
	public void doClientEditChange() {
		currentScreenInfo.setClientEdit(currentScreenInfo.getClientEdit().booleanValue() ? Boolean.FALSE : Boolean.TRUE);
		clientEditDesc = currentScreenInfo.getClientEdit().booleanValue() ? EDIT : VIEW;
		clientTypeDesc = currentScreenInfo.istherePerson().booleanValue() ? PERSON_DET : BUSINESS_DET;
		if (currentScreenInfo.getClientEdit().booleanValue()) {
			if (personEditObj == null && businessEditObj == null) {
				if (currentScreenInfo.istherePerson()) {
					personEditObj = (Person) SerializationUtils.clone(currentScreenInfo.getPerson());
					clientEditObj = personEditObj;
					selectedMaritalStatus = personEditObj.getMaritalStatus();
					selectedNamePrefix = personEditObj.getNamePrefix();
					selectedGender = personEditObj.getGender();
					maritalStatusMap = utilityService.createStandardGUIDisplayMap(translations.getMaritalstatus());
					genderMap = utilityService.createStandardGUIDisplayMap(translations.getGenders());
				    namePrefixMap = utilityService.createStandardGUIDisplayMap(translations.getNameprefix());
				    businessEditObj = new Business();
				}
				else if (currentScreenInfo.isthereBusiness()) {
					businessEditObj = (Business) SerializationUtils.clone(currentScreenInfo.getBusiness());
					clientEditObj = businessEditObj;
					personEditObj = new Person();
				}
			}			
		}
		else {
			if (currentScreenInfo.istherePerson()) {
				selectedMaritalStatusDesc = translations.getMaritalstatus().get(currentScreenInfo.getPerson().getMaritalStatus());
				selectedNamePrefixDesc = translations.getNameprefix().get(currentScreenInfo.getPerson().getNamePrefix());
				selectedGenderDesc = translations.getGenders().get(currentScreenInfo.getPerson().getGender());
			}
			else if (currentScreenInfo.getClient() instanceof Business) {
				//Do Nothing
			}
		}
	}
	
	public void saveClientEdit() {
		if (currentScreenInfo.istherePerson()) {
			currentScreenInfo.getPerson().setFirstName(personEditObj.getFirstName());
			currentScreenInfo.getPerson().setMiddleName(personEditObj.getMiddleName());
			currentScreenInfo.getPerson().setLastName(personEditObj.getLastName());
			currentScreenInfo.getPerson().setNamePrefix(selectedNamePrefix);
			currentScreenInfo.getPerson().setGender(selectedGender);
			currentScreenInfo.getPerson().setDob(personEditObj.getDob());
			currentScreenInfo.getPerson().setMaritalStatus(selectedMaritalStatus);
			currentScreenInfo.getPerson().setSsn(personEditObj.getSsn());
			currentScreenInfo.getPerson().setEmail(clientEditObj.getEmail());
			currentScreenInfo.getPerson().setTelephone(clientEditObj.getTelephone());
			currentScreenInfo.getPerson().setCellPhone(clientEditObj.getCellPhone());
			currentScreenInfo.getPerson().setDtChanged(new Date());
			currentScreenInfo.getPerson().setUpdatedBy("SYSTEM");
			personDao.store(currentScreenInfo.getPerson());
			currentScreenInfo.setClient(currentScreenInfo.getPerson());
		}
		else if (currentScreenInfo.isthereBusiness()) {
			currentScreenInfo.getBusiness().setName(businessEditObj.getName());
			currentScreenInfo.getBusiness().setBusNo(businessEditObj.getBusNo());
			currentScreenInfo.getBusiness().setWebsiteUrl(businessEditObj.getWebsiteUrl());
			currentScreenInfo.getBusiness().setEmail(clientEditObj.getEmail());
			currentScreenInfo.getBusiness().setTelephone(clientEditObj.getTelephone());
			currentScreenInfo.getBusiness().setCellPhone(clientEditObj.getCellPhone());
			currentScreenInfo.getBusiness().setDtChanged(new Date());
			currentScreenInfo.getBusiness().setUpdatedBy("SYSTEM");
			businessDao.store(currentScreenInfo.getBusiness());
			currentScreenInfo.setClient(currentScreenInfo.getBusiness());
		}
		doClientEditChange();
	}
	
	public void doAddressEditChange() {
		currentScreenInfo.setAddressEdit(currentScreenInfo.getAddressEdit().booleanValue() ? Boolean.FALSE : Boolean.TRUE);
		addressEditDesc = currentScreenInfo.getAddressEdit().booleanValue() ? EDIT : VIEW;
		if (currentScreenInfo.getAddressEdit().booleanValue()) {
			if (addressEditObj == null && currentScreenInfo.isthereAddress()) {
				addressEditObj = (Address) SerializationUtils.clone(currentScreenInfo.getAddress());
				selectedCountry = addressEditObj.getCountry();
				selectedStateProv = addressEditObj.getStateProv();
				countryMap = utilityService.createSpecialGUIDisplayMap(translations.getCountries());
			}
			if (selectedCountry == null || (!selectedCountry.equals(CANADA) && !selectedCountry.equals(USA))){
				stateProvMap = new HashMap<String,String>();
				selectedStateProv = null;
				stateProvFlag = Boolean.FALSE;
			}
			else if (selectedCountry.equals(CANADA)) {
				stateProvMap = utilityService.createSpecialGUIDisplayMap(translations.getCanadaprovinces());
				stateProvFlag = Boolean.TRUE;
			}
			else if (selectedCountry.equals(USA)) {
				stateProvMap = utilityService.createSpecialGUIDisplayMap(translations.getUsastates());
				stateProvFlag = Boolean.TRUE;
			}
		}
		else {
			selectedCountryDesc = translations.getCountries().get(currentScreenInfo.getAddress().getCountry());
			if (currentScreenInfo.getAddress().getCountry().equals(CANADA)) {
				selectedStateProvDesc = translations.getCanadaprovinces().get(currentScreenInfo.getAddress().getStateProv());
			}
			else if (currentScreenInfo.getAddress().getCountry().equals(USA)) {
				selectedStateProvDesc = translations.getUsastates().get(currentScreenInfo.getAddress().getStateProv());
			}
			else {
				selectedStateProvDesc = "";
			}
		}
	}
	
	public void saveAddressEdit() {
		currentScreenInfo.getAddress().setStreet(addressEditObj.getStreet());
		currentScreenInfo.getAddress().setAptUnit(addressEditObj.getAptUnit());
		currentScreenInfo.getAddress().setCity(addressEditObj.getCity());
		currentScreenInfo.getAddress().setStateProv(selectedStateProv);
		currentScreenInfo.getAddress().setCountry(selectedCountry);
		currentScreenInfo.getAddress().setPostalZip(addressEditObj.getPostalZip());
		addressDao.store(currentScreenInfo.getAddress());
		doAddressEditChange();
	}
	
	public void validateDOB(FacesContext context, UIComponent componentToValidate, Object value)
            throws ValidatorException 
    {
		Date enteredDt = (Date) value;
		Date sysDt = new Date();
		if (!enteredDt.before(sysDt)) {
			FacesMessage message = new FacesMessage("Date of Birth must be before System Date (today)");
			throw new ValidatorException(message);
		}
		
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
	
	
}

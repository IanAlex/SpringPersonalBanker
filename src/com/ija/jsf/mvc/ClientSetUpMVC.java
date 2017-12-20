package com.ija.jsf.mvc;

import java.util.*;
import java.text.SimpleDateFormat;


import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.faces.component.UIComponent;

import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ija.pb.hibernate.tables.*;
import com.ija.pb.hibernate.dao.*;
import com.ija.pb.nondaodata.*;
import com.ija.pb.services.*;

@Component
@ManagedBean
@ViewScoped
public class ClientSetUpMVC implements Serializable {
	
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
	@Autowired
	ClientEditViewSettingsMVC cevMVC;
	@Autowired
	TabNavigator tabNavigator;
	
	private static final String PERSON = "P";
	private static final String BUSINESS = "B";
	
	private static final String CANADA = "CA";
	private static final String USA = "US";
	
	private Map<String,String> clientTypesMap;
	private Map<String,String> maritalStatusMap;
	private Map<String,String> genderMap;
	private Map<String,String> namePrefixMap;
	private Map<String,String> countryMap;
	private Map<String,String> stateProvMap;
	private String oldselectedClientType;
	private String selectedClientType;
	private String selectedMaritalStatus;
	private String selectedNamePrefix;
	private String selectedGender;
	private String selectedCountry;
	private String selectedStateProv;
	private String selectedClientTypeDesc;
	private String selectedMaritalStatusDesc;
	private String selectedNamePrefixDesc;
	private String selectedGenderDesc;
	private String selectedCountryDesc;
	private String selectedStateProvDesc;
	private Boolean stateProvFlag;
	
	
	public ClientSetUpMVC() {
	}
	
	@PostConstruct
	public void init() {
		clientTypesMap = utilityService.createStandardGUIDisplayMap(translations.getClienttypes());
		selectedClientType = PERSON;
		oldselectedClientType = "";
		clientTypesMap = utilityService.createStandardGUIDisplayMap(translations.getClienttypes());
		maritalStatusMap = utilityService.createStandardGUIDisplayMap(translations.getMaritalstatus());
		genderMap = utilityService.createStandardGUIDisplayMap(translations.getGenders());
	    namePrefixMap = utilityService.createStandardGUIDisplayMap(translations.getNameprefix());
		countryMap = utilityService.createSpecialGUIDisplayMap(translations.getCountries());
		stateProvFlag = Boolean.TRUE;
		currentScreenInfo.setWizClient(null);
		currentScreenInfo.setWizPerson(null);
		currentScreenInfo.setWizBusiness(null);
		currentScreenInfo.setWizAddress(new Address());
		currentScreenInfo.getWizAddress().setDtCreated(new Date());
		currentScreenInfo.getWizAddress().setDtChanged(new Date());
		currentScreenInfo.getWizAddress().setUpdatedBy("SYSTEM");
	}
	
	public CurrentScreenInfo getCurrentScreenInfo() {
		return this.currentScreenInfo;
	}
 
	public void setCurrentScreenInfo(CurrentScreenInfo currentScreenInfo) {
		this.currentScreenInfo = currentScreenInfo;
	}
	
	public Map<String,String> getClientTypesMap() {
		return this.clientTypesMap;
	}
 
	public void setClientTypesMap(Map<String,String> clientTypesMap) {
		this.clientTypesMap = clientTypesMap;
	}
	
	public String getSelectedClientType() {
		return this.selectedClientType;
	}
 
	public void setSelectedClientType(String selectedClientType) {
		this.selectedClientType = selectedClientType;
	}
	
	public String getSelectedClientTypeDesc() {
		return this.selectedClientTypeDesc;
	}
 
	public void setSelectedClientTypeDesc(String selectedClientTypeDesc) {
		this.selectedClientTypeDesc = selectedClientTypeDesc;
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
	
	public void clearfields() {		
		currentScreenInfo.setWizClient(null);
		currentScreenInfo.setWizPerson(null);
		currentScreenInfo.setWizBusiness(null);	
		selectedMaritalStatus = null;
		selectedNamePrefix = null;
		selectedGender = null;
	}
	
	public String onFlowProcess(FlowEvent event) {
		String currentWizScreen = event.getOldStep();
		String nextWizScreen = event.getNewStep();
		if (nextWizScreen.equals("enterAddressInfo")) {
			//do Nothing
		}
		else if (nextWizScreen.equals("chooseClientType") && currentWizScreen.equals("enterAddressInfo")) {	
			currentScreenInfo.getWizAddress().setCountry(selectedCountry);
			currentScreenInfo.getWizAddress().setStateProv(selectedStateProv);			
		}
		else if (nextWizScreen.equals("enterClientInfo") && currentWizScreen.equals("chooseClientType")) {	
			if (!selectedClientType.equals(oldselectedClientType)) {
				clearfields();
				Login login = loginDao.findForLoginKey(currentScreenInfo.getLoginKey());
				if (selectedClientType.equals(PERSON)) {
					Person person = new Person();
					person.setClientType(PERSON);
					person.setLogin(login);
					currentScreenInfo.setWizPerson(person);
					currentScreenInfo.setWizClient(person);
				}
				else if (selectedClientType.equals(BUSINESS)) {
					Business business = new Business();
					business.setClientType(BUSINESS);
					business.setLogin(login);
					currentScreenInfo.setWizBusiness(business);
					currentScreenInfo.setWizClient(business);
				}
				currentScreenInfo.getWizClient().setLogin(login);
				currentScreenInfo.getWizClient().setDtChanged(new Date());
				currentScreenInfo.getWizClient().setDtCreated(new Date());
				currentScreenInfo.getWizClient().setUpdatedBy("SYSTEM");
				oldselectedClientType = selectedClientType;
			}
		}
		else if (nextWizScreen.equals("newClientConfirm") && currentWizScreen.equals("enterClientInfo")) {
			selectedClientTypeDesc = translations.getClienttypes().get(selectedClientType);
			selectedCountryDesc = translations.getCountries().get(selectedCountry);
			if (stateProvFlag.booleanValue()) {
				if (selectedCountry.equals(CANADA)) {
					selectedStateProvDesc = translations.getCanadaprovinces().get(selectedStateProv);
				}
				else if (selectedCountry.equals(CANADA)) {
					selectedStateProvDesc = translations.getUsastates().get(selectedStateProv);
				}
			}
			if (selectedClientType.equals(PERSON)) {
				selectedMaritalStatusDesc = translations.getMaritalstatus().get(selectedMaritalStatus);
				selectedNamePrefixDesc = translations.getNameprefix().get(selectedNamePrefix);
				selectedGenderDesc = translations.getGenders().get(selectedGender);
			}
		}
		return nextWizScreen;
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
	
	public String createNewClient() {
		String retStr = null;
		if (currentScreenInfo.getWizAddress() != null && (currentScreenInfo.isthereWizPerson() || currentScreenInfo.isthereWizBusiness() )) {
			Address address = currentScreenInfo.getWizAddress();
			addressDao.store(address);
			if (address.getAddressId() != null) {
				currentScreenInfo.setAddress(address);
				if (currentScreenInfo.isthereWizPerson()) {
					Person person = currentScreenInfo.getWizPerson();
					person.setAddress(address);
					person.setGender(selectedGender);
					person.setNamePrefix(selectedNamePrefix);
					person.setMaritalStatus(selectedMaritalStatus);
					personDao.store(person);
					if (person.getClientId() != null) {
						currentScreenInfo.setPerson(person);
						currentScreenInfo.setClient(person);
						currentScreenInfo.setAddress(address);
						cevMVC.init();
						tabNavigator.init();
						tabNavigator.setActiveTabIndex(-1);  // no tab intially selected
						retStr = "main.xhtml?faces-redirect=true";
					}
				}
				else if (currentScreenInfo.isthereWizBusiness()) {
					Business business = currentScreenInfo.getWizBusiness();
					business.setAddress(address);
					businessDao.store(business);
					if (business.getClientId() != null) {
						currentScreenInfo.setBusiness(business);
						currentScreenInfo.setClient(business);
						currentScreenInfo.setAddress(address);
						cevMVC.init();
						tabNavigator.init();
						tabNavigator.setActiveTabIndex(-1);  // no tab intially selected
						retStr = "main.xhtml?faces-redirect=true";
					}
				}
			}
		}
		return retStr;
	}

}

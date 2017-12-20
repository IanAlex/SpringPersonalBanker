package com.ija.pb.nondaodata;

import java.util.*;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class Translations {
	
	public static final String ORIGINAL = "Original";
	public static final String REVERSED = "Reversed";
	
	private Map<String, String> genders;
	private Map<String, String> nameprefix;
	private Map<String, String> maritalstatus;
	private Map<String, String> clienttypes;
	private Map<String, String> accounttypes;
	private Map<String, String> stocktxntypes;
	private Map<String, String> gictxntypes;
	private Map<String, String> annuitytxntypes;
	private Map<String, String> savingstxntypes;
	private Map<String, String> chequingtxntypes;
	private Map<String, String> annuitypayfreqcodes;
	private Map<String, String> intcompoundfreqcodes;
	private Map<String, String> gicintcompoundfreqcodes;
	private Map<String, String> gictermlengthcodes;
	private Map<String, String> potentialusertypes;
	private Map<String, String> drcr;
	private Map<String, String> countries;
	private Map<String, String> usastates;
	private Map<String, String> canadaprovinces;
	
	public Translations() {
	}
	
    @PostConstruct
	public void init() {
    	genders = new HashMap<String, String>();
    	genders.put("M", "Male");
		genders.put("F", "Female");
		
		nameprefix = new HashMap<String, String>();
		nameprefix.put("Mr", "Mr.");
		nameprefix.put("Mi", "Miss");
		nameprefix.put("Ms", "Ms.");
		nameprefix.put("Si", "Sir");
		nameprefix.put("Ma", "Master");
		nameprefix.put("Dr", "Dr.");
		nameprefix.put("Pr", "Professor");
		
		maritalstatus = new HashMap<String, String>();
		maritalstatus.put("SI", "Single");
		maritalstatus.put("MA", "Married");
		maritalstatus.put("CL", "Common Law");
		maritalstatus.put("SE", "Separated");
		maritalstatus.put("DV", "Divorced");
		maritalstatus.put("WI", "Widowed");
		
		clienttypes = new HashMap<String, String>();
		clienttypes.put("P", "Person");
		clienttypes.put("B", "Business");
		
		accounttypes = new HashMap<String, String>();
		accounttypes.put("ST", "Stock");
		accounttypes.put("GC", "GIC/Term Deposit");
		accounttypes.put("AN", "Annuity");
		accounttypes.put("CH", "Chequing");
		accounttypes.put("SV", "Savings");
		
		stocktxntypes = new TreeMap<String, String>();
		stocktxntypes.put("O", "Open-Buy");
		stocktxntypes.put("B", "Buy");
		stocktxntypes.put("S", "Sell");
		
		
		gictxntypes = new TreeMap<String, String>();
		gictxntypes.put("O", "Open-Purchase");
		gictxntypes.put("W", "Funds Out");
		gictxntypes.put("M", "Maturity");
		
		annuitytxntypes = new TreeMap<String, String>();
		annuitytxntypes.put("O", "Open-Buy");
		annuitytxntypes.put("P", "Payment");
		annuitytxntypes.put("S", "Sell");
		
		savingstxntypes = new TreeMap<String, String>();
		savingstxntypes.put("O", "Open-Funds In");
		savingstxntypes.put("D", "Deposit");
		savingstxntypes.put("W", "Withdrawal");
		savingstxntypes.put("TI", "Transfer In");
		savingstxntypes.put("TO", "Transfer Out");
		savingstxntypes.put("I", "Interest");
		
		chequingtxntypes = new TreeMap<String, String>();
		chequingtxntypes.put("O", "Open-Funds In");
		chequingtxntypes.put("D", "Deposit");
		chequingtxntypes.put("W", "Withdrawal");
		chequingtxntypes.put("TI", "Transfer In");
		chequingtxntypes.put("TO", "Transfer Out");
		chequingtxntypes.put("C", "Issue Cheque");
		
		annuitypayfreqcodes = new HashMap<String, String>();
		annuitypayfreqcodes.put("12", "Monthly");
		annuitypayfreqcodes.put("6", "Bi-Monthly");
		annuitypayfreqcodes.put("4", "Quarterly");
		annuitypayfreqcodes.put("2", "Semi Annual");
		annuitypayfreqcodes.put("1", "Annual");
		
		intcompoundfreqcodes = new HashMap<String, String>();
		intcompoundfreqcodes.put("365", "Daily");
		intcompoundfreqcodes.put("52", "Weekly");
		intcompoundfreqcodes.put("12", "Monthly");
		intcompoundfreqcodes.put("6", "Bi-Monthly");
		intcompoundfreqcodes.put("4", "Quarterly");
		intcompoundfreqcodes.put("2", "Semi Annual");
		intcompoundfreqcodes.put("1", "Annual");
		
		gicintcompoundfreqcodes = new HashMap<String, String>();
		gicintcompoundfreqcodes.put("365", "Daily");
		gicintcompoundfreqcodes.put("52", "Weekly");
		gicintcompoundfreqcodes.put("12", "Monthly");
		gicintcompoundfreqcodes.put("6", "Bi-Monthly");
		gicintcompoundfreqcodes.put("4", "Quarterly");
		gicintcompoundfreqcodes.put("2", "Semi Annual");
		gicintcompoundfreqcodes.put("1", "Annual");
		
		gictermlengthcodes = new HashMap<String, String>();
		gictermlengthcodes.put("D", "Days");
		gictermlengthcodes.put("W", "Weeks");
		gictermlengthcodes.put("M", "Months");
		gictermlengthcodes.put("Y", "Years");
		
		potentialusertypes = new TreeMap<String, String>();
		potentialusertypes.put("PC", "Potential Client for Software Programming");
		potentialusertypes.put("PE", "Poptential Employer");
		potentialusertypes.put("AG", "Agency/Recruiter");
		potentialusertypes.put("FA",  "Friend/Acquaintance");
		
		drcr = new HashMap<String, String>();
		drcr.put("D", "Debit");
		drcr.put("C", "Credit");
		
		countries = new TreeMap<String, String>();
		countries.put("AD","ANDORRA" );
		countries.put("AE","UNITED ARAB EMIRATES" );
		countries.put("AF","AFGHANISTAN" );
		countries.put("AG","ANTIGUA AND BARBUDA" );
		countries.put("AI","ANGUILLA" );
		countries.put("AL","ALBANIA" );
		countries.put("AM","ARMENIA" );
		countries.put("AO","ANGOLA" );
		countries.put("AQ","ANTARCTICA" );
		countries.put("AR","ARGENTINA" );
		countries.put("AS","AMERICAN SAMOA" );
		countries.put("AT","AUSTRIA" );
		countries.put("AU","AUSTRALIA" );
		countries.put("AW","ARUBA" );
		countries.put("AX","ÅLAND ISLANDS" );
		countries.put("AZ","AZERBAIJAN");
		countries.put("BA","BOSNIA AND HERZEGOVINA" );
		countries.put("BB","BARBADOS" );
		countries.put("BD","BANGLADESH" );
		countries.put("BE","BELGIUM" );
		countries.put("BF","BURKINA FASO" );
		countries.put("BG","BULGARIA" );
		countries.put("BH","BAHRAIN" );
		countries.put("BI","BURUNDI" );
		countries.put("BJ","BENIN" );
		countries.put("BL","SAINT BARTHÉLEMY" );
		countries.put("BM","BERMUDA" );
		countries.put("BN","BRUNEI DARUSSALAM" );
		countries.put("BO","BOLIVIA PLURINATIONAL STATE OF" );
		countries.put("BQ","BONAIRE SINT EUSTATIUS AND SABA" );
		countries.put("BR","BRAZIL" );
		countries.put("BS","BAHAMAS" );
		countries.put("BT","BHUTAN" );
		countries.put("BV","BOUVET ISLAND" );
		countries.put("BW","BOTSWANA" );
		countries.put("BY","BELARUS" );
		countries.put("BZ","BELIZE" );
		countries.put("CA","CANADA" );
		countries.put("CC","COCOS (KEELING) ISLANDS" );
		countries.put("CD","CONGO THE DEMOCRATIC REPUBLIC OF THE" );
		countries.put("CF","CENTRAL AFRICAN REPUBLIC" );
		countries.put("CG","CONGO" );
		countries.put("CH","SWITZERLAND" );
		countries.put("CI","CÔTE D'IVOIRE" );
		countries.put("CK","COOK ISLANDS" );
		countries.put("CL","CHILE" );
		countries.put("CM","CAMEROON" );
		countries.put("CN","CHINA" );
		countries.put("CO","COLOMBIA" );
		countries.put("CR","COSTA RICA" );
		countries.put("CU","CUBA" );
		countries.put("CV","CAPE VERDE" );
		countries.put("CW","CURAÇAO" );
		countries.put("CX","CHRISTMAS ISLAND" );
		countries.put("CY","CYPRUS" );
		countries.put("CZ","CZECH REPUBLIC" );
		countries.put("DE","GERMANY" );
		countries.put("DJ","DJIBOUTI" );
		countries.put("DK","DENMARK" );
		countries.put("DM","DOMINICA" );
		countries.put("DO","DOMINICAN REPUBLIC" );
		countries.put("DZ","ALGERIA" );
		countries.put("EC","ECUADOR" );
		countries.put("EE","ESTONIA" );
		countries.put("EG","EGYPT" );
		countries.put("EH","WESTERN SAHARA" );
		countries.put("ER","ERITREA" );
		countries.put("ES","SPAIN" );
		countries.put("ET","ETHIOPIA" );
		countries.put("FI","FINLAND" );
		countries.put("FJ","FIJI" );
		countries.put("FK","FALKLAND ISLANDS (MALVINAS)" );
		countries.put("FM","MICRONESIA FEDERATED STATES OF" );
		countries.put("FO","FAROE ISLANDS" );
		countries.put("FR","FRANCE" );
		countries.put("GA","GABON" );
		countries.put("GB","UNITED KINGDOM" );
		countries.put("GD","GRENADA" );
		countries.put("GD","GRENADA" );
		countries.put("GF","FRENCH GUIANA" );
		countries.put("GG","GUERNSEY" );
		countries.put("GH","GHANA" );
		countries.put("GI","GIBRALTAR" );
		countries.put("GL","GREENLAND" );
		countries.put("GM","GAMBIA" );
		countries.put("GN","GUINEA" );
		countries.put("GP","GUADELOUPE" );
		countries.put("GQ","EQUATORIAL GUINEA" );
		countries.put("GR","GREECE" );
		countries.put("GS","SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS" );
		countries.put("GT","GUATEMALA" );
		countries.put("GU","GUAM" );
		countries.put("GW","GUINEA-BISSAU" );
		countries.put("GY","GUYANA" );
		countries.put("HK","HONG KONG" );
		countries.put("HM","HEARD ISLAND AND MCDONALD ISLANDS" );
		countries.put("HN","HONDURAS" );
		countries.put("HR","CROATIA" );
		countries.put("HT","HAITI" );
		countries.put("HU","HUNGARY" );
		countries.put("ID","INDONESIA" );
		countries.put("IE","IRELAND" );
		countries.put("IL","ISRAEL" );
		countries.put("IM","ISLE OF MAN" );
		countries.put("IN","INDIA" );
		countries.put("IO","BRITISH INDIAN OCEAN TERRITORY" );
		countries.put("IQ","IRAQ" );
		countries.put("IR","IRAN ISLAMIC REPUBLIC OF" );
		countries.put("IS","ICELAND" );
		countries.put("IT","ITALY" );
		countries.put("JE","JERSEY" );
		countries.put("JM","JAMAICA" );
		countries.put("JO","JORDAN" );
		countries.put("JP","JAPAN" );
		countries.put("KE","KENYA" );
		countries.put("KG","KYRGYZSTAN" );
		countries.put("KH","CAMBODIA" );
		countries.put("KI","KIRIBATI" );
		countries.put("KM","COMOROS" );
		countries.put("KN","SAINT KITTS AND NEVIS" );
		countries.put("KP","KOREA DEMOCRATIC PEOPLES REPUBLIC OF" );
		countries.put("KR","KOREA REPUBLIC OF" );
		countries.put("KW","KUWAIT" );
		countries.put("KY","CAYMAN ISLANDS" );
		countries.put("KZ","KAZAKHSTAN" );
		countries.put("LA","LAOS PEOPLES DEMOCRATIC REPUBLIC" );
		countries.put("LB","LEBANON" );
		countries.put("LC","SAINT LUCIA" );
		countries.put("LI","LIECHTENSTEIN" );
		countries.put("LK","SRI LANKA" );
		countries.put("LR","LIBERIA" );
		countries.put("LS","LESOTHO" );
		countries.put("LT","LITHUANIA" );
		countries.put("LU","LUXEMBOURG" );
		countries.put("LV","LATVIA" );
		countries.put("LY","LIBYA" );
		countries.put("MA","MOROCCO" );
		countries.put("MC","MONACO" );
		countries.put("MD","MOLDOVA REPUBLIC OF" );
		countries.put("ME","MONTENEGRO" );
		countries.put("MF","SAINT MARTIN (FRENCH PART)" );
		countries.put("MG","MADAGASCAR" );
		countries.put("MH","MARSHALL ISLANDS" );
		countries.put("MK","MACEDONIA THE FORMER YUGOSLAV REPUBLIC OF" );
		countries.put("ML","MALI" );
		countries.put("MM","MYANMAR" );
		countries.put("MN","MONGOLIA" );
		countries.put("MO","MACAO" );
		countries.put("MP","NORTHERN MARIANA ISLANDS" );
		countries.put("MQ","MARTINIQUE" );
		countries.put("MR","MAURITANIA" );
		countries.put("MS","MONTSERRAT" );
		countries.put("MT","MALTA" );
		countries.put("MU","MAURITIUS" );
		countries.put("MV","MALDIVES" );
		countries.put("MW","MALAWI" );
		countries.put("MX","MEXICO" );
		countries.put("MY","MALAYSIA" );
		countries.put("MZ","MOZAMBIQUE" );
		countries.put("NA","NAMIBIA" );
		countries.put("NC","NEW CALEDONIA" );
		countries.put("NE","NIGER" );
		countries.put("NF","NORFOLK ISLAND" );
		countries.put("NG","NIGERIA" );
		countries.put("NI","NICARAGUA" );
		countries.put("NL","NETHERLANDS" );		
		countries.put("NO","NORWAY" );
		countries.put("NP","NEPAL" );
		countries.put("NR","NAURU" );
		countries.put("NU","NIUE" );
		countries.put("NZ","NEW ZEALAND" );
		countries.put("OM","OMAN" );
		countries.put("PA","PANAMA"  );
		countries.put("PE","PERU"  );
		countries.put("PF","FRENCH POLYNESIA"  );
		countries.put("PG","PAPUA NEW GUINEA"  );
		countries.put("PH","PHILIPPINES"  );
		countries.put("PK","PAKISTAN"  );
		countries.put("PL","POLAND"  );
		countries.put("PM","SAINT PIERRE AND MIQUELON"  );
		countries.put("PN","PITCAIRN"  );
		countries.put("PR","PUERTO RICO"  );
		countries.put("PS","PALESTINE STATE OF"  );
		countries.put("PT","PORTUGAL"  );
		countries.put("PW","PALAU"  );
		countries.put("PY","PARAGUAY"  );
		countries.put("QA","QATAR"  );
		countries.put("RE","RÉUNION"  );
		countries.put("RO","ROMANIA"  );
		countries.put("RS","SERBIA"  );
		countries.put("RU","RUSSIAN FEDERATION"  );
		countries.put("RW","RWANDA"  );
		countries.put("SA","SAUDI ARABIA"  );
		countries.put("SB","SOLOMON ISLANDS"  );
		countries.put("SC","SEYCHELLES"  );
		countries.put("SD","SUDAN"  );
		countries.put("SE","SWEDEN"  );
		countries.put("SG","SINGAPORE"  );
		countries.put("SH","SAINT HELENA ASCENSION AND TRISTAN DA CUNHA"  );
		countries.put("SI", "SLOVENIA"  );
		countries.put("SJ","SVALBARD AND JAN MAYEN"   );
		countries.put("SK","SLOVAKIA"  );
		countries.put("SL","SIERRA LEONE"  );
		countries.put("SM","SAN MARINO"  );
		countries.put("SN","SENEGAL"  );
		countries.put("SO","SOMALIA"  );
		countries.put("SR","SURINAME"  );
		countries.put("SS","SOUTH SUDAN"  );
		countries.put("ST","SAO TOME AND PRINCIPE"  );
		countries.put("SV","EL SALVADOR"  );
		countries.put("SX","SINT MAARTEN (DUTCH PART)"  );
		countries.put("SY","SYRIAN ARAB REPUBLIC"  );
		countries.put("SZ","SWAZILAND" );
		countries.put("TC","TURKS AND CAICOS ISLANDS"  );
		countries.put("TD","CHAD"  );
		countries.put("TF","FRENCH SOUTHERN TERRITORIES"  );
		countries.put("TG","TOGO"  );
		countries.put("TH","THAILAND"  );
		countries.put("TJ","TAJIKISTAN"  );
		countries.put("TK","TOKELAU"  );
		countries.put("TL","TIMOR-LESTE"  );
		countries.put("TM","TURKMENISTAN"  );
		countries.put("TN","TUNISIA"  );
		countries.put("TO","TONGA"  );
		countries.put("TR","TURKEY"  );
		countries.put("TT","TRINIDAD AND TOBAGO"  );
		countries.put("TV","TUVALU"  );
		countries.put("TW","TAIWAN PROVINCE OF CHINA"  );
		countries.put("TZ","TANZANIA UNITED REPUBLIC OF"  );
		countries.put("UA","UKRAINE"  );
		countries.put("UG","UGANDA"  );
		countries.put("UM","UNITED STATES MINOR OUTLYING ISLANDS"  );
		countries.put("US","UNITED STATES"  );
		countries.put("UY","URUGUAY"  );
		countries.put("UZ","UZBEKISTAN"  );
		countries.put("VA","HOLY SEE (VATICAN CITY STATE)"  );
		countries.put("VC","SAINT VINCENT AND THE GRENADINES"  );
		countries.put("VE","VENEZUELA BOLIVARIAN REPUBLIC OF"  );
		countries.put("VG","VIRGIN ISLANDS BRITISH"  );
		countries.put("VI","VIRGIN ISLANDS U.S."  );
		countries.put("VN","VIET NAM"  );
		countries.put("VU","VANUATU"  );
		countries.put("WF","WALLIS AND FUTUNA"  );
		countries.put("WS","SAMOA"  );
		countries.put("YE","YEMEN"  );
		countries.put("YT","MAYOTTE"  );
		countries.put("ZA","SOUTH AFRICA"  );
		countries.put("ZM","ZAMBIA"  );
		countries.put("ZW","ZIMBABWE"  );
		
		usastates = new TreeMap<String, String>();
		usastates.put("AK","Alaska" );
		usastates.put("AK","Alaska" );
		usastates.put("AR","Arkansas"  );
		usastates.put("AZ","Arizona" );
		usastates.put("CA","California" );
		usastates.put("CO","Colorado" );
		usastates.put("CT","Connecticut" );
		usastates.put("DC","Dist of Columbia" );
		usastates.put("DE","Delaware"  );
		usastates.put("FL","Florida" );
		usastates.put("GA","Georgia"  );
		usastates.put("GU","Guam" );
		usastates.put("HI","Hawaii"  );
		usastates.put("IA","Iowa"  );
		usastates.put("ID","Idaho"  );
		usastates.put("IL","Illinois"   );
		usastates.put("IN","Indiana"  );
		usastates.put("KS","Kansas" );
		usastates.put("KY","Kentucky"     );
		usastates.put("LA","Louisiana" );
		usastates.put("MA","Massachusetts" );
		usastates.put("MD","Maryland" );
		usastates.put("ME","Maine" );
		usastates.put("MI","Michigan"  );
		usastates.put("MN","Minnesota" );
		usastates.put("MO","Missouri" );
		usastates.put("MS","Mississippi" );
		usastates.put("MT","Montana"  );
		usastates.put("NC","North Carolina" );
		usastates.put("ND","North Dakota" );
		usastates.put("NE","Nebraska"  );
		usastates.put("NH","New Hampshire"  );
		usastates.put("NJ","New Jersey" );
		usastates.put("NM","New Mexico"   );
		usastates.put("NV","Nevada"  );
		usastates.put("NY","New York" );
		usastates.put("OH","Ohio" );
		usastates.put("OK","Oklahoma" );
		usastates.put("OR","Oregon" );
		usastates.put("PA","Pennsylvania" );
		usastates.put("PR","Puerto Rico" );
		usastates.put("RI","Rhode Island" );
		usastates.put("SC","South Carolina" );
		usastates.put("SD","South Dakota" );
		usastates.put("TN","Tennessee" );
		usastates.put("TX","Texas"  );
		usastates.put("TX","Texas"  );
		usastates.put("UT","Utah" );
		usastates.put("VA","Virginia" );
		usastates.put("VI","Virgin Islands" );
		usastates.put("VT","Vermont" );
		usastates.put("WA","Washington" );
		usastates.put("WI","Wisconsin" );
		usastates.put("WV","West Virginia"  );
		usastates.put("WY","Wyoming"  );
		
		canadaprovinces = new TreeMap<String, String>();
		canadaprovinces.put("AB","Alberta"  );
		canadaprovinces.put("BC","British Columbia"  );
		canadaprovinces.put("MB","Manitoba"  );
		canadaprovinces.put("NB","New Brunswick"  );
		canadaprovinces.put("NL","Newfoundland and Labrador"  );
		canadaprovinces.put("NS","Nova Scotia"  );
		canadaprovinces.put("NT","Northwest Territories"  );
		canadaprovinces.put("NU","Nunavut"  );
		canadaprovinces.put("ON","Ontario"  );
		canadaprovinces.put("PE","Prince Edward Island"  );
		canadaprovinces.put("QC","Quebec"  );
		canadaprovinces.put("SK","Saskatchewan"  );
		canadaprovinces.put("YT","Yukon"  );
		
    }
	
	public void setGenders(Map<String, String> genders) {
		this.genders = genders;
	}
	
	public Map<String, String> getGenders() {
		return this.genders;
	}
 
	public void setNameprefix(Map<String, String> nameprefix) {
		this.nameprefix = nameprefix;
	}
	
	public Map<String, String> getNameprefix() {
		return this.nameprefix;
	}
 
	public void setMaritalstatus(Map<String, String> maritalstatus) {
		this.maritalstatus = maritalstatus;
	}
	
	public Map<String, String> getMaritalstatus() {
		return this.maritalstatus;
	}
 
	public void setClienttypes(Map<String, String> clienttypes) {
		this.clienttypes = clienttypes;
	}
	
	public Map<String, String> getClienttypes() {
		return this.clienttypes;
	}
 
	public void setAccounttypes(Map<String, String> accounttypes) {
		this.accounttypes = accounttypes;
	}
	
	public Map<String, String> getAccounttypes() {
		return this.accounttypes;
	}
 
	public void setStocktxntypes(Map<String, String> stocktxntypes) {
		this.stocktxntypes = stocktxntypes;
	}
	
	public Map<String, String> getStocktxntypes() {
		return this.stocktxntypes;
	}
 
	public void setGictxntypes(Map<String, String> gictxntypes) {
		this.gictxntypes = gictxntypes;
	}
	
	public Map<String, String> getGictxntypes() {
		return this.gictxntypes;
	}
 
	public void setAnnuitytxntypes(Map<String, String> annuitytxntypes) {
		this.annuitytxntypes = annuitytxntypes;
	}
	
	public Map<String, String> getAnnuitytxntypes() {
		return this.annuitytxntypes;
	}
 
	public void setSavingstxntypes(Map<String, String> savingstxntypes) {
		this.savingstxntypes = savingstxntypes;
	}
	
	public Map<String, String> getSavingstxntypes() {
		return this.savingstxntypes;
	}
 
	public void setChequingtxntypes(Map<String, String> chequingtxntypes) {
		this.chequingtxntypes = chequingtxntypes;
	}
	
	public Map<String, String> getChequingtxntypes() {
		return this.chequingtxntypes;
	}
	
	public void setAnnuitypayfreqcodes(Map<String, String> annuitypayfreqcodes) {
		this.annuitypayfreqcodes = annuitypayfreqcodes;
	}
	
	public Map<String, String> getAnnuitypayfreqcodes() {
		return this.annuitypayfreqcodes;
	}
	
	public void setIntcompoundfreqcodes(Map<String, String> annuityintcompoundfreqcodes) {
		this.intcompoundfreqcodes = intcompoundfreqcodes;
	}
	
	public Map<String, String> getIntcompoundfreqcodes() {
		return this.intcompoundfreqcodes;
	}
 
	public void setDrcr(Map<String, String> drcr) {
		this.drcr = drcr;
	}
	
	public Map<String, String> getPotentialusertypes() {
		return this.potentialusertypes;
	}
	
	public void setPotentialusertypes(Map<String, String> potentialusertypes) {
		this.potentialusertypes = potentialusertypes;
	}
	
	public Map<String, String> getDrcr() {
		return this.drcr;
	}
 
	public void setCountries(Map<String, String> countries) {
		this.countries = countries;
	}
	
	public Map<String, String> getCountries() {
		return this.countries;
	}
 
	public void setUsastates(Map<String, String> usastates) {
		this.usastates = usastates;
	}
	
	public Map<String, String> getUsastates() {
		return this.usastates;
	}
 
	public void setCanadaprovinces(Map<String, String> canadaprovinces) {
		this.canadaprovinces = canadaprovinces;
	}
	
	public Map<String, String> getCanadaprovinces() {
		return this.canadaprovinces;
	}
 
	

}

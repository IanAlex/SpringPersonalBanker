package com.ija.pb.services;

import java.util.*;

public interface UtilityService {
	
	// convert Map<key, desc> to LinkedHashMap<desc, value(key)> for JSF2 selectOne control
	Map<String, String> createStandardGUIDisplayMap(Map<String, String> inMap);
	Map<String, Integer> createStandardGUIDisplayMapWithInt(Map<Integer, String> inMap);
	// convert Map<key, desc> to LinkedHashMap<value(key): desc, value(key)> 
	//for JSF2 selectOne control
	Map<String, String> createSpecialGUIDisplayMap(Map<String, String> inMap);	
	Double round(Double value, int places);
	String removeQuotes(String inStr);
	boolean determineEmailValid(String emailStr);
	void sendEmail (String fromAddress, String toAddress, String subject,
			String messageText, String userName, String password );
}

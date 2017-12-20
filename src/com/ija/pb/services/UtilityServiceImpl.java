package com.ija.pb.services;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class UtilityServiceImpl implements UtilityService {

	
	@Override
	// convert Map<key, desc> to LinkedHashMap<desc, value(key)> for JSF2 selectOne control
	public Map<String, String> createStandardGUIDisplayMap(Map<String, String> inMap) {
		if (inMap != null && !inMap.isEmpty()) {
			Map<String, String> guiMap = new LinkedHashMap<String, String>();
			for(Map.Entry<String,String> mapEntry : inMap.entrySet()) {
				guiMap.put(mapEntry.getValue(),  mapEntry.getKey());
			}	
			return guiMap;
		}
		else {
			return null;
		}
	}
	
	@Override
	// convert Map<key, desc> to LinkedHashMap<desc, value(key)> for JSF2 selectOne control
		public Map<String, Integer> createStandardGUIDisplayMapWithInt(Map<Integer, String> inMap) {
			if (inMap != null && !inMap.isEmpty()) {
				Map<String, Integer> guiMap = new LinkedHashMap<String, Integer>();
				for(Map.Entry<Integer,String> mapEntry : inMap.entrySet()) {
					guiMap.put(mapEntry.getValue(),  mapEntry.getKey());
				}	
				return guiMap;
			}
			else {
				return null;
			}
		}

	@Override
	// convert Map<key, desc> to LinkedHashMap<value(key): desc, value(key)> 
	//for JSF2 selectOne control
	public Map<String, String> createSpecialGUIDisplayMap(Map<String, String> inMap) {
		if (inMap != null && !inMap.isEmpty()) {
			Map<String, String> guiMap = new LinkedHashMap<String, String>();
			for(Map.Entry<String,String> mapEntry : inMap.entrySet()) {
				guiMap.put(mapEntry.getKey() + ":  " + mapEntry.getValue(),  mapEntry.getKey());
			}	
			return guiMap;
		}
		else {
			return null;
		}
	}
	
	@Override
	//round a double to desired dec places
	public Double round(Double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
	    long factor = (long) Math.pow(10, places);
	    if (value != null) {
	    	long tmp = Math.round(value.doubleValue() * factor);
	    	return new Double ( (double) tmp / factor);
	    }
	    return new Double(0.0);  
	    
	}
	
	@Override
	public String removeQuotes(String inStr) {
		if (inStr == null || inStr.trim().equals("")) {
			return inStr;
		}
		boolean quoteFlag = true;
		StringBuffer wk = new StringBuffer(inStr);
		while (quoteFlag) {
			if (wk.lastIndexOf("\"") >= 0) {
				int idx = wk.lastIndexOf("\"");
				wk = wk.replace(idx, idx + 1, "");
			}
			else {
				quoteFlag = false;
			}
		}
		return wk.toString();
	}
	
	@Override
	public boolean determineEmailValid(String emailStr) {
		String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(emailStr);
		return matcher.matches();
	}
	
	@Override
	public void sendEmail (String fromAddress, String toAddress, String subject,
									String messageText, String userName, String password ) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		
		final String uName = userName;
		final String pWord = password;
		
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(uName,pWord);
					}
				});
		
		try {
			 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toAddress));
			message.setSubject(subject);
			message.setText(messageText);
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}



	}
	

}

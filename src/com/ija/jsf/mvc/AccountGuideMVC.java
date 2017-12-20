package com.ija.jsf.mvc;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ija.pb.nondaodata.CurrentScreenInfo;

@Component
@Scope("request")
public class AccountGuideMVC implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	@Autowired
	CurrentScreenInfo currentScreenInfo;
	
	public AccountGuideMVC() {
	}

}

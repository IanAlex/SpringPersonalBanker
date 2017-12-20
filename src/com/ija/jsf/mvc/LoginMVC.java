package com.ija.jsf.mvc;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ApplicationScoped;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.application.FacesMessage;


import com.ija.pb.hibernate.dao.*;
import com.ija.pb.hibernate.tables.*;
import com.ija.pb.nondaodata.*;

@Component
@ViewScoped
public class LoginMVC implements Serializable {

	private static final long serialVersionUID = -2912693560354598053L;
	
	@Autowired 
	@Qualifier("loginDao")
	private LoginDao loginDao;	
	@Autowired 
	@Qualifier("clientDao")
	private ClientDao clientDao;
	@Autowired
	CurrentScreenInfo currentScreenInfo;
	@Autowired
	ClientSetUpMVC csuMVC;
	@Autowired
	ClientEditViewSettingsMVC cevMVC;
	@Autowired
	TabNavigator tabNavigator;
	
	private String name;	
	private String password;
	
	
	/*
	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}
	*/
	
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
	
	public void postJSFLoginValidation(ComponentSystemEvent event) {
		UIComponent components = event.getComponent();
		UIInput uiUser = (UIInput) components.findComponent("username");
		String user = (String) uiUser.getLocalValue();
		String userId = uiUser.getClientId();
		UIInput uiPassword = (UIInput) components.findComponent("password");
		String password = (String) uiPassword.getLocalValue();
		String passwordId = uiPassword.getClientId();
		if (user == null || user.trim().equals("")) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage("You must enter a user name");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(userId, msg);
			fc.renderResponse();
		}
		if (password == null || password.trim().equals("")) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage("You must enter a password");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(passwordId, msg);
			fc.renderResponse();
		}
		if (user != null && !user.trim().equals("")  && password != null && !password.trim().equals("") ) {
			Login login = loginDao.findForUserInfo(user, password);
			if (login == null) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage msg = new FacesMessage("Invalid user name or password.  Try again or register.");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(userId, msg);
				fc.renderResponse();
			}
		}
	}
	
	public boolean validateLogin() {
		FacesContext fc = FacesContext.getCurrentInstance();
		boolean goodFlag = true;
		if (name == null || name.trim().equals("")) {	
			goodFlag = false;
			fc.addMessage(null, new FacesMessage("You must enter a user name"));
		}
		if (password == null || password.trim().equals("")) {
			goodFlag = false;
			fc.addMessage(null, new FacesMessage("You must enter a psssword"));
		}
		if (name != null && !name.trim().equals("")  && password != null && !password.trim().equals("") ) {
			Login login = loginDao.findForUserInfo(name, password);
			if (login == null) {
				goodFlag = false;
				fc.addMessage(null, new FacesMessage("Invalid user name or password.  Try again or register."));
			}
		}
		return goodFlag;
	}
	
	public String signin() {
		currentScreenInfo.setClient(null);
		if (!validateLogin()) {
			return null;
		}
		Login login = loginDao.findForUserInfo(name, password);
		if (login != null) {
			System.out.println("hi ian -- valid signin");
			Client client = clientDao.findByLoginKey(login.getLoginKey());
			currentScreenInfo.setLoginKey(login.getLoginKey());
			if (client != null) {
				currentScreenInfo.setClient(client);
				if (client instanceof Person) {
					currentScreenInfo.setPerson((Person) client);
				}
				else if (client instanceof Business) {
					currentScreenInfo.setBusiness((Business) client);
				}
				currentScreenInfo.setAddress(client.getAddress());
				cevMVC.init();
				tabNavigator.init();
				tabNavigator.setActiveTabIndex(-1);  // no tab intially selected
			}
			else {
				csuMVC.init();
			}
			return "success";
		}
		else {
			System.out.println("hi ian -- BAD signin");
			return null;
		}
	}
	
}

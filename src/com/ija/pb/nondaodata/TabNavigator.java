package com.ija.pb.nondaodata;

import java.io.Serializable;


import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;

import org.springframework.stereotype.Component;

import org.primefaces.event.TabChangeEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ija.pb.hibernate.dao.*;
import com.ija.jsf.mvc.*;

@Component
public class TabNavigator implements Serializable {	

private static final long serialVersionUID = -2912693560354598053L; 
	
	@Autowired 
	@Qualifier("clientDao")
	private ClientDao clientDao;
	@Autowired
	CurrentScreenInfo currentScreenInfo;
	@Autowired
	AccountSetUpMVC accountSetUpMVC;
	
	private Integer activeTabIndex = new Integer(-1);	
	
	private static final String MENU_NOT_CHOSE_STYLE = "nav-menuTab";
	private static final String MENU_IS_CHOSE_STYLE = "navChose-menuTab";
	
	private String loginTabStyle;
	private String aboutTabStyle;
	private String viewEditClientTabStyle;
	private String portfolioTabStyle;
	private String addAccountTabStyle;
	
	boolean inLoginFirstTime = true;
	
	public TabNavigator() {
	}
	
	@PostConstruct
	public void init() {
		System.out.println("hi ian 333.A");
		loginTabStyle = MENU_NOT_CHOSE_STYLE;
		aboutTabStyle = MENU_NOT_CHOSE_STYLE;
		viewEditClientTabStyle = MENU_NOT_CHOSE_STYLE;
		portfolioTabStyle = MENU_NOT_CHOSE_STYLE;
		addAccountTabStyle = MENU_NOT_CHOSE_STYLE;
		activeTabIndex = new Integer(-1);
	}
		
	public Integer getActiveTabIndex() {
		return this.activeTabIndex;
	}
	
	public void setActiveTabIndex(Integer activeTabIndex){
	     this.activeTabIndex = activeTabIndex;
	}
	
	public String getLoginTabStyle() {
		return this.loginTabStyle;
	}
 
	public void setLoginTabStyle(String loginTabStyle) {
		this.loginTabStyle = loginTabStyle;
	}	
	
	public String getAboutTabStyle() {
		return this.aboutTabStyle;
	}
 
	public void setAboutTabStyle(String aboutTabStyle) {
		this.aboutTabStyle = aboutTabStyle;
	}	
	
	public String getViewEditClientTabStyle() {
		return this.viewEditClientTabStyle;
	}
 
	public void setViewEditClientTabStyle(String viewEditClientTabStyle) {
		this.viewEditClientTabStyle = viewEditClientTabStyle;
	}	
	
	public String getPortfolioTabStyle() {
		return this.portfolioTabStyle;
	}
 
	public void setPortfolioTabStyle(String portfolioTabStyle) {
		this.portfolioTabStyle = portfolioTabStyle;
	}
	
	public String getAddAccountTabStyle() {
		return this.addAccountTabStyle;
	}
 
	public void setAddAccountTabStyle(String addAccountTabStyle) {
		this.addAccountTabStyle = addAccountTabStyle;
	}
	
	public void onStartPageTabChange(TabChangeEvent event){
		FacesContext fctx = FacesContext.getCurrentInstance();
		Application application = fctx.getApplication();
		NavigationHandler navHandler = application.getNavigationHandler();
		String tabId = event.getTab().getId();
		if (tabId.equals("AboutInfo")) {
			activeTabIndex = 0;
			aboutTabStyle = MENU_IS_CHOSE_STYLE;
			loginTabStyle = MENU_NOT_CHOSE_STYLE;
			FacesContext.getCurrentInstance().renderResponse();
			if (inLoginFirstTime) {
				inLoginFirstTime = false;
				navHandler.handleNavigation(fctx,null, "start.xhtml?faces-redirect=true");
			}
			/*
			else {
				navHandler.handleNavigation(fctx,null, "start.xhtml");
			}
			*/
		}
		else {
			activeTabIndex = 1;
			aboutTabStyle = MENU_NOT_CHOSE_STYLE;
			loginTabStyle = MENU_IS_CHOSE_STYLE;
			//FacesContext.getCurrentInstance().renderResponse();
			if (inLoginFirstTime) {
				inLoginFirstTime = false;
				navHandler.handleNavigation(fctx,null, "start.xhtml?faces-redirect=true"); 
			}
			
		}
	}
	
	public void onMainPageTabChange(TabChangeEvent event) {   
		currentScreenInfo.setClient(clientDao.findByLoginKey(currentScreenInfo.getLoginKey()));
		FacesContext fctx = FacesContext.getCurrentInstance();
		Application application = fctx.getApplication();
		NavigationHandler navHandler = application.getNavigationHandler();
		String tabId = event.getTab().getId();
		if (tabId.equals("About")) {
			activeTabIndex = 0;
			aboutTabStyle = MENU_IS_CHOSE_STYLE;
			viewEditClientTabStyle = MENU_NOT_CHOSE_STYLE;
			portfolioTabStyle = MENU_NOT_CHOSE_STYLE;
			addAccountTabStyle = MENU_NOT_CHOSE_STYLE;
			navHandler.handleNavigation(fctx,null, "main.xhtml"); 
		}
		else if (tabId.equals("ClientSettings")) {
			activeTabIndex = 1;
			aboutTabStyle = MENU_NOT_CHOSE_STYLE;
			viewEditClientTabStyle = MENU_IS_CHOSE_STYLE;
			portfolioTabStyle = MENU_NOT_CHOSE_STYLE;
			addAccountTabStyle = MENU_NOT_CHOSE_STYLE;
			navHandler.handleNavigation(fctx,null, "main.xhtml"); 
		}
		else if (tabId.equals("Portfolio")) {
			activeTabIndex = 2;
			aboutTabStyle = MENU_NOT_CHOSE_STYLE;
			viewEditClientTabStyle = MENU_NOT_CHOSE_STYLE;
			portfolioTabStyle = MENU_IS_CHOSE_STYLE;
			addAccountTabStyle = MENU_NOT_CHOSE_STYLE;
			navHandler.handleNavigation(fctx,null, "main.xhtml"); 
		}
		else if (tabId.equals("AddAccount")) {
			activeTabIndex = 3;
			aboutTabStyle = MENU_NOT_CHOSE_STYLE;
			viewEditClientTabStyle = MENU_NOT_CHOSE_STYLE;
			portfolioTabStyle = MENU_NOT_CHOSE_STYLE;
			addAccountTabStyle = MENU_NOT_CHOSE_STYLE;
			accountSetUpMVC.init();
			navHandler.handleNavigation(fctx,null, "addAccount.xhtml?faces-redirect=true");
		}
		else if (tabId.equals("SignOut")) {
			activeTabIndex = 4;
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			navHandler.handleNavigation(fctx,null, "logout.xhtml?faces-redirect=true"); 
		}
		return;
	}
	
	public String goToAccountSummary() {
		activeTabIndex = 2;
		return "main.xhtml?faces-redirect=true";
	}
	
	public String goToSpecificAccount() {
		activeTabIndex = 2;
		return "account-detail.xhtml?faces-redirect=true";
	}
	
	public void onAccountPageTabChange(TabChangeEvent event) { 
		String tabId = event.getTab().getId();
		if (tabId.equals("About")) {
			activeTabIndex = 0;
			aboutTabStyle = MENU_IS_CHOSE_STYLE;
			viewEditClientTabStyle = MENU_NOT_CHOSE_STYLE;
			portfolioTabStyle = MENU_NOT_CHOSE_STYLE;
			addAccountTabStyle = MENU_NOT_CHOSE_STYLE;
		}
		else if (tabId.equals("ClientSettings")) {
			activeTabIndex = 1;
			aboutTabStyle = MENU_NOT_CHOSE_STYLE;
			viewEditClientTabStyle = MENU_IS_CHOSE_STYLE;
			portfolioTabStyle = MENU_NOT_CHOSE_STYLE;
			addAccountTabStyle = MENU_NOT_CHOSE_STYLE;
		}
		else if (tabId.equals("Portfolio")) {
			activeTabIndex = 2;
			aboutTabStyle = MENU_NOT_CHOSE_STYLE;
			viewEditClientTabStyle = MENU_NOT_CHOSE_STYLE;
			portfolioTabStyle = MENU_IS_CHOSE_STYLE;
			addAccountTabStyle = MENU_NOT_CHOSE_STYLE;
		}
		else if (tabId.equals("AddAccount")) {
			activeTabIndex = 3;
			aboutTabStyle = MENU_NOT_CHOSE_STYLE;
			viewEditClientTabStyle = MENU_NOT_CHOSE_STYLE;
			portfolioTabStyle = MENU_NOT_CHOSE_STYLE;
			addAccountTabStyle = MENU_NOT_CHOSE_STYLE;
		}
		else if (tabId.equals("SignOut")) {
			activeTabIndex = 4;
		}
		//currentScreenInfo.setClient(clientDao.findByLoginKey(currentScreenInfo.getLoginKey()));
		FacesContext fctx = FacesContext.getCurrentInstance();
		Application application = fctx.getApplication();
		NavigationHandler navHandler = application.getNavigationHandler();
		if (tabId.equals("AddAccount")) 
		{
			accountSetUpMVC.init();
			navHandler.handleNavigation(fctx,null, "addAccount.xhtml?faces-redirect=true");
		}
		else if (tabId.equals("SignOut")) {
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			navHandler.handleNavigation(fctx,null, "logout.xhtml?faces-redirect=true"); 
		}
		else {
			navHandler.handleNavigation(fctx,null, "main.xhtml?faces-redirect=true"); 
		}
		return;
		
		//return "main.xhtml?faces-redirect=true";
	}
	
	public void onClientSetupPageTabChange(TabChangeEvent event) {   
		FacesContext fctx = FacesContext.getCurrentInstance();
		Application application = fctx.getApplication();
		NavigationHandler navHandler = application.getNavigationHandler();
		String tabId = event.getTab().getId();
		if (tabId.equals("SignOut")) {
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			navHandler.handleNavigation(fctx,null, "logout.xhtml?faces-redirect=true"); 
		}
	}

}

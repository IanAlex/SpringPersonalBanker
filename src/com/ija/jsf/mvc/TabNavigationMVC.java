package com.ija.jsf.mvc;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;

import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class TabNavigationMVC implements Serializable {
	
	private static final long serialVersionUID = -2912693560354598053L;
	
	private String pageToDisplay = "main.xhtml";
	
	
	public String getPageToDisplay() {
		return this.pageToDisplay;
	}
	
	public void setPageToDisplay(String pageToDisplay){
	     this.pageToDisplay = pageToDisplay;
	}
	
	public void onTabChange(TabChangeEvent event) {   
		System.out.println("hi ian 99.87");
		/*
		try {
			FacesContext.getCurrentInstance().getExternalContext().dispatch("main.xhtml");
		}
		catch (Exception e) {	}
		*/
		FacesContext fctx = FacesContext.getCurrentInstance();
		Application application = fctx.getApplication();
		NavigationHandler navHandler = application.getNavigationHandler();
		navHandler.handleNavigation(fctx,null, "main.xhtml?faces-redirect=true"); 
		return;
		
		//return "main.xhtml?faces-redirect=true";
	}


}

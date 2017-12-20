package com.ija.spring.custom;

import java.util.Map;
import javax.faces.context.FacesContext;


import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class ViewScope implements Scope {

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		 if (FacesContext.getCurrentInstance().getViewRoot() != null) {
	            Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();
	            if (viewMap.containsKey(name)) {
	                return viewMap.get(name);
	            } else {
	                Object object = objectFactory.getObject();
	                viewMap.put(name, object);
	                return object;
	            }
	        } else {
	            return null;
	        }
	}

	@Override
	public String getConversationId() {
		return null;
	}

	@Override
	public void registerDestructionCallback(String arg0, Runnable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object remove(String name) {
		if (FacesContext.getCurrentInstance().getViewRoot() != null) {
            return FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(name);
        } else {
            return null;
        }
	}

	@Override
	public Object resolveContextualObject(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

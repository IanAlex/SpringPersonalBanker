package com.ija.pb.junit;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ija.pb.nondaodata.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:///C:/Users/Ian/SpringFinancial/SpringPersonalBanker/WebContent/WEB-INF/applicationContext.xml"})
public class AbstractTranslationsTest {
	
	@Autowired
	private Translations translations;
	
	@Test
	public void runTest() {
		System.out.println("hi ian Genders(F) :" + translations.getGenders().get("F"));
		System.out.println("hi ian Nameprefix(Pr) :" + translations.getNameprefix().get("Pr"));
		System.out.println("hi ian Maritalstatus(CL) :" + translations.getMaritalstatus().get("CL"));
		System.out.println("hi ian Clienttypes(P) :" + translations.getClienttypes().get("P"));
		System.out.println("hi ian Accounttypes(GC) :" + translations.getAccounttypes().get("GC"));
		System.out.println("hi ian Stocktxntypes(S) :" + translations.getStocktxntypes().get("S"));
		System.out.println("hi ian Gictxntypes(M) :" + translations.getGictxntypes().get("M"));
		System.out.println("hi ian Annuitytxntypes(P) :" + translations.getAnnuitytxntypes().get("P"));
		System.out.println("hi ian Savingstxntypes(I) :" + translations.getSavingstxntypes().get("I"));
		System.out.println("hi ian Chequingtxntypes(TO) :" + translations.getChequingtxntypes().get("TO"));
		System.out.println("hi ian Drcr(C) :" + translations.getDrcr().get("C"));
		System.out.println("hi ian Countries(AL) :" + translations.getCountries().get("AL"));
		System.out.println("hi ian Usastates(DC) :" + translations.getUsastates().get("DC"));
		System.out.println("hi ian Canadaprovinces(NL) :" + translations.getCanadaprovinces().get("NL"));
	}

}

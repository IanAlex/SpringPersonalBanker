package com.ija.pb.services;

import java.util.*;

import com.ija.pb.hibernate.tables.*;
import com.ija.pb.nondaodata.AnnuityPayout;

public interface AnnuityService {	
	AnnuityTxn createTxn(Double amount, String annuityTxnType,  Annuity annuity, Date txnDt, Integer offsetTxnId, Account offsetAccount);
	List<AnnuityPayout> calcAnnuityPayout(Annuity annuity, Date valuationDt);
	double calcPV(Annuity annuity, Date valuationDt);
	void updateRunningTotals(Annuity annuity);
	void updateForAnnuityPayments(Annuity inAnnuity, Date inDt);
}

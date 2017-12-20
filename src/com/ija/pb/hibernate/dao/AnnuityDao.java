package com.ija.pb.hibernate.dao;

import java.util.*;
import com.ija.pb.hibernate.tables.*;

public interface AnnuityDao {
	List<Annuity> findAll();
	Set<Annuity> findForClient(Client client, Boolean openOnly);
	Annuity findById(Integer accountId);
	void store(Annuity annuity);
}

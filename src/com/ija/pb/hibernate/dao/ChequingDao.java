package com.ija.pb.hibernate.dao;

import java.util.*;
import com.ija.pb.hibernate.tables.*;

public interface ChequingDao {
	List<Chequing> findAll();
	Set<Chequing> findForClient(Client client, Boolean openOnly);
	Chequing findById(Integer accountId);
	void store(Chequing chequing);
}

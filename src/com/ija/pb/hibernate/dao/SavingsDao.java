package com.ija.pb.hibernate.dao;

import java.util.*;
import com.ija.pb.hibernate.tables.*;

public interface SavingsDao {
	List<Savings> findAll();
	Set<Savings> findForClient(Client client, Boolean openOnly);
	Savings findById(Integer accountId);
	void store(Savings savings);
}


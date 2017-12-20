package com.ija.pb.hibernate.dao;

import java.util.*;
import com.ija.pb.hibernate.tables.*;

public interface GICDao {
	List<GIC> findAll();
	Set<GIC> findForClient(Client client, Boolean openOnly);
	GIC findById(Integer accountId);
	void store(GIC gic);
}

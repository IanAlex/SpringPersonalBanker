package com.ija.pb.hibernate.dao;

import java.util.*;
import com.ija.pb.hibernate.tables.*;

public interface StockDao {
	List<Stock> findAll();
	Set<Stock> findForClient(Client client, Boolean openOnly);
	Stock findById(Integer accountId);
	void store(Stock stock);
}

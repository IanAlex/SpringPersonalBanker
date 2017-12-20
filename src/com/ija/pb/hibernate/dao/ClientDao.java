package com.ija.pb.hibernate.dao;

import java.util.*;
import com.ija.pb.hibernate.tables.*;

public interface ClientDao {
	List<Client> findAll();
	Client findById(Integer clientId);
	Client findByLoginKey(String loginKey);
	void store(Client client);
}

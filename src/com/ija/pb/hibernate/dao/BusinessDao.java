package com.ija.pb.hibernate.dao;

import java.util.*;
import com.ija.pb.hibernate.tables.*;

public interface BusinessDao {
	List<Business> findAll();
	Business findById(Integer clientId);
	void store(Business business);
}

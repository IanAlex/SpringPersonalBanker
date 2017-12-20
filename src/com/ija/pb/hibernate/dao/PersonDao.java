package com.ija.pb.hibernate.dao;

import java.util.*;
import com.ija.pb.hibernate.tables.*;

public interface PersonDao {
	List<Person> findAll();
	Person findById(Integer clientId);
	void store(Person person);
}

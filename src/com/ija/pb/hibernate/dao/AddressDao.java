package com.ija.pb.hibernate.dao;

import java.util.*;
import com.ija.pb.hibernate.tables.*;

public interface AddressDao {
	
	List<Address> findAll();
	Address findById(Integer addressId);
	void store(Address address);
}

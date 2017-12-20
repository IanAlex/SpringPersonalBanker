package com.ija.pb.hibernate.dao;

import java.util.*;
import com.ija.pb.hibernate.tables.*;

public interface LoginDao {
	List<Login> findAll();
	Login findForUserInfo(String userId, String pswd);
	Login findForLoginKey(String loginKey);
	void store (Login login);
}

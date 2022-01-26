package com.prueba.usuarios.service;

import java.util.List;

import com.prueba.usuarios.model.Phone;
import com.prueba.usuarios.model.User;

public interface UserService {
	
	User saveUser(User user);
	Phone savePhone(Phone phone);
	
	void newLogin(String userName, String token);
	
	void addPhoneToUser(Long userId, Phone phone);
	
	User getUser(Long userId);
	List<User> getUsers();
	
	

}

package com.prueba.usuarios.service;

import java.util.List;
import java.util.Optional;

import com.prueba.usuarios.model.Phone;
import com.prueba.usuarios.model.User;

public interface UserService {

	User saveUser(User user) throws Exception;
	

	Phone savePhone(Phone phone);

	User findUserByUserName(String userName);

	User finByEmail(String email);

	void newLogin(String userName, String token) throws Exception;

	void addPhoneToUser(Long userId, Phone phone);

	User getUser(Long userId);

	List<User> getUsers();
	
	Optional<User> findById(Long id);

}

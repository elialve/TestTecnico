package com.prueba.usuarios.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prueba.usuarios.model.Phone;
import com.prueba.usuarios.model.User;
import com.prueba.usuarios.repository.PhoneRepo;
import com.prueba.usuarios.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor @Transactional 
public class UserServiceImpl implements UserService, UserDetailsService{
	
	private final UserRepo userRepo;
	private final PhoneRepo phoneRepo;
	

	@Override
	public User saveUser(User user) {
		return userRepo.save(user);
	}
	
	
	

	@Override
	public Phone savePhone(Phone phone) {
		return phoneRepo.save(phone);
	}

	@Override
	public void addPhoneToUser(Long userId, Phone phone) {
		Optional<User> user = userRepo.findById(userId);
		if(user.isPresent() && phone!=null) {
			User usu = user.get();
			usu.getPhones().add(phone);
		}
	}

	
	@Override
	public User getUser(Long userId) {
		Optional<User> user = userRepo.findById(userId);
		if(user.isPresent()) {
			return user.get();
		}
		return null;
	}

	@Override
	public List<User> getUsers() {
		return userRepo.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByName(username);
		if(user == null) {
			throw new UsernameNotFoundException("User not found in the database");
		}
		
		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), new ArrayList<>());
	}




	@Override
	public void newLogin(String userName, String token) {
		User user = userRepo.findByName(userName);
		user.setLastLogin(new java.util.Date());
		user.setToken(token);
		saveUser(user);
	}

}

package com.prueba.usuarios.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prueba.usuarios.exception.FieldIncorrectException;
import com.prueba.usuarios.model.Phone;
import com.prueba.usuarios.model.User;
import com.prueba.usuarios.repository.PhoneRepo;
import com.prueba.usuarios.repository.UserRepo;
import com.prueba.usuarios.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

	private final UserRepo userRepo;
	private final PhoneRepo phoneRepo;
	
	@Autowired
	private JwtUtil jwtUtil;
	

	@Override
	public User saveUser(User user) throws Exception {

		validateFields(user);
		final String jwt = jwtUtil.generateToken(user.getName());
		user.setToken(jwt);
		user.setIsActive(true);
		return userRepo.save(user);
	}
	
	@Override
	public Phone savePhone(Phone phone) {
		return phoneRepo.save(phone);
	}

	@Override
	public void addPhoneToUser(Long userId, Phone phone) {
		Optional<User> user = userRepo.findById(userId);
		if (user.isPresent() && phone != null) {
			User usu = user.get();
			usu.getPhones().add(phone);
		}
	}

	@Override
	public User getUser(Long userId) {
		Optional<User> user = userRepo.findById(userId);
		if (user.isPresent()) {
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
		if (user == null) {
			throw new UsernameNotFoundException("User not found in the database");
		}

		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
				new ArrayList<>());
	}

	@Override
	public void newLogin(String userName, String token) throws Exception {
		User user = userRepo.findByName(userName);
		user.setLastLogin(new java.util.Date());
		user.setToken(token);
		userRepo.save(user);
	}

	@Override
	public User findUserByUserName(String userName) {
		User user = userRepo.findByName(userName);
		return user;
	}

	@Override
	public User finByEmail(String email) {
		User user = userRepo.findByEmail(email);
		return user;
	}
	

	
	public void validateFields(User user) throws FieldIncorrectException {
		if (user.getName() == null) {
			throw new FieldIncorrectException("Por favor especifique un nombre de usuario");
		}

		if (user.getName().isEmpty()) {
			throw new FieldIncorrectException("El nombre de usuario no puede estar vacio");
		}

		if (user.getEmail() == null) {
			throw new FieldIncorrectException("Por favor especifique  un email");
		}

		if (user.getEmail().isEmpty()) {
			throw new FieldIncorrectException("El email no puede estar vacio");
		}
		
		if (user.getPassword() == null) {
			throw new FieldIncorrectException("Por favor especifique una password");
		}
		
		if (user.getPassword().isEmpty() ) {
			throw new FieldIncorrectException("La password no puede estar vacia");
		}
		
		validateUserNotExists(user.getName());
		validateEmailFormat(user.getEmail());
		validateEmailNoExists(user.getEmail());
		validatePassword(user.getPassword());
		
	}
	
	
	public void validateUserNotExists(String userName) throws FieldIncorrectException {
		User usuario = findUserByUserName(userName);
		if (usuario != null ) {
			throw new FieldIncorrectException("Nombre de usuario ya existe");
		}
	}
	
	
	public void validateEmailFormat(String email) throws FieldIncorrectException {
		Pattern pat = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))");
		Matcher matcher = pat.matcher(email);
		
		if(!matcher.find()) {
			throw new FieldIncorrectException("El formato del correo no es valido");
		}
		
	}
	
	
	public void validateEmailNoExists(String email) throws FieldIncorrectException {
		User usuario = finByEmail(email);
		if (usuario != null ) {
			throw new FieldIncorrectException("El correo ya existe");
		}
	}
	
	public void validatePassword(String password) throws FieldIncorrectException {
		Pattern paternMayusc = Pattern.compile("^(?=.*[a-z])[a-z0-9]*[A-Z][a-z0-9]*$");
		Matcher validateMayusc = paternMayusc.matcher(password);
		
		if(!validateMayusc.find()) {
			throw new FieldIncorrectException("La password no debe tener signos, y contener una Mayuscula");
		}
		
		Pattern patternNumber = Pattern.compile("(?=^(?:\\D*\\d\\D*){2}$)^[\\w]+$");
		Matcher validateMaxNumber = patternNumber.matcher(password);
		
		if(!validateMaxNumber.find()) {
			throw new FieldIncorrectException("La password debe contener dos numeros");
		}
		
	}






}

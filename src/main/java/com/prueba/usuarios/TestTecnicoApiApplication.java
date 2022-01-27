package com.prueba.usuarios;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.prueba.usuarios.model.Phone;
import com.prueba.usuarios.model.User;
import com.prueba.usuarios.repository.UserRepo;



@SpringBootApplication
public class TestTecnicoApiApplication {

	
	@Autowired
	private UserRepo repository;
	
	@PostConstruct
	public void initUsers() {
		Set<Phone> hset =  new HashSet<Phone>();
	
	  List<User> users = Stream.of(
			  new User(hset, "admin","AD12ad","admin@gmail.com",true)
			  ).collect(Collectors.toList());
	  repository.saveAll(users);
			  
	}
	

	
	public static void main(String[] args) {
		SpringApplication.run(TestTecnicoApiApplication.class, args);
	}
	
}

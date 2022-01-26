package com.prueba.usuarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.usuarios.model.User;

public interface UserRepo extends JpaRepository<User,Long>{
	
	Optional<User> findById(Long id);
    User findByName(String username);
	
}

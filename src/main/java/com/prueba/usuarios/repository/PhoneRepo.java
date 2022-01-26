package com.prueba.usuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.usuarios.model.Phone;

public interface PhoneRepo extends JpaRepository<Phone,Long>{

   Optional<Phone> findById(Long id);

}

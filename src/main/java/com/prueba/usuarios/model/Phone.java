package com.prueba.usuarios.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name="TBL_PHONE")
@Entity
public class Phone {
	
	@JsonIgnore
	@ManyToOne
	private User user;
	
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String number;
	private String citycode;
	private String countrycode;
	
	public Phone() {
		
	}

	public Phone(User user, String number, String citycode, String countrycode) {
		this.user = user;
		this.number = number;
		this.citycode = citycode;
		this.countrycode = countrycode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}
	

}


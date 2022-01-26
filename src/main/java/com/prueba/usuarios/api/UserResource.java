package com.prueba.usuarios.api;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.usuarios.model.AuthRequest;
import com.prueba.usuarios.model.User;
import com.prueba.usuarios.service.UserService;
import com.prueba.usuarios.service.UserServiceImpl;
import com.prueba.usuarios.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserResource {
	private final UserService userService;

	@Autowired
	private AuthenticationManager authenticatorManager;


	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/")
	public String hello() {
		return "Hola que tal";
	}

	@PostMapping("/authenticate")
	public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
		JSONObject res = new JSONObject();
		try {
			authenticatorManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		} catch (Exception e) {
			 
		     res.put("mensaje","User or Password Invalid");
		     return res.toString();
		}
		
		
		String token = jwtUtil.generateToken(authRequest.getUserName());
	
		res.put("mensaje","Por favor, ocupar token para acceso a otros endPoints");
	    res.put("token",token);
	    
	    userService.newLogin(authRequest.getUserName(),token );
	    
		return res.toString();
		
	}

	@GetMapping("/listar")
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.ok().body(userService.getUsers());
	}

	@PostMapping("/registro")
	public ResponseEntity<User> saveUser(@RequestBody User user) throws Exception {
		final String jwt = jwtUtil.generateToken(user.getName());
		user.setToken(jwt);
		
		user.setIsActive(true);
		User usuario = userService.saveUser(user);

		return new ResponseEntity<>(usuario, HttpStatus.CREATED);

	}

}

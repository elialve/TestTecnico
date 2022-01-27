package com.prueba.usuarios.api;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.usuarios.exception.FieldIncorrectException;
import com.prueba.usuarios.model.AuthRequest;
import com.prueba.usuarios.model.User;
import com.prueba.usuarios.service.UserService;
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
	public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
		JSONObject res = new JSONObject();
		try {
			authenticatorManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		} catch (UsernameNotFoundException e) {
			res.put("mensaje", "User or Password Invalid");
			return new ResponseEntity<>(res.toString(), HttpStatus.BAD_REQUEST);
		}

		String token = jwtUtil.generateToken(authRequest.getUserName());
		res.put("mensaje", "Por favor, ocupar token para acceso a otros endPoints");
		res.put("token", token);
		userService.newLogin(authRequest.getUserName(), token);

		return new ResponseEntity<>(res.toString(), HttpStatus.OK);

	}


	@PostMapping("/registro")
	public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
		User usuario = null;

		try {
			usuario = userService.saveUser(user);
		}catch(FieldIncorrectException e) {
			JSONObject res = new JSONObject();
			res.put("mensaje", "Error: " + e.getMessage());
			return new ResponseEntity<>(res.toString(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			JSONObject res = new JSONObject();
			res.put("mensaje", "Error: " + e.getMessage());
			return new ResponseEntity<>(res.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(usuario, HttpStatus.CREATED);

	}
	

	@GetMapping("/listar")
	public ResponseEntity<List<User>> getUsers(){
		try {
			List<User> list = (List<User>) userService.getUsers();
			if(list.isEmpty() || list.size() == 0) {
				return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<List<User>>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<User> getSingleUser(@PathVariable Long id){
		Optional<User> user = userService.findById(id);
		if(user.isPresent()) {
			return new ResponseEntity<User>(user.get(), HttpStatus.OK);
		}
		
		return new ResponseEntity<User>( HttpStatus.NOT_FOUND);
	}
	
    

}

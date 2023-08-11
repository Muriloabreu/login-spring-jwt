package com.login.springjwt.controllers;


import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import com.login.springjwt.Dtos.UserDto;
import com.login.springjwt.models.UserModel;
import com.login.springjwt.repositories.UserRepository;



@Controller
@CrossOrigin(originPatterns = "*", maxAge = 3600) /*Para permitir ser acessado de qualquer fonte*/
@RequestMapping("/users") /*Mapeamento a nível de classe*/
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping
	public ResponseEntity<Object> saveUser(@RequestBody @Validated UserDto userDto){	
		
		if (userRepository.existsByCpf(userDto.getCpf())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: CPF is already in use!");}/* Check se placa já está cadastrada */
		if (userRepository.existsBylogin(userDto.getLogin())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Login is already in use!");}/* Check se placa já está cadastrada */
		
		
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(userDto, userModel); /*Coverte Dtos para Model*/
		String passwordcripto = new BCryptPasswordEncoder().encode(userModel.getPasswordUser());
		userModel.setPasswordUser(passwordcripto);
		return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(userModel));
	}
	
	@GetMapping
	public ResponseEntity<Iterable<UserModel>> getAllUsers(){
		return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") Long id) {

		Optional<UserModel> userOptional = userRepository.findById(id);

		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found. "); /* Mensagem se a User não for encontrado */
		}

		return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") Long id) {

		Optional<UserModel> userOptional = userRepository.findById(id);

		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found. "); /* Mensagem se a User não for encontrado */
		}

		userRepository.delete(userOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.. ");

	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateConsumo(@PathVariable(value = "id") Long id,
			                                        @RequestBody @Validated UserDto userDto) {

		Optional<UserModel> userOptional = userRepository.findById(id);

		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found. "); /* Mensagem se a User não for encontrado */
		}
		
		var userModel = userOptional.get();
		userModel.setName(userDto.getName());
		userModel.setLogin(userDto.getLogin());		
		
		String passwordcriptoDto = new BCryptPasswordEncoder().encode(userDto.getPasswordUser());

		userModel.setPasswordUser(passwordcriptoDto);
		userModel.setCpf(userDto.getCpf());	
		
				
		return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userModel));
	

	}

}

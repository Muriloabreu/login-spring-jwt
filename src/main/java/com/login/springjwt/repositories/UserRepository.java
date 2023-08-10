package com.login.springjwt.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.login.springjwt.models.UserModel;

public interface UserRepository extends CrudRepository<UserModel, Long>{
	
	@Query("select u from UserModel u where u.login = ?1 ")/*BUSCA CUSTOMIZADA NA TABELA USER PELO PARAMENTRO LOGIN*/
	UserModel findUserByLogin(String login);
	
	boolean existsByCpf(String cpf);
	boolean existsBylogin(String login);

}

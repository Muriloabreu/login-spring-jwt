package com.login.springjwt.Dtos;



public class UserDto {	

	private String login;
	
	private String passwordUser;
	
	private String name;
	
	private String cpf;

	public UserDto() {
	}

	public UserDto(String login, String passwordUser, String name, String cpf) {
		this.login = login;
		this.passwordUser = passwordUser;
		this.name = name;
		this.cpf = cpf;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasswordUser() {
		return passwordUser;
	}

	public void setPasswordUser(String passwordUser) {
		this.passwordUser = passwordUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	
	
	
	
	

}

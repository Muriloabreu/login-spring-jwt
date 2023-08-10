package com.login.springjwt.models;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.management.relation.Role;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TB_USERS")
public class UserModel implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;	
	@Column(unique = true)
	private String login;
	@Column(unique = false)
	private String passwordUser;
	@Column(unique = false)
	private String name;
	@Column(unique = true)
	private String cpf;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", uniqueConstraints = @UniqueConstraint(
			columnNames = {"user_id", "role_id"}, name = "unique_role_user" ),
	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", table = "tb_user", unique = false, 
	foreignKey = @ForeignKey(name = "user_fk", value = ConstraintMode.CONSTRAINT)),
	
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", table = "tb_role", unique = false, updatable = false,
			foreignKey = @ForeignKey(name = "role_fk", value = ConstraintMode.CONSTRAINT)) )
	private List<RoleModel> roles; /* PAPEIS, ACESSOS*/
	
	public UserModel() {		
	}

	public UserModel(Long id, String login, String passwordUser, String name, String cpf, List<RoleModel> roles) {
		this.id = id;
		this.login = login;
		this.passwordUser = passwordUser;
		this.name = name;
		this.cpf = cpf;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	public List<RoleModel> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleModel> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserModel [id=" + id + ", login=" + login + ", passwordUser=" + passwordUser + ", name=" + name + ", cpf=" + cpf 
				+ ", roles=" + roles + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserModel other = (UserModel) obj;
		return Objects.equals(id, other.id);
	}
	
	/*SÃO OS ACESSOS DO USUARIOS, ROLE_ADMIN, ROLE_USER*/
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {	
		
		return roles;
	}

	@JsonIgnore
	@Override
	public String getPassword() {		
		return this.passwordUser;
	}

	@JsonIgnore
	@Override
	public String getUsername() {		
		return this.login;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {		
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {		
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {		
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {		
		return true;
	}
	
	
	
	

}

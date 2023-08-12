package com.login.springjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.login.springjwt.services.UserServiceImpl;

/*Mapeia URL, endereços, autoriza ou bloqueia acesso a URL*/
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	UserServiceImpl userServiceImpl;
	
	/* CONFIGURA AS SOLICITAÇÕES DE ACESSO POR HTTP */
	@Override
		protected void configure(HttpSecurity http) throws Exception {
		
		/* ATIVANDO PROTEÇÃO CONTRA USUARIOS QUE NÃO ESTÃO VALIDADOS POR TOKEN */
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		/*ATIVANDO A PERMISSÃO DE ACESSO A PAGINA INICIAL DO SISTEMA EX.: SISTEMA.COM.BR/INDEX.HTML */
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()
		
		/* URL LOGOUT - REDIRECIONA APÓS USER SAIR DO SISTEMA */
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		/* MAPEIA URL DE LOGOUT E INVALIDA USUARIO */
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		/* FILTRA AS REQUISIÇÕES DE LOGIN PARA AUTENTICAÇÃO */
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
				UsernamePasswordAuthenticationFilter.class)	
		
		/* FILTRA DEMAIS REQUISIÇÕES PARA VERIFICAR A PRESENÇA DO TOKEN JWT NO HEADER HTTP */		
		.addFilterBefore(new JwtApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
		}
	
	
	

	@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			
		    
		/* SERVICE QUE IRA CONSULTAR O USUARIO NO BANCO DE DADOS */
		auth.userDetailsService(userServiceImpl)

		/* PADRAO DE CODIFICAÇÃO DE SENHA */
		.passwordEncoder(new BCryptPasswordEncoder());
			
		}
}

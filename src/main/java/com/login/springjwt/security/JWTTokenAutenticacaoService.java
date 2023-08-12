package com.login.springjwt.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.login.springjwt.ApplicationContextLoad;
import com.login.springjwt.models.UserModel;
import com.login.springjwt.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {
	
	/*VALIDADE DE DOIS DIAS DO TOKEN*/
	private static final long EXPIRATION_TIME = 172800000;
	
	/*SENHA UNICA PARA COMPOR A AUTENTICAÇÃO*/
	private static final String SECRET = "gshetagshndkopaunenshsasgdjdosuss";
	
	/*PREFIXO PADRÃO DE TOKEN */
	private static final String TOKEN_PREFIX = "Bearer";
	
	/**/
	private static final String HEADER_STRING = "Authorization";
	
	/*GERANDO TOKEN DE AUTENTICAÇÃO ADICIONANDO AO CABEÇALHO E RESPOSTA Http*/
	public void addAuthentication(HttpServletResponse response, String username) throws IOException{
		
		/*MONTAGEM DO TOKEN*/
		String JWT = Jwts.builder() /*Chama o gerador de Token*/
		        	.setSubject(username) /*Adicona o usuario*/
		        	.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /*Tempo de expiração*/
		        	.signWith(SignatureAlgorithm.HS512, SECRET).compact(); /*Compactação e algoritmos de geração de senha*/
	
		/*JUNTA TOKEN COM O PREFIXO*/
		String token = TOKEN_PREFIX + " " + JWT; /*Bearer + TOKEN*/
		
		/*ADD CABEÇALHO Http*/
		response.addHeader(HEADER_STRING, token); /*Authorization: Bearer + TOKEN*/
		
		/*ESCREVE TOKEN COMO RESPOSTA DO CORPO Http*/
		response.getWriter().write("{\"Authorization\": \""+token+"\"}"); 
	
	}	
	
	
	/*RETORNA USER VALIDADO COM TOKEN OU CASO NÃO SEJA VALIDO RETORNA NULL*/
	public Authentication getAuthentication(HttpServletRequest request) {
		
		/* PEGA O TOKEN ENVIADO NO CABEÇALHO Http */
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {

			/* FAZ A VALIDAÇÃO DO TOKRN DO USER NA REQUISIÇÃO */
			String user = Jwts.parser().setSigningKey(SECRET) /* Bearer + TOKEN */
							.parseClaimsJws(token.replace(TOKEN_PREFIX, "")) /* SÓ FICA O TOKEN */
							.getBody().getSubject();/* User */

			if (user != null) {
				/*
				 * ApplicationContextLoad carrega todos os repositorys,services em memoria do
				 * spriing
				 */
				UserModel userModel = ApplicationContextLoad.getApplicationContext()
						.getBean(UserRepository.class).findUserByLogin(user);

				if (userModel != null) {
					
					return new UsernamePasswordAuthenticationToken(
							userModel.getLogin(),
							userModel.getPasswordUser(),
							userModel.getAuthorities());
							

				}

			}

		}
	return null; /*NÃO AUTORIZADO*/
		
			
	}
	
	

	
	
	/* RETORNAR USER LOGADO */

}

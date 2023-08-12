package com.login.springjwt.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/*FILTRO ONDE TODAS AS REQUISIÇÕES SERÃO CAPTURADAS PARA AUTENTICAR*/
public class JwtApiAutenticacaoFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		/*ESTABELECE AUTENTICAÇÃO PARA REQUISIÇÃO*/
		Authentication authentication = new JWTTokenAutenticacaoService()
				.getAuthentication((HttpServletRequest) request);
		
		
		/*INSERE O PROCESSO DE AUTENTICAÇÃO  NO SPRING SECURITY*/
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		/*CONTINUA O PROCESSO*/
		chain.doFilter(request, response);
		
	}

}

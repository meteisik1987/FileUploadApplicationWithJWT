package com.mete.fileuploadapplication.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
//		super();
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			String username = request.getParameter("username");
			String passord = request.getParameter("password");

			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, passord);
			return authenticationManager.authenticate(token);
		} catch (Exception e) {
			response.setHeader("message", e.getMessage());
			try {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
//			Map<String, String> tokens = new HashMap<>();
//
//			tokens.put("message", e.getMessage());
//
//			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//			new ObjectMapper().writeValue(response.getOutputStream(), tokens);
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		User user = (User) auth.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

		List<String> authorities = new ArrayList<>();

		for (GrantedAuthority authority : user.getAuthorities()) {
			authorities.add(authority.getAuthority());
		}

		String access_token = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 50 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString()).withClaim("roles", authorities).sign(algorithm);

		String refresh_token = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 500 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString()).sign(algorithm);

		Map<String, String> tokens = new HashMap<>();

		tokens.put("access_token", access_token);
		tokens.put("refresh_token", refresh_token);

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);

	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		Map<String, String> tokens = new HashMap<>();
		tokens.put("status", "FORBIDDEN");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);

	}

}

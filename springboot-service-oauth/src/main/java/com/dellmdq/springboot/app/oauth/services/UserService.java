package com.dellmdq.springboot.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dellmdq.springboot.app.commons.users.models.entity.User;
import com.dellmdq.springboot.app.oauth.clients.UserFeignClient;

import brave.Tracer;
import feign.FeignException;

@Service
public class UserService implements UserDetailsService, IUserService {

	private Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserFeignClient feignClient;

	@Autowired
	private Tracer tracer;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		try {

			User user = feignClient.findByUserName(username);

			// obtenemos las autorizaciones
			List<GrantedAuthority> authorities = user.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role.getName()))// convertimos rol a authority
					.peek(authority -> log.info("Role: " + authority.getAuthority()))// mostramos el role convertido a authority
					.collect(Collectors.toList());

			log.info("Authenticated user: " + username);

			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
					user.getEnabled(), true, true, true, authorities);

		} catch (FeignException e) {
			String error = "Login error. User " + username + " does not exists."; 
			log.error(error);
			
			tracer.currentSpan().tag("Error message", error + ": " + e.getMessage());
			throw new UsernameNotFoundException(error);
		}

	}

	@Override
	public User findByUsername(String username) {
		return feignClient.findByUserName(username);
	}

	@Override
	public User update(User user, Long id) {
		return feignClient.update(user, id);
	}

}

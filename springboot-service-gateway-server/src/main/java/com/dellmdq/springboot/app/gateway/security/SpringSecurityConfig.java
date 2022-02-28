package com.dellmdq.springboot.app.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@EnableWebFluxSecurity
public class SpringSecurityConfig {

	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	
	@Bean
	public SecurityWebFilterChain configure(ServerHttpSecurity http) {
		return http.authorizeExchange()
				.pathMatchers("/api/oauth/oauth/token").permitAll()
				.pathMatchers(HttpMethod.GET, "/api/products/products",
						"/api/items/items",
						"/api/users/users",
						"/api/items/products/{id}/quantity/{quantity}",
						"/api/products/products/{id}").permitAll()
				.pathMatchers(HttpMethod.GET, "/api/users/users/{id}").hasAnyRole("ADMIN","USER")
				.pathMatchers("/api/products/**", "/api/items/**", "/api/users/users/**").hasAnyRole("ADMIN")
				.anyExchange().authenticated()
				.and().addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.csrf().disable()
				.build();
			
	}
}

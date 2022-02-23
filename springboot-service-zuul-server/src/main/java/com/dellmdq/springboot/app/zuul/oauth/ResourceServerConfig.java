package com.dellmdq.springboot.app.zuul.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


/**Aca vamos a configurar los recursos(endpoints) para que queden protegidos con JWT*/
@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Value("${config.security.oauth.jwt.key}")
	private String jwtKey;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
	}

	/**Siempre de mas especificas a reglas menos especificas el orden de los antMatchers*/
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/oauth/oauth/**").permitAll()//queda publica con permitAll
		.antMatchers(HttpMethod.GET, "/api/products/products", "/api/items/items", "/api/users/users").permitAll()
		.antMatchers(HttpMethod.GET, "/api/products/products/{id}",
				"/api/items/products/{id}/quantity/{quantity}",
				"/api/users/users/{id}").hasAnyRole("ADMIN","USER")
		.antMatchers("/api/products/**", "/api/items/**","/api/users/**").hasRole("ADMIN")
		.anyRequest().authenticated();
//		esto se puede hacer mas fácil usando wildcards y sin especificar el metodo solo la ruta base^^		
//		.antMatchers(HttpMethod.POST, "/api/products/products", "/api/items/products","/api/users/users").hasRole("ADMIN")
//		.antMatchers(HttpMethod.PUT, "/api/products/products/{id}", "/api/items/products/{id}", "/api/users/users/{id}").hasRole("ADMIN")
//		.antMatchers(HttpMethod.DELETE, "/api/products/products/{id}", "/api/items/products/{id}", "/api/users/users/{id}").hasRole("ADMIN");
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(jwtKey);//misma llave con la que se genero el token para desde aca validarlo
		return tokenConverter;
	}
	
}

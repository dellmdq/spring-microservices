package com.dellmdq.springboot.app.zuul.oauth;

import java.util.Arrays;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


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
		.anyRequest().authenticated()
		.and().cors().configurationSource(corsConfigurationSource());//configuramos el cors para habilitar acceso desde otros dominios
		
		
		
//		esto se puede hacer mas fácil usando wildcards y sin especificar el metodo solo la ruta base^^		
//		.antMatchers(HttpMethod.POST, "/api/products/products", "/api/items/products","/api/users/users").hasRole("ADMIN")
//		.antMatchers(HttpMethod.PUT, "/api/products/products/{id}", "/api/items/products/{id}", "/api/users/users/{id}").hasRole("ADMIN")
//		.antMatchers(HttpMethod.DELETE, "/api/products/products/{id}", "/api/items/products/{id}", "/api/users/users/{id}").hasRole("ADMIN");
	}
	/**Configuramos Cors para hablitar acceso.*/
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(Arrays.asList("*"));//agregamos el origen del dominio que intenta acceder
		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		
		return source;
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(Base64.getEncoder().encodeToString(jwtKey.getBytes()));//misma llave con la que se genero el token para desde aca validarlo
		return tokenConverter;
	}
	
	/**Habilitamos cors con un filtro para su acceso desde la aplicación en general no solo desde spring security*/
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
	
}

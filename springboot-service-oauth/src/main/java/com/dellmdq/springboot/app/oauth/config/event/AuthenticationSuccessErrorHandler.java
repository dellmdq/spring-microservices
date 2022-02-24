package com.dellmdq.springboot.app.oauth.config.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.dellmdq.springboot.app.commons.users.models.entity.User;
import com.dellmdq.springboot.app.oauth.services.IUserService;

import feign.FeignException;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {
	
	private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);
	
	@Autowired
	private IUserService userService;

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		
		if(authentication.getDetails() instanceof WebAuthenticationDetails) {
			return;
		}
		
		
		UserDetails userDetail = (UserDetails) authentication.getPrincipal();
		String message = "Success login: " + userDetail.getUsername();
		System.out.println(message);
		log.info(message);
		
		User userLogged = userService.findByUsername(authentication.getName());

		if(userLogged.getLoggingFails() != null && userLogged.getLoggingFails() > 0) {
			userLogged.setLoggingFails(0);
			userService.update(userLogged, userLogged.getId());
		}
	}

	/**Implementado cantidad max de tres intentos de logging*/
	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
	
		String message= "Login error: " + exception.getMessage();
		log.error(message);
		System.out.println(message);
		
		try {
			
			User user = userService.findByUsername(authentication.getName());
			if(user.getLoggingFails() == null) {
				user.setLoggingFails(0);
			}
			
			//log.info("Logging attempt: " + user.getLoggingFails());
			user.setLoggingFails(user.getLoggingFails() + 1);
			log.info("Logging attempt: " + user.getLoggingFails());
			
			if(user.getLoggingFails() >= 3) {
				log.error(String.format("User %s disabled. Too many failed loggings", user.getUsername()));
				user.setEnabled(false);
			}
			
			userService.update(user, user.getId());
			
			
			
		}catch(FeignException exc) {
			
			log.error(String.format("Username %s does not exist", authentication.getName()));
		}
	
	
	}

}

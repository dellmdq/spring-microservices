package com.dellmdq.springboot.app.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dellmdq.springboot.app.commons.models.entity.User;

@FeignClient("service-users")
public interface UserFeignClient {
	
	@GetMapping("/users/search/findByUsername")
	public User findByUserName(@RequestParam String username);
	

}

package com.dellmdq.springboot.app.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.dellmdq.springboot.app.commons.users.models.entity.User;


@FeignClient("service-users")
public interface UserFeignClient {
	
	@GetMapping("/users/search/findByUsername")
	public User findByUserName(@RequestParam("username") String username);
	
	@PutMapping("/users/{id}")
	public User update(@RequestBody User user, @PathVariable Long id);
}

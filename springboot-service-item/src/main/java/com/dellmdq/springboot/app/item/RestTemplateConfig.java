package com.dellmdq.springboot.app.item;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean//("restClient")
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	};
}

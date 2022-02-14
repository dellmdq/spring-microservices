package com.dellmdq.springboot.app.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCircuitBreaker//se encarga con un hilo separado de para envolver ribber y hacer manejo de fallos, latencia, busqueda de rutas.
@EnableEurekaClient
//@RibbonClient(name = "service-products") //ya viene en eureka
@EnableFeignClients
@SpringBootApplication
public class SpringbootServiceItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServiceItemApplication.class, args);
	}

}

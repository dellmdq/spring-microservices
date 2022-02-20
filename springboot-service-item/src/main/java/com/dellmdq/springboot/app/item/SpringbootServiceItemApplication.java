package com.dellmdq.springboot.app.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


//empezamos a usar resilience esta anotacion es de hystrix
//@EnableCircuitBreaker//se encarga con un hilo separado de para envolver ribber y hacer manejo de fallos, latencia, busqueda de rutas.

//ya viene en eureka
//@RibbonClient(name = "service-products") 

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})//con esta clase deja de configurar automaticamente la bd
@EntityScan({"com.dellmdq.springboot.app.commons.models.entity"})//escanee buscando producto en otro package
public class SpringbootServiceItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServiceItemApplication.class, args);
	}

}

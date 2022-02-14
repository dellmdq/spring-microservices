package com.dellmdq.springboot.app.products.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dellmdq.springboot.app.products.models.service.IProductService;
import com.dellmdq.springboot.app.products.models.entity.Product;

@RestController
public class ProductController {
	
	@Autowired
	private Environment env;
	
	@Value("${server.port}")
	private Integer port;
	
	@Autowired
	private IProductService productService;
	
	@GetMapping("/products")
	public List<Product> list() {
		return productService.findAll().stream().map(p -> {
			p.setPort(Integer.parseInt(env.getProperty("local.server.port")));
			
			//en vez de usarr la variable de entorno env usamos @value
			//p.setPort(port);
			return p;
		}).collect(Collectors.toList());
	}
	
	@GetMapping("/products/{id}")
	public Product getById(@PathVariable Long id) {
		Product prod = productService.findById(id);
		prod.setPort(Integer.parseInt(env.getProperty("local.server.port")));
		//prod.setPort(port);
		
//		try {
//			Thread.sleep(2000L);//Timeout en Ribbon o Hystrix es timout default es 1 seg más de eso lanza exception
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return prod;
		
	}
	
	

}

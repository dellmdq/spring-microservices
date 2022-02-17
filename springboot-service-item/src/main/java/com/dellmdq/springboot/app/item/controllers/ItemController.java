package com.dellmdq.springboot.app.item.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dellmdq.springboot.app.item.models.Item;
import com.dellmdq.springboot.app.item.models.Product;
import com.dellmdq.springboot.app.item.models.service.ItemService;

// import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;



@RestController
public class ItemController {
	
	private final Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	/**este sera implementado en el endpoint para usar el circuit breaker*/
	@Autowired
	private CircuitBreakerFactory cbFactory;

	@Autowired
	@Qualifier("serviceFeign")
	//@Qualifier("restClient")
	private ItemService itemService;
	
	@GetMapping("/items")
	public List<Item> getAll(@RequestParam(name = "name", required = false) String name, @RequestHeader(name="token-request", required = false) String token){
		System.out.println(name);
		System.out.println(token);
		return itemService.findAll();
	}

	//empezamos a usar Resilience4j...
	/**Hystrix command. En caso de fallo derivar a tal método que debe
	 * tener la misma firma. */
	//@HystrixCommand(fallbackMethod = "alternativeMethod")
	
	
	@GetMapping("/products/{id}/quantity/{quantity}")
	public Item getById(@PathVariable Long id, @PathVariable Integer quantity) {
		
		/*usamos circuitbreaker ahora
		return itemService.findById(id, quantity);*/
		
		//envolvemos el metodo, 2do param es un metodo alternativo, o excepcion.
		return cbFactory.create("item")//nombre del circuitbreaker
				.run(() -> itemService.findById(id, quantity), exc -> alternativeMethod(id, quantity, exc));
	}
	
	/**Metodo alternativo que será llamado en el fallback*/
	public Item alternativeMethod(Long id, Integer quantity, Throwable exc) {
		
		logger.info(exc.getMessage());
		
		Item item = new Item();
		Product prod = new Product();
		
		item.setQuantity(quantity);
		prod.setId(id);
		prod.setName("Camara Sony");
		prod.setPrice(500.00);
		item.setProd(prod);
		
		return item;
	
	}
}

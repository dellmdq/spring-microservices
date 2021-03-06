package com.dellmdq.springboot.app.item.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dellmdq.springboot.app.item.models.Item;
import com.dellmdq.springboot.app.commons.models.entity.Product;
import com.dellmdq.springboot.app.item.models.service.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

// import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@RefreshScope//permite actualizar los campos, que son injectados con @Value. Cuando estos son modificados se actualiza al nuevo valor 
@RestController
public class ItemController {
	
	private final Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	private Environment env;
	
	@Value("${configuration.text}")
	private String configurationText;
	
	/**este sera implementado en el endpoint para usar el circuit breaker*/
	@Autowired
	private CircuitBreakerFactory<?, ?> cbFactory;

	@Autowired
	@Qualifier("serviceRestClient")
	//@Qualifier("restClient")
	private ItemService itemService;
	
	@GetMapping("/items")
	public List<Item> getAll(@RequestParam(name = "name", required = false) String name, @RequestHeader(name="token-request", required = false) String token){
		System.out.println(name);
		System.out.println(token);
		return itemService.findAll();
	}
	
	//empezamos a usar Resilience4j...
	/**Hystrix command. En caso de fallo derivar a tal m??todo que debe
	 * tener la misma firma. */
	//@HystrixCommand(fallbackMethod = "alternativeMethod")
	@GetMapping("/products/{id}/quantity/{quantity}")
	public Item getById(@PathVariable Long id, @PathVariable Integer quantity) {
		
		/*usamos circuitbreaker ahora
		return itemService.findById(id, quantity);*/
		
		//envolvemos el metodo, 2do param es un metodo alternativo, o excepcion.
		return cbFactory.create("items")//nombre del circuitbreaker
				.run(() -> itemService.findById(id, quantity), exc -> alternativeMethod(id, quantity, exc));
	}
	
	/**CB config using annotations*/
	@CircuitBreaker(name="items", fallbackMethod="alternativeMethod")
	@GetMapping("/products2/{id}/quantity/{quantity}")
	public Item getById2(@PathVariable Long id, @PathVariable Integer quantity) {

		return itemService.findById(id, quantity);
	}
	
	/**Sin circuit breaker pero usando el time limiter para agregar los timeout
	 * a la configuraci??n a traves de esta anotacion. Marcar?? los timeouts pero no
	 * habra pasaje de estados (cerrado, abierto, semi-abierto) ya que no esta el CB.*/
	@TimeLimiter(name="items", fallbackMethod="alternativeMethod2")
	@GetMapping("/products3/{id}/quantity/{quantity}")
	public CompletableFuture<Item>  getById3(@PathVariable Long id, @PathVariable Integer quantity) {

		return CompletableFuture.supplyAsync( () -> itemService.findById(id, quantity));//envolvemos la llamada para poder calcular el tiempo de respuesta
	}
	
	/**Combinamos las anotaciones para tener ambas funcionalidades CB y TL
	 * Tener en cuenta de no usar el fallback method con @TimeLimiter ya que
	 * eso inhabilita el circuitbreaker.*/
	@CircuitBreaker(name="items", fallbackMethod="alternativeMethod2")
	@TimeLimiter(name="items"/*, fallbackMethod="alternativeMethod2"*/)
	@GetMapping("/products4/{id}/quantity/{quantity}")
	public CompletableFuture<Item>  getById4(@PathVariable Long id, @PathVariable Integer quantity) {

		return CompletableFuture.supplyAsync( () -> itemService.findById(id, quantity));//envolvemos la llamada para poder calcular el tiempo de respuesta
	}
	
	/**Metodo alternativo que ser?? llamado en el fallback*/
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
	
	public CompletableFuture<Item> alternativeMethod2(Long id, Integer quantity, Throwable exc) {
		
		logger.info(exc.getMessage());
		
		Item item = new Item();
		Product prod = new Product();
		
		item.setQuantity(quantity);
		prod.setId(id);
		prod.setName("Camara Sony");
		prod.setPrice(500.00);
		item.setProd(prod);
		
		return CompletableFuture.supplyAsync( () ->item);
	
	}
	
	@GetMapping("/config")
	public ResponseEntity<?> getConfig(@Value("${server.port}") String port){
		
		logger.info(configurationText);
		
		Map<String, String> json = new HashMap<>();
		json.put("texto", configurationText);
		json.put("port", port);
		
		if(env.getActiveProfiles().length>0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("author.name", env.getProperty("configuration.author.name"));
			json.put("author.mail", env.getProperty("configuration.author.mail"));
		}
		
		return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
	}
	
	@PostMapping("/products")
	@ResponseStatus(HttpStatus.CREATED)
	public Product create(@RequestBody Product product) {
		return itemService.save(product);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/products/{id}")
	public Product update(@RequestBody Product product, @PathVariable Long id) {
		return itemService.update(product, id);
		
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/products/{id}")
	public void delete(@PathVariable Long id) {
		itemService.delete(id);
	}


}

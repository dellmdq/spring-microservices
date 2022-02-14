package com.dellmdq.springboot.app.item.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dellmdq.springboot.app.item.models.Item;
import com.dellmdq.springboot.app.item.models.Product;
import com.dellmdq.springboot.app.item.models.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class ItemController {

	@Autowired
	@Qualifier("serviceFeign")
	//@Qualifier("restClient")
	private ItemService itemService;
	
	@GetMapping("/items")
	public List<Item> getAll(){
		return itemService.findAll();
	}

	/**Hystrix command. En caso de fallo derivar a tal método que debe
	 * tener la misma firma. */
	@HystrixCommand(fallbackMethod = "alternativeMethod")
	@GetMapping("/products/{id}/quantity/{quantity}")
	public Item getById(@PathVariable Long id, @PathVariable Integer quantity) {
		return itemService.findById(id, quantity);
	}
	
	/**Metodo alternativo que será llamado en el fallback*/
	public Item alternativeMethod(Long id, Integer quantity) {
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

package com.dellmdq.springboot.app.item.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dellmdq.springboot.app.item.models.Item;
import com.dellmdq.springboot.app.item.models.service.ItemService;

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

	@GetMapping("/products/{id}/quantity/{quantity}")
	public Item getById(@PathVariable Long id, @PathVariable Integer quantity) {
		return itemService.findById(id, quantity);
	}
}

package com.dellmdq.springboot.app.item.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.dellmdq.springboot.app.item.clients.ProductRestClient;
import com.dellmdq.springboot.app.item.models.Item;
import com.dellmdq.springboot.app.commons.models.entity.Product;



//@Primary
@Service("serviceFeign")
public class ItemServiceFeign implements ItemService {

	@Autowired
	private ProductRestClient prodRestFeignClient; 
	
	@Override
	public List<Item> findAll() {
		
		return prodRestFeignClient.getAll().stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer quantity) {
		return new Item(prodRestFeignClient.getById(id), quantity);
	}

	@Override
	public Product save(Product product) {
		return prodRestFeignClient.create(product);
	}

	@Override
	public Product update(Product product, Long id) {
		return prodRestFeignClient.update(product, id);
	}

	@Override
	public void delete(Long id) {
		prodRestFeignClient.delete(id);
	}
		
	

}

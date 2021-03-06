package com.dellmdq.springboot.app.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dellmdq.springboot.app.item.models.Item;
import com.dellmdq.springboot.app.commons.models.entity.Product;


@Service("serviceRestClient")
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private RestTemplate restClient;

	@Override
	public List<Item> findAll() {
		//using restTemplate
		//List<Product> products = Arrays.asList(restClient.getForObject("http://localhost:8001/products", Product[].class));
		
		//using ribbon 
		List<Product> products = Arrays.asList(restClient.getForObject("http://service-products/products", Product[].class));
		
		return products.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer quantity) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id.toString());
		Product product = restClient.getForObject("http://service-products/products/{id}", Product.class, params);
		return new Item(product, quantity);
	}

	@Override
	public Product save(Product product) {
		HttpEntity<Product> body = new HttpEntity<Product>(product);
		ResponseEntity<Product> response = restClient.exchange("http://service-products/products", HttpMethod.POST, body, Product.class);
		return response.getBody();
	}

	@Override
	public Product update(Product product, Long id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id.toString());
		
		Product prodToUpdate = restClient.getForObject("http://service-products/products/{id}", Product.class, params);
		prodToUpdate.setName(product.getName());
		prodToUpdate.setPrice(product.getPrice());
		
		HttpEntity<Product> body = new HttpEntity<Product>(prodToUpdate);
		ResponseEntity<Product> response = restClient.exchange("http://service-products/products/{id}", HttpMethod.PUT, body, Product.class, params);
		
		return response.getBody();
	}

	@Override
	public void delete(Long id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id.toString());
		restClient.delete("http://service-products/products/{id}", params);
		
	}

}

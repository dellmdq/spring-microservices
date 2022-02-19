package com.dellmdq.springboot.app.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dellmdq.springboot.app.item.models.Item;
import com.dellmdq.springboot.app.item.models.Product;


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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product update(Product product, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}

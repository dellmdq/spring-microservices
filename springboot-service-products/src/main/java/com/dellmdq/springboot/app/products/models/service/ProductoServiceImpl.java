package com.dellmdq.springboot.app.products.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellmdq.springboot.app.products.models.entity.Product;
import com.dellmdq.springboot.app.products.models.repository.ProductRepository;


@Service
public class ProductoServiceImpl implements IProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	@Transactional
	public List<Product> findAll() {
		return (List<Product>) productRepository.findAll();
	}

	@Override
	public Product findById(Long id) {
		return productRepository.findById(id).orElse(null);
	}

}

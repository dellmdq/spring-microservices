package com.dellmdq.springboot.app.products.models.repository;

import org.springframework.data.repository.CrudRepository;

import com.dellmdq.springboot.app.products.models.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{

}

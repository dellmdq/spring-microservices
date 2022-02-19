package com.dellmdq.springboot.app.item.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dellmdq.springboot.app.item.models.Product;


/**desacoplamos la dirección fisica, ribbon se encarga del direccionamiento)
 * la configuración va al app properties.*/
@FeignClient(name = "service-products")//desacoplamos la dirección fisica ribbon se encarga del direccionamiento, url="localhost:8001")
public interface ProductRestClient {
	
	@GetMapping("/products")//indicamos la ruta endpoint para consumir el API Rest
	public List<Product> getAll();
	
	@GetMapping("/products/{id}")
	public Product getById(@PathVariable Long id);
	
	@PostMapping("/products")
	public Product create(@RequestBody Product product);
	
	@PutMapping("products/{id}")
	public Product update(@RequestBody Product product, @PathVariable Long id);
	
	@DeleteMapping("products/{id}")
	public void delete(@PathVariable Long id);
	
}

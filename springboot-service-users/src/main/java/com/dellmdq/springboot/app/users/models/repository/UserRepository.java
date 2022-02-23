package com.dellmdq.springboot.app.users.models.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.dellmdq.springboot.app.commons.users.models.entity.User;




@RepositoryRestResource(path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	
	@RestResource(path = "findByUsername")//para modificar el path uri del endpoint
	public User findByUsername(@Param("username") String username);//seteamos el param custom
	
	@Query(value = "select u from User u where u.username=?1")
	public User obtenerPorUsername(String username);

}

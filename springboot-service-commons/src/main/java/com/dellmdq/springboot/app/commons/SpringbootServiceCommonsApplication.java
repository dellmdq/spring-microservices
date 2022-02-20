package com.dellmdq.springboot.app.commons;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})//con esta clase deja de configurar automaticamente la bd
public class SpringbootServiceCommonsApplication {

}

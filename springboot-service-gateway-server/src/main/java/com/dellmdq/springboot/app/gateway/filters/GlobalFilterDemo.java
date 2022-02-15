package com.dellmdq.springboot.app.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GlobalFilterDemo implements GlobalFilter, Ordered {
	
	private final Logger logger = LoggerFactory.getLogger(GlobalFilterDemo.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		logger.info("Executing pre filter");
		
		
		/**modding the request with a pre filter. adding a token*/
		exchange.getRequest().mutate().headers(h -> h.add("token", "123456"));
	
		
		//returna la cadena de filtro para seguir aplicando y ejecutando demás filtros o
		//seguir en la llamada del request
		
		return chain.filter(exchange).then(Mono.fromRunnable(()-> {
			logger.info("Executing post filter");
			
			//mostramos el token
			Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent( value -> {
				exchange.getResponse().getHeaders().add("token", value);
			});
			
			
			
			exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
			exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
			
		}));//then maneja el post luego que se terminaron los filtros, usando una expresion lambda
	}

	@Override
	public int getOrder() {
		return -1;
	}

}

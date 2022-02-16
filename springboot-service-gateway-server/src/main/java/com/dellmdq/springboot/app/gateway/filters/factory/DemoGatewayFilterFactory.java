package com.dellmdq.springboot.app.gateway.filters.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class DemoGatewayFilterFactory extends AbstractGatewayFilterFactory<DemoGatewayFilterFactory.Config> {

	private final Logger logger = LoggerFactory.getLogger(DemoGatewayFilterFactory.class);

	public DemoGatewayFilterFactory() {
		super(Config.class);
	}

	



	/**
	 * Aca hacemos algo similar al filtro aplicado en {@code GlobalFilterDemo} pero
	 * de manera más custom y por exchange, no es global.
	 * Se usar OrderedGateWayFilter para indicar el orden de ejecución, la expresion
	 * lambda forma parte del 1er parametro de este new y el 2 indicate el valor de prioridad para 
	 * el metodo. Menor valor indica mayor prioridad.
	 */
	@Override
	public GatewayFilter apply(Config config) {
		return new OrderedGatewayFilter((exchange, chain) -> {

			logger.info("Executing pre gateway filter factory: " + config.message);

			return chain.filter(exchange).then(Mono.fromRunnable(() -> {

				Optional.ofNullable(config.cookieValue).ifPresent(cookie -> {
					exchange.getResponse().addCookie(ResponseCookie.from(config.cookieName, cookie).build());
				});

				logger.info("Executing post gateway filter factory: " + config.message);

			}));

		},2);
	}
	
	/**Esto sirve para configurar los campos del yml de demo en una sola linea*/
	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("mensaje", "cookieName", "cookieValue");
	}
	
	
	/**Sirve para cambiar el nombre de la clase que se debe usar en el archivo 
	 * application*/
//	@Override
//	public String name() {
//		return "ExampleCookie";
//	}



	public static class Config {

		private String message;
		private String cookieValue;
		private String cookieName;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getCookieValue() {
			return cookieValue;
		}

		public void setCookieValue(String cookieValue) {
			this.cookieValue = cookieValue;
		}

		public String getCookieName() {
			return cookieName;
		}

		public void setCookieName(String cookieName) {
			this.cookieName = cookieName;
		}

	}

}

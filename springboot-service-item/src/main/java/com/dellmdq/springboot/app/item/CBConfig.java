package com.dellmdq.springboot.app.item;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;


@Configuration
public class CBConfig {
	
	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer(){
		return factory -> factory.configureDefault(id -> {
			
			return new Resilience4JConfigBuilder(id)
					.circuitBreakerConfig(CircuitBreakerConfig.custom()
							.slidingWindowSize(10)//ventana de cantidad de llamadas para cambio de estado
							.failureRateThreshold(50)//limite general de llamadas con fallos
							.waitDurationInOpenState(Duration.ofSeconds(10L))//tiempo de espera en estado abierto
							.permittedNumberOfCallsInHalfOpenState(5)//numero de llamadas permitidas en estado semi-abierto
							.slowCallRateThreshold(50)//limite de llamadas lentas 50%
							.slowCallDurationThreshold(Duration.ofSeconds(2L))//limite para catalogar llamada como lenta.
							.build())
					.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3L)).build())//timeout general
					.build();
				
		});
	}

}

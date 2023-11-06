package org.ashok.appservice.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Configuration
public class WebEndPoints {

	
	@Bean
	public RouterFunction<ServerResponse> routerFunction(){
		
		return RouterFunctions.route()
					.GET("/invoice-fallback", request -> {
						return ServerResponse.ok().body(Mono.just("System is busy... please try after some time."),
								String.class);
					})
					.POST("/invoice-fallback", request -> {
						return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build();
					})
					.build();
							

	}
}

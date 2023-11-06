package org.ashok.appservice.config;

import java.security.Principal;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {
	
	
	@Bean
	public KeyResolver keyResolver() {
	
		return exchange -> {
			
			return exchange.getPrincipal()
					.map(Principal::getName) // logged in user
					.defaultIfEmpty("anonymous"); //rate limiting across all the users
		};
	}

}

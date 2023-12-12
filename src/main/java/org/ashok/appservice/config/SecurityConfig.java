package org.ashok.appservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {	
	
	//TODO - Make csrf work, for now disabled.
	  	
	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity,
			ReactiveClientRegistrationRepository clientRegistrationRepository ) {
		return httpSecurity
				//.csrf(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.authorizeExchange(exchange -> 
					exchange.anyExchange().authenticated())
				.oauth2Login(Customizer.withDefaults())
				.logout(logout ->logout.logoutSuccessHandler(
											getLogoutSuccessHandler(clientRegistrationRepository)))
				.build();
					
	}
	/**
	 * Defines a repository to store Access Tokens in the web session (redis)
	 * @return
	 */
	
	@Bean
	ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
		return new WebSessionServerOAuth2AuthorizedClientRepository();
	}
	
	private ServerLogoutSuccessHandler getLogoutSuccessHandler(
			ReactiveClientRegistrationRepository clientRegistrationRepository) {
				
		var oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(
												clientRegistrationRepository);
		oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
		
		return oidcLogoutSuccessHandler;
		
	}
}

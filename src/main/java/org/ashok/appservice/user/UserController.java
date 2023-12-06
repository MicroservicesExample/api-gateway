package org.ashok.appservice.user;

import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class UserController {

	@GetMapping("user")
	Mono<User> getUser(@AuthenticationPrincipal OidcUser oidcUser){
		User user =  new User(
						(String)oidcUser.getPreferredUsername(),
						oidcUser.getGivenName(),
						oidcUser.getFamilyName(),
						oidcUser.getAuthorities()
									.stream()
									.map(authority -> authority.getAuthority())
									.collect(Collectors.toList())
						);
							
						
		return Mono.just(user);
	}
}

package org.ashok.appservice.csrf;



import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
/**
 * CSRF is enabled in SecurityConfig.java with defaults. All http methods that update the state on server, like post, put, delete etc should supply the csrf token in the request
 * either as a _csrf request parameter or X-CSRF-TOKEN header.
 * Front end applications should consume the below endpoint for getting the csrf token which needs to be posted in HTTP post, put , delete requests.
 * @author Ashok Mane
 *
 */

@RestController
public class CsrfController {

	@SuppressWarnings("unchecked")
	@GetMapping("/csrf")
	public Mono<CsrfToken> csrfToken(ServerWebExchange exchange) {
		return (Mono<CsrfToken>) exchange.getAttributes().get(CsrfToken.class.getName());
	}
}

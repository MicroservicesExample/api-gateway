package org.ashok.appservice.csrf;



import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(CsrfController.class);
	
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/csrf")
	public Mono<CsrfToken> csrfToken(ServerWebExchange exchange) {
				
		exchange.getSession().doOnNext(websession -> {
						
			Map<String, Object> attrs = websession.getAttributes();
			logger.info(String.format("***session id:%s:",websession.getId()));
			
			for(String key: attrs.keySet()) {
				logger.info(String.format("***session attribute %s:%s", key, attrs.get(key)));
			}

		}).subscribe();
		
		return (Mono<CsrfToken>) exchange.getAttributes().get(CsrfToken.class.getName());
	}
}

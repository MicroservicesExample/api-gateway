package org.ashok.appservice.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.ashok.appservice.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;


@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTests {

	@Autowired
	WebTestClient webTestClient;
	
	@MockBean
	ReactiveClientRegistrationRepository clientRepository;
	
	@Test
	void whenUnauthenticatedThen401() {
		
		webTestClient
			.get()
			.uri("/user")
			.exchange()
			.expectStatus().is3xxRedirection();
			
	}

	@Test
	void whenAuthenticatedReturnUser() {
		var expectedUser = new User("testUser", "firstName", "lastName", List.of("admin", "user"));
		
		webTestClient
			.mutateWith(configureMockLogin(expectedUser))
			.get()
			.uri("/user")
			.exchange()
			.expectStatus().is2xxSuccessful()
			.expectBody(User.class)
			.value(user -> assertThat(user).isEqualTo(expectedUser));
	}

	private WebTestClientConfigurer configureMockLogin(User expectedUser) {
	
		return SecurityMockServerConfigurers.mockOidcLogin()
				.idToken(builder -> {
							builder.claim(StandardClaimNames.PREFERRED_USERNAME, expectedUser.userName());
							builder.claim(StandardClaimNames.GIVEN_NAME, expectedUser.firstName());
							builder.claim(StandardClaimNames.FAMILY_NAME, expectedUser.lastName());
				})
				.authorities(new OAuth2UserAuthority(expectedUser.roles().get(0), Map.of("read","yes","write","yes")), 
							new OAuth2UserAuthority(expectedUser.roles().get(1), Map.of("read","yes")));
			
	}
}

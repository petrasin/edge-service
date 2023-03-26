package dev.mpetrasin.edgeservice.user;

import dev.mpetrasin.edgeservice.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTests {

  @Autowired
  WebTestClient webClient;

  @MockBean
  ReactiveClientRegistrationRepository clientRegistrationRepository;

  @Test
  void whenNotAuthenticatedThen401() {
    webClient.get()
             .uri("/user")
             .exchange()
             .expectStatus().isUnauthorized();

  }

  @Test
  void whenAuthenticatedThenReturnUser() {

    var expectedUser = new User("john.snow", "John", "Snow",
        List.of("employee", "customer"));

    webClient
        .mutateWith(configureMockOidcLogin(expectedUser))
        .get()
        .uri("/user")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(User.class)
        .isEqualTo(expectedUser);
  }

  private SecurityMockServerConfigurers.OidcLoginMutator configureMockOidcLogin(User expectedUser) {

    return SecurityMockServerConfigurers.mockOidcLogin().idToken(
        builder -> builder
            .claim(StandardClaimNames.PREFERRED_USERNAME,
                expectedUser.username())
            .claim(StandardClaimNames.GIVEN_NAME, expectedUser.firstName())
            .claim(StandardClaimNames.FAMILY_NAME, expectedUser.lastName())
            .claim("roles", expectedUser.roles())
    );
  }

}
package dev.mpetrasin.edgeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                   ReactiveClientRegistrationRepository clientRegistrationRepository) {
    return http
        .authorizeExchange(exchange -> exchange
            .pathMatchers("/", "/*.css", "/*.js", "/favicon.ico")
              .permitAll()
            .pathMatchers(HttpMethod.GET,"/books/**")
              .permitAll()
            .anyExchange().authenticated())
        .exceptionHandling(exceptionHandlingSpec ->
            exceptionHandlingSpec.authenticationEntryPoint(
                new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)
            )
        )
        .oauth2Login(Customizer.withDefaults())
        .logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler(
            clientRegistrationRepository
        )))
        .build();
  }

  private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(
      ReactiveClientRegistrationRepository clientRegistrationRepository
  ) {
    var oidLogoutSuccessHandler =
        new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
    oidLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
    return oidLogoutSuccessHandler;
  }


}

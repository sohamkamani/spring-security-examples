package com.sohamkamani.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;
import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Defines the spring security configuration for our application via the `getSecurityFilterChain`
 * method
 */
@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfiguration {

    /**
     * @param httpSecurity is injected by spring security
     * @param jwtSecurityContextRepository is the injected instance of the
     *        JwtSecurityContextRepository class that we defined earlier
     */
    @Bean
    @Autowired
    SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
            // Disable CSRF (not required for this demo)
            .csrf().disable();

        httpSecurity.authorizeHttpRequests()
            .antMatchers("/logout").permitAll();

        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();

        httpSecurity.oauth2Login()
            .defaultSuccessUrl("/welcome", true);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {

        return new InMemoryClientRegistrationRepository(
            CommonOAuth2Provider.GITHUB.getBuilder("github")
                .clientId("a6f4d49ed380c4d80aa0")
                .clientSecret("0a3a74b5d6b56f215aba18fb618693d371c86c07")
                .build());
    }
}

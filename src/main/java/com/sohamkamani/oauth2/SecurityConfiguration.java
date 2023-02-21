package com.sohamkamani.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Defines the spring security configuration for our application via the `getSecurityFilterChain`
 * method
 */
@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    /**
     * @param httpSecurity is injected by spring security
     */
    @Bean
    @Autowired
    SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
            // Disable CSRF (not required for this demo)
            .csrf().disable();

        // authorize all requests coming through, and ensure that they are
        // authenticated
        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();

        // enable oauth2 login, and forward successful logins to the `/welcome` route
        // Here `true` ensures that the user is forwarded to `/welcome` irrespective of
        // the original route that they entered through our application
        httpSecurity.oauth2Login()
            .defaultSuccessUrl("/welcome", true);

        return httpSecurity.build();
    }


    // enabling oauth2 login will not work without defining a
    // `ClientRegistrationRepository` bean. FOr simplicity, we're defining it in the same class
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {

        // Create a new client registration
        return new InMemoryClientRegistrationRepository(
            // spring security provides us pre-populated builders for popular
            // oauth2 providers, like Github, Google, or Facebook.
            // The `CommonOAuth2Provider` contains the definitions for these
            CommonOAuth2Provider.GITHUB.getBuilder("github")
                // In this case, most of the common configuration, like the token and user endpoints
                // are pre-populated. We only need to supply our client ID and secret
                .clientId("a6f4d49ed380c4d80aa0")
                .clientSecret("0a3a74b5d6b56f215aba18fb618693d371c86c07")
                .build());
    }

    // @Bean
    // public ClientRegistrationRepository clientRegistrationRepository() {
    // ClientRegistration.Builder builder = ClientRegistration.withRegistrationId("github");
    // builder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
    // builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
    // builder.redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}");
    // builder.scope("read:user");
    // builder.authorizationUri("https://github.com/login/oauth/authorize");
    // builder.tokenUri("https://github.com/login/oauth/access_token");
    // builder.userInfoUri("https://api.github.com/user");
    // builder.userNameAttributeName("id");
    // builder.clientName("GitHub");
    // builder.clientId("a6f4d49ed380c4d80aa0");
    // builder.clientSecret("0a3a74b5d6b56f215aba18fb618693d371c86c07");
    // // Create a new client registration
    // return new InMemoryClientRegistrationRepository(
    // builder.build());
    // }
}

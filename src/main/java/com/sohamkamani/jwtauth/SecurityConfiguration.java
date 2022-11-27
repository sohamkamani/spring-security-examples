package com.sohamkamani.jwtauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * Defines the spring security configuration for our application via the `getSecurityFilterChain`
 * method
 */
@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    /**
     * @param httpSecurity is injected by spring security
     * @param jwtSecurityContextRepository is the injected instance of the
     *        JwtSecurityContextRepository class that we defined earlier
     */
    @Bean
    @Autowired
    SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity,
            JwtSecurityContextRepository jwtSecurityContextRepository) throws Exception {

        httpSecurity
                // Disable CSRF (not required for this demo)
                .csrf().disable()
                // Configure stateless session management. For JWT based auth, all the user
                // authentication info is self contained in the token itself, so we don't
                // need to store any additional session information
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // Configure the custom authentication strategy we defined earlier
                .sessionAuthenticationStrategy(new JwtSessionAuthenticationStrategy())
                .and()
                .securityContext()
                // Configure the context repository that was injected
                .securityContextRepository(jwtSecurityContextRepository)
                .and()
                .authorizeHttpRequests().anyRequest().authenticated()
                .and()
                // Now when the user navigates to /login (default) they will
                // be taken to a form login page, and be redirected to the "/welcome"
                // route when successfully logged in
                .formLogin()
                .successForwardUrl("/welcome");

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(
            ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    // We can store some sample user names and passwords in an in memory user details manager.
    // Since this method is exposed as a bean it will be auto injected into other spring components
    // Like the JwtSecurityContextRepository class
    @Bean
    public UserDetailsService userDetailsService() {
        // we Are going with to default password encoder or for this example
        // however, in production you should use something more robust, like a BCryptPasswordEncoder
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user1")
                .password("password1")
                .roles("USER")
                .username("user2")
                .password("password2")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}

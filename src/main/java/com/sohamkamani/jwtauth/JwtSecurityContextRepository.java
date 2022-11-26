package com.sohamkamani.jwtauth;

import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtSecurityContextRepository implements SecurityContextRepository {

    private UserDetailsService userDetailsService;

    public JwtSecurityContextRepository(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        Optional<String> maybeToken = JwtUtils.getToken(request);

        SecurityContext context = SecurityContextHolder.getContext();

        if (maybeToken.isEmpty()) {
            return context;
        }

        Optional<DecodedJWT> decodedJWT = JwtUtils.getValidatedToken(maybeToken.get());

        if (decodedJWT.isEmpty()) {
            return context;
        }

        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext();
        applicationContext.scan("com.sohamkamani.jwtauth");
        applicationContext.refresh();

        UserDetailsService userDetailsService =
                applicationContext.getBean(UserDetailsService.class);
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(decodedJWT.get().getClaim("username").asString());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
        context.setAuthentication(usernamePasswordAuthenticationToken);
        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request,
            HttpServletResponse response) {}

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return JwtUtils.getToken(request).isPresent();
    }

}

package com.sohamkamani.jwtauth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * This is our custom session authentication strategy, where we issue a JWT token when the user
 * successfully logs in
 */
public class JwtSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    public JwtSessionAuthenticationStrategy() {}

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request,
            HttpServletResponse response) throws SessionAuthenticationException {
        // Get the authenticated user information
        User user = (User) authentication.getPrincipal();
        // Create a cookie containing the JWT that has the username encoded within its payload
        Cookie jwtCookie = JwtUtils.generateCookie(user.getUsername());
        // Set the cookie via the HTTP response
        response.addCookie(jwtCookie);
    }


}

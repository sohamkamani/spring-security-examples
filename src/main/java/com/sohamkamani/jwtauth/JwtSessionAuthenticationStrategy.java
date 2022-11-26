package com.sohamkamani.jwtauth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

public class JwtSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    public JwtSessionAuthenticationStrategy() {}

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request,
            HttpServletResponse response) throws SessionAuthenticationException {
        User user = (User) authentication.getPrincipal();
        Cookie jwtCookie = JwtUtils.generateCookie(user.getUsername());
        response.addCookie(jwtCookie);
    }


}

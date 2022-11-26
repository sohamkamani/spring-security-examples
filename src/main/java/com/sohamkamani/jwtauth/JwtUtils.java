package com.sohamkamani.jwtauth;

import java.time.Instant;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtils {
    private static final String SECRET_KEY = "my-secret-key";
    private static final String COOKIE_NAME = "token";
    private static final Algorithm JWT_ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    private static final JWTVerifier JWT_VERIFIER = JWT.require(JWT_ALGORITHM).build();
    private static final int MAX_AGE_SECONDS = 120;


    static Cookie generateCookie(String username) {
        Instant now = Instant.now();
        String token =
                JWT.create().withIssuedAt(now).withExpiresAt(now.plusSeconds(MAX_AGE_SECONDS))
                        .withClaim("username", username).sign(JWT_ALGORITHM);
        Cookie jwtCookie = new Cookie(COOKIE_NAME, token);
        jwtCookie.setMaxAge(MAX_AGE_SECONDS);
        return jwtCookie;
    }

    static Optional<String> getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (!cookie.getName().equals(COOKIE_NAME)) {
                continue;
            }
            return Optional.of(cookie.getValue());
        }
        return Optional.empty();
    }

    static Optional<DecodedJWT> getValidatedToken(String token) {
        try {
            return Optional.of(JWT_VERIFIER.verify(token));
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}

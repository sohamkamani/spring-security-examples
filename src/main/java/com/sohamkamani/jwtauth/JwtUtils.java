package com.sohamkamani.jwtauth;

import java.time.Instant;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Contains all the methods needed to encode, decode and verify the JWT
 */
public class JwtUtils {

    // Private key used to sign and verify the JWT
    private static final String SECRET_KEY = "my-secret-key";
    // Name of the cookie to set and retrieve
    static final String COOKIE_NAME = "token";
    // Settings for the JWT configuration, like algorithm, verification method and expiry age
    // In most cases, we can use these settings as is
    private static final Algorithm JWT_ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    private static final JWTVerifier JWT_VERIFIER = JWT.require(JWT_ALGORITHM).build();
    private static final int MAX_AGE_SECONDS = 120;
    private static final int MAX_REFRESH_WINDOW_SECONDS = 30;


    static Cookie generateCookie(String username) {
        // Create a new JWT token string, with the username embedded in the payload
        Instant now = Instant.now();
        String token = JWT.create()
            .withIssuedAt(now)
            .withExpiresAt(now.plusSeconds(MAX_AGE_SECONDS))
            // A "claim" is a single payload value which we can set
            .withClaim("username", username)
            .sign(JWT_ALGORITHM);

        // Create a cookie with the value set as the token string
        Cookie jwtCookie = new Cookie(COOKIE_NAME, token);
        jwtCookie.setMaxAge(MAX_AGE_SECONDS);
        return jwtCookie;
    }

    static Optional<String> getToken(HttpServletRequest request) {
        // Get the cookies from the request
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        // Find the cookie with the cookie name for the JWT token
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (!cookie.getName().equals(COOKIE_NAME)) {
                continue;
            }
            // If we find the JWT cookie, return its value
            return Optional.of(cookie.getValue());
        }
        // Return empty if no cookie is found
        return Optional.empty();
    }

    static Optional<DecodedJWT> getValidatedToken(String token) {
        try {
            // If the token is successfully verified, return its value
            return Optional.of(JWT_VERIFIER.verify(token));
        } catch (JWTVerificationException e) {
            // If the token can't be verified, return an empty value
            return Optional.empty();
        }
    }

    // Gets the expiry timestamp from the request and returns true if it falls
    // within the allowed window, which starts at a given time before expiry
    // in this case, 30s
    static boolean isRefreshable(HttpServletRequest request) {
        Optional<String> token = getToken(request);
        if (token.isEmpty()) {
            return false;
        }
        Instant expiryTime = JWT.decode(token.get()).getExpiresAtAsInstant();
        Instant canBeRefreshedAfter = expiryTime.minusSeconds(MAX_REFRESH_WINDOW_SECONDS);
        return Instant.now().isAfter(canBeRefreshedAfter);
    }

}

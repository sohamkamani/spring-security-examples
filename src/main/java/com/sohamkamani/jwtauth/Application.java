package com.sohamkamani.jwtauth;

import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Declare the main application class. We will define our HTTP routes here
@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// All requests to '/welcome' will be handled by this method
	@RequestMapping("/welcome")
	public String simpleRequest(HttpServletResponse response) {
		// Get the username of the currently logged in user
		Optional<String> username = getUsernameFromSecurityContext();
		if (username.isEmpty()) {
			// if user information cannot be obtained, return
			// a 403 status
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return "error";
		}

		return "welcome " + username.get();
	}

	@GetMapping("/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
		// Get the username of the currently logged in user
		Optional<String> username = getUsernameFromSecurityContext();
		if (username.isEmpty() || !JwtUtils.isRefreshable(request)) {
			// if user information cannot be obtained,
			// or if the token isn't supposed to be refreshed
			// return a 403 status
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}

		Cookie jwtCookie = JwtUtils.generateCookie(username.get());
		// Set the cookie via the HTTP response
		response.addCookie(jwtCookie);
	}

	@GetMapping("/logout")
	public void logout(HttpServletResponse response) {
		response.addCookie(new Cookie(JwtUtils.COOKIE_NAME, null));
	}

	private Optional<String> getUsernameFromSecurityContext() {
		// Get the security context for this request thread
		// and get the principal object from the context
		SecurityContext context = SecurityContextHolder.getContext();
		Object principal = context.getAuthentication().getPrincipal();

		// If the user is authenticated, the principal should be an
		// instance of UserDetails
		if (!(principal instanceof UserDetails)) {
			// if not, return an empty value
			return Optional.empty();
		}

		// Get the username from the userdetails and return
		// the welcome message
		String username =
			((UserDetails) principal).getUsername();
		return Optional.of(username);
	}

}

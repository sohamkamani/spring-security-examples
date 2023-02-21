
package com.sohamkamani.oauth2;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
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
		// this method is defined below
		Optional<String> username = getUsernameFromSecurityContext();
		if (username.isEmpty()) {
			// if user information cannot be obtained, return
			// a 403 status
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return "error";
		}

		return "welcome " + username.get();
	}

	private Optional<String> getUsernameFromSecurityContext() {
		// Get the security context for this request thread
		// and get the principal object from the context
		SecurityContext context = SecurityContextHolder.getContext();
		Object principal = context.getAuthentication().getPrincipal();

		// If the user is authenticated, the principal should be an
		// instance of DefaultOAuth2User
		if (!(principal instanceof DefaultOAuth2User)) {
			// if not, return an empty value
			return Optional.empty();
		}

		// Get the username from the DefaultOAuth2User and return
		// the welcome message
		String username =
			((DefaultOAuth2User) principal).getAttributes().get("login").toString();
		return Optional.of(username);
	}

}

package com.sohamkamani.jwtauth;

import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
		// Get the security context for this request thread
		// and get the principal object from the context
		SecurityContext context = SecurityContextHolder.getContext();
		Object principal = context.getAuthentication().getPrincipal();

		// If the user is authenticated, the principal should be an
		// instance of UserDetails
		if (!(principal instanceof UserDetails)) {
			// if not, return a forbidden status and an error message
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return "error";
		}

		// Get the username from the userdetails and return
		// the welcome message
		String username =
				((UserDetails) principal).getUsername();

		return "welcome " + username;
	}

}

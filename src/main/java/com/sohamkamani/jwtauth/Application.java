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

@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping("/welcome")
	public String simpleRequest(HttpServletResponse response) {
		SecurityContext context = SecurityContextHolder.getContext();

		Object principal = context.getAuthentication().getPrincipal();
		if (!(principal instanceof UserDetails)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return "error";
		}

		String username =
				((UserDetails) principal).getUsername();

		return "welcome " + username;
	}

}

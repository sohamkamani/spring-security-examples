package com.sohamkamani.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/simple-request")
	public String simpleRequest() {
		return "ok";
	}

	@GetMapping("/echo")
	public String echo(@RequestParam(name = "text") String echoText) {
		System.out.println("Input to /echo was: " + echoText);
		return "Echo: " + echoText;
	}

}

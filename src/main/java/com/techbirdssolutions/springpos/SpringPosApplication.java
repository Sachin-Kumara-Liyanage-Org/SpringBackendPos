package com.techbirdssolutions.springpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * This is the main class for the SpringPos application.
 * It is annotated with @SpringBootApplication, indicating it's a Spring Boot application.
 * The main method inside this class serves as the entry point for the application.
 */
@SpringBootApplication
public class SpringPosApplication {
	/**
	 * The main method of the SpringPos application.
	 * This method uses SpringApplication's run method to launch the application.
	 * @param args The command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringPosApplication.class, args);
	}

}

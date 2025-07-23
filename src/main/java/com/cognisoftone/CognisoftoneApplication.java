package com.cognisoftone;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.logging.Logger;

@SpringBootApplication
public class CognisoftoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(CognisoftoneApplication.class, args);
	}

	@Autowired
	private Environment env;

	private final Logger log = Logger.getLogger(CognisoftoneApplication.class.getName());

	@PostConstruct
	public void showProfileMessage() {
		String message = env.getProperty("startup.profile.message", "Sin mensaje de perfil");
		log.info(message);
	}

}

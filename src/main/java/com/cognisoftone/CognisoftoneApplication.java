package com.cognisoftone;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.Arrays;
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
		log.info("Mensaje de perfil: " + env.getProperty("startup.profile.message", "Sin mensaje de perfil"));
		log.info("Perfil activo desde Spring: " + Arrays.toString(env.getActiveProfiles()));
		log.info("SPRING_PROFILES_ACTIVE desde ENV: " + System.getenv("SPRING_PROFILES_ACTIVE"));
	}


}

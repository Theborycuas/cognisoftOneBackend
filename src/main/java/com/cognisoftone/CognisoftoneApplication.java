package com.cognisoftone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class CognisoftoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(CognisoftoneApplication.class, args);
	}

}

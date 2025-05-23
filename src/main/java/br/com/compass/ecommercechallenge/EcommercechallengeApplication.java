package br.com.compass.ecommercechallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EcommercechallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommercechallengeApplication.class, args);
	}

}

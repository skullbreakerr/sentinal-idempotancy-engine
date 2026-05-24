package com.example.sentinal_idempotancy_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients // <--- ADD THIS ANNOTATION
@EnableJpaRepositories(basePackages = "com.example.sentinal_idempotancy_engine.repository")
public class SentinalIdempotancyEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(SentinalIdempotancyEngineApplication.class, args);
	}

}

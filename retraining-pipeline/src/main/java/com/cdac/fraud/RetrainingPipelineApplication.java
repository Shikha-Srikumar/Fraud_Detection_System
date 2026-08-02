package com.cdac.fraud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // This is crucial for background ML retraining 
public class RetrainingPipelineApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetrainingPipelineApplication.class, args);
	}

}


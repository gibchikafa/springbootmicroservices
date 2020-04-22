package com.cleaningschedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.cleaningschedule.presentation")
public class CleaningscheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleaningscheduleApplication.class, args);
	}

}

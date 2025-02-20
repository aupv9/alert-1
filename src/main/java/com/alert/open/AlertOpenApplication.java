package com.alert.open;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.alert.open.entity")
public class AlertOpenApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertOpenApplication.class, args);
	}

}

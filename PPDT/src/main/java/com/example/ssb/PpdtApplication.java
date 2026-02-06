package com.example.ssb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication(scanBasePackages = "com.example.ssb")
@EnableMongoRepositories
public class PpdtApplication {

	public static void main(String[] args) {
		SpringApplication.run(PpdtApplication.class, args);
	}

}

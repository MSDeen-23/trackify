package com.beworkerbee.emailservice;

import com.beworkerbee.emailservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailServiceApplication {

	@Autowired
	private EmailService emailService;
	public static void main(String[] args) {
		SpringApplication.run(EmailServiceApplication.class, args);
	}

}

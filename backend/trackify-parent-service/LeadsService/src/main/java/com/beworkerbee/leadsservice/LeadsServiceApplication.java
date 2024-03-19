package com.beworkerbee.leadsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ComponentScan(basePackages = {"com.beworkerbee.utils","com.beworkerbee.leadsservice"})
@EnableAspectJAutoProxy
public class LeadsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeadsServiceApplication.class, args);
	}

}

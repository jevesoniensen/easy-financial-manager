package com;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes={EfnConfiguration.class})
@SpringBootApplication
public class Application {
    
	public static void main(String[] args) {
	    SpringApplication.run( Application.class, args);
	}
}

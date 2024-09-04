package com.example.nbiot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // This annotation marks the main entry point for a Spring Boot application
public class NbIoTApplication {

    // The main method, which serves as the entry point of the Java application
    public static void main(String[] args) {
        // This line launches the Spring Boot application by initializing the Spring context and running the embedded server
        SpringApplication.run(NbIoTApplication.class, args);
    }
}


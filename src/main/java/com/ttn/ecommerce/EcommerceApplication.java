package com.ttn.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EcommerceApplication {

    public static void main(String[] args) {
        System.out.println("It is working yes.");
        SpringApplication.run(EcommerceApplication.class, args);
    }

}

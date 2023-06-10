package com.nagp.hotelbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCircuitBreaker
@EnableEurekaClient
@SpringBootApplication
@EnableScheduling
public class HotelBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelBookingApplication.class, args);
    }

}

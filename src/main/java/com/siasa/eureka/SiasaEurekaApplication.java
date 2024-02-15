package com.siasa.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SiasaEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiasaEurekaApplication.class, args);
    }

}

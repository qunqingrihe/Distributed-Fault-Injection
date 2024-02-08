package com.example.gatewayagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"config"})
public class GatewayAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayAgentApplication.class, args);
    }

}

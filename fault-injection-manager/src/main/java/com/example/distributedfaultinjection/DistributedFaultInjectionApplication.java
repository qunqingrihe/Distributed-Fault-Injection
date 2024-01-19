package com.example.distributedfaultinjection;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.example.distributedfaultinjection.repository")
public class DistributedFaultInjectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributedFaultInjectionApplication.class, args);
	}

}

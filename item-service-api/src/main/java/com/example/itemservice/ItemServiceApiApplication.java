package com.example.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ItemServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApiApplication.class, args);
	}

}

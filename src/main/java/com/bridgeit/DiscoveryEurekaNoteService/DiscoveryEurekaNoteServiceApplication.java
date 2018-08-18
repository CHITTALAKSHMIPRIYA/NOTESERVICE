package com.bridgeit.DiscoveryEurekaNoteService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableEurekaClient
@SpringBootApplication
@RibbonClient(name="DiscoveryEurekaNoteService")
@EnableFeignClients
public class DiscoveryEurekaNoteServiceApplication  {
	public static void main(String[] args) {
		SpringApplication.run(DiscoveryEurekaNoteServiceApplication.class, args);
	}
}

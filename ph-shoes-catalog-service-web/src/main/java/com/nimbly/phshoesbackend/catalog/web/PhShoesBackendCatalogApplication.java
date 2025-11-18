package com.nimbly.phshoesbackend.catalog.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport
@SpringBootApplication(scanBasePackages = "com.nimbly.phshoesbackend.catalog")
public class PhShoesBackendCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhShoesBackendCatalogApplication.class, args);
	}

}

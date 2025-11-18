package com.nimbly.phshoesbackend.catalog.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport
@EntityScan("com.nimbly.phshoesbackend.catalog.core.model")
@EnableJpaRepositories("com.nimbly.phshoesbackend.catalog.core.repository.jpa")
@SpringBootApplication(scanBasePackages = "com.nimbly.phshoesbackend.catalog")
public class PhShoesBackendCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhShoesBackendCatalogApplication.class, args);
	}

}

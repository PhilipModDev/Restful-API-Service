package com.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ServiceApplication {

	public static final Logger LOGGER = LoggerFactory.getLogger(ServiceApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(ServiceApplication.class)
				.headless(false)
				.bannerMode(Banner.Mode.OFF)
				.logStartupInfo(false)
				.run(args);
	}
}

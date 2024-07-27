package com.backend.application.WebApp;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class WebAppApplication {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(WebAppApplication.class);
		builder.bannerMode(Banner.Mode.OFF);
		builder.logStartupInfo(false);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);
	}

}

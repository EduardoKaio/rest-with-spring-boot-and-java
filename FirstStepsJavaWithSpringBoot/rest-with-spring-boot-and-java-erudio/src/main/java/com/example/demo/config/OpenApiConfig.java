package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.AbstractListenerWriteFlushProcessor;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

		@Bean
		public OpenAPI customOpenAPI() {
			return new OpenAPI()
					.info(new Info()
						.title("RESTful API with Java 21 and Spring Boot 3.1.7")
						.version("v1")
						.description("Some description about your API")
						.termsOfService("https://kaioeduardo.com")
						.license(
							new License()
								.name("Apache 2.0")
								.url("https://kaioeduardo.com"))
						); 
		}
}

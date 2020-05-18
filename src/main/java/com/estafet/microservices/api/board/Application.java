package com.estafet.microservices.api.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public io.opentracing.Tracer jaegerTracer() {
		return new com.uber.jaeger.Configuration(System.getenv("JAEGER_SERVICE_NAME"),
				com.uber.jaeger.Configuration.SamplerConfiguration.fromEnv(),
				com.uber.jaeger.Configuration.ReporterConfiguration.fromEnv()).getTracer();
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

}

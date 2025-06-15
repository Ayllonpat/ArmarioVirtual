package com.trianasalesianos.dam.ArmarioVirtual;

import com.trianasalesianos.dam.ArmarioVirtual.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class ArmarioVirtualApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ArmarioVirtualApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ArmarioVirtualApplication.class, args);
	}
}

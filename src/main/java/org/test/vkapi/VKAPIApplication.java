package org.test.vkapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.test.vkapi.config.VkProperties;

@SpringBootApplication
@EnableConfigurationProperties(VkProperties.class)
public class VKAPIApplication {
	static {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
	}
	public static void main(String[] args) {
		SpringApplication.run(VKAPIApplication.class, args);
	}
}

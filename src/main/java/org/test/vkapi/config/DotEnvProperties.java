package org.test.vkapi.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotEnvProperties {
    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }
}

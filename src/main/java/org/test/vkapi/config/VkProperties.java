package org.test.vkapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "vk")
public class VkProperties {
    private String urlApi;
    private String apiMethodUsersGet;
    private String apiVersion;
    private String accessToken;
    private String fields;
    private String defaultYear;
}

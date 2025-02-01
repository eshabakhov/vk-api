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
    private String urlVkApi;
    private String methodUsersGetVkApi;
    private String vkApiVersion;
    private String accessToken;
    private String fields;
}

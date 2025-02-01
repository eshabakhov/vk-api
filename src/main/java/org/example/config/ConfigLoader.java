package org.example.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class ConfigLoader {
    private static final Properties properties = new Properties();
    private static final Dotenv dotenv = Dotenv.load(); // Загружаем .env

    static {
        try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            log.error("Ошибка загрузки application.properties: {}", e.getMessage());
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);

        if (value != null && value.contains("${")) {
            int start = value.indexOf("${") + 2;
            int end = value.indexOf("}");
            if (start > 1 && end > start) {
                String envKey = value.substring(start, end);
                String envValue = dotenv.get(envKey);
                if (envValue != null) {
                    return envValue;
                }
            }
        }
        return value;
    }
}

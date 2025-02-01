package org.test.vkapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "excel")
public class ExcelProperties {
    private String filePath;
    private String fileName;
    private String sheetName;
}

package org.test.vkapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorResponseDTO {
    @JsonProperty(value = "http_code")
    private Short httpCode;
    private String message;
}

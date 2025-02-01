package org.test.vkapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserContactsDTO {
    @JsonProperty(value = "mobile_phone")
    private String mobilePhone;
    @JsonProperty(value = "home_phone")
    private String homePhone;
}

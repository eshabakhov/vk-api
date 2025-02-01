package org.test.vkapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;
    private String bdate;
    @JsonProperty(value = "city")
    private UserCityDTO userCity;
    @JsonProperty(value = "contacts")
    private UserContactsDTO userContacts;
}

package org.test.vkapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseList {
    private List<UserDTO> response;
}

package org.test.vkapi.restclient;

import lombok.AllArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.test.vkapi.config.VkProperties;
import org.test.vkapi.dto.ResponseList;
import org.test.vkapi.dto.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Component
@AllArgsConstructor
public class RestClientUser {

    private final RestTemplate restTemplate;
    private final VkProperties vkProperties;

    private static final String PARAM_API_VERSION = "v";
    private static final String PARAM_ACCESS_TOKEN = "access_token";
    private static final String PARAM_USERS_IDS = "user_ids";
    private static final String PARAM_FIELDS = "fields";

    public ResponseList getUsers(List<User> userList) throws URISyntaxException {
        URI uri = new URIBuilder(String.format("%s%s", vkProperties.getUrlApi(), vkProperties.getApiMethodUsersGet()))
                .addParameter(PARAM_API_VERSION, vkProperties.getApiVersion())
                .addParameter(PARAM_ACCESS_TOKEN, vkProperties.getAccessToken())
                .addParameter(PARAM_USERS_IDS, userList.stream().map(User::getUserId).toList()
                        .toString().replace("[", "").replace("]", ""))
                .addParameter(PARAM_FIELDS, vkProperties.getFields())
                .build();

        return restTemplate.getForObject(uri, ResponseList.class);
    }
}

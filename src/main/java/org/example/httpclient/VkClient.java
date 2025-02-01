package org.example.httpclient;


import org.apache.http.client.utils.URIBuilder;
import org.example.config.ConfigLoader;
import org.example.dto.User;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class VkClient {
    private final String VK_URL_API = ConfigLoader.get("vk.urlApi");
    private final String VK_API_METHOD_USERS_GET = ConfigLoader.get("vk.apiMethodUsersGet");
    private final String VK_API_VERSION = ConfigLoader.get("vk.apiVersion");
    private final String VK_ACCESS_TOKEN = ConfigLoader.get("vk.accessToken");
    private final String VK_FIELDS = ConfigLoader.get("vk.fields");

    private static final String PARAM_API_VERSION = "v";
    private static final String PARAM_ACCESS_TOKEN = "access_token";
    private static final String PARAM_USERS_IDS = "user_ids";
    private static final String PARAM_FIELDS = "fields";

    private final HttpClient httpClient;

    public VkClient() {
        httpClient = HttpClient.newHttpClient();
    }

    public HttpResponse<String> getUsers(List<User> userList) throws IOException, InterruptedException, URISyntaxException {
        URI uri = new URIBuilder(String.format("%s%s", VK_URL_API, VK_API_METHOD_USERS_GET))
                .addParameter(PARAM_API_VERSION, VK_API_VERSION)
                .addParameter(PARAM_ACCESS_TOKEN, VK_ACCESS_TOKEN)
                .addParameter(PARAM_USERS_IDS, userList.stream().map(User::getUserId).toList()
                        .toString().replace("[", "").replace("]", ""))
                .addParameter(PARAM_FIELDS, VK_FIELDS)
                .build();
        return httpClient.send(HttpRequest.newBuilder().
                uri(uri).build(), HttpResponse.BodyHandlers.ofString());
    }
}

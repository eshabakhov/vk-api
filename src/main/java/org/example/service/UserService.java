package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.UserDAO;
import org.example.dto.User;
import org.example.httpclient.VkClient;
import org.example.mapper.JSONUserMapper;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getUsers(int page, int pageSize) {
        return userDAO.getUsersPageable(page, pageSize);
    }

    public void update() throws InterruptedException {
        log.info("Start data update in user_info");
        long usersCount = userDAO.count();
        int pageSize = 500;
        int nThreads = 100;
        long tasksCount = usersCount / pageSize + 1;
        CountDownLatch latch = new CountDownLatch((int) tasksCount);
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        for (int i = 1; i <= tasksCount; i++) {
            int page = i;
            executor.submit(() -> {
                try {
                    Thread.sleep(page * 1000L);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
                List<User> userList = restResponseUsers(page, pageSize);
                userList.stream().parallel().forEach(userDAO::updateUsers);
                latch.countDown();
            });
        }
        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        log.info("Data by user_info updated successfully!");
    }

    private List<User> restResponseUsers(int page, int pageSize) {
        VkClient vkClient = new VkClient();
        List<User> userList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(vkClient.getUsers(getUsers(page, pageSize)).body().toString());
            JSONArray jsonResponse = jsonObject.getJSONArray("response");
            for (int i = 0; i < jsonResponse.length(); i++) {
                if (!jsonResponse.isNull(i)) {
                    JSONUserMapper mapper = new JSONUserMapper();
                    User user = mapper.map(jsonResponse.getJSONObject(i));
                    if (Objects.nonNull(user)) {
                        userList.add(user);
                    }
                }
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            log.error(e.getMessage(), e);
        }
        return userList;
    }
}

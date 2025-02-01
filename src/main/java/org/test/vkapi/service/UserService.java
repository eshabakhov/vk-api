package org.test.vkapi.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.vkapi.config.ExcelProperties;
import org.test.vkapi.dto.ResponseList;
import org.test.vkapi.dto.User;
import org.test.vkapi.excel.ExcelUserWriter;
import org.test.vkapi.exception.UserNotFoundException;
import org.test.vkapi.mapper.UserDTOUserMapper;
import org.test.vkapi.repository.UserRepository;
import org.test.vkapi.restclient.RestClientUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RestClientUser restClientUser;

    private final ExcelUserWriter excelUserWriter;

    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            System.out.println(1);
            return new UserNotFoundException(id);
        });
    }

    public List<User> list(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(Math.toIntExact(page - 1), Math.toIntExact(pageSize));
        return userRepository.findAll(pageable).stream().toList();
    }

    public void updateAllUsers() throws InterruptedException {
        log.info("Start updating users...");
        long usersCount = userRepository.count();
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
                List<User> userList = restResponseToListUser(page, pageSize);
                userRepository.saveAll(userList);
                latch.countDown();
            });
        }
        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        log.info("Users updated successfully!");
    }

    public ByteArrayOutputStream downloadFile() throws InterruptedException {
        return excelUserWriter.writeUsersToByteArrayOutputStream();
    }

    private List<User> restResponseToListUser(int page, int pageSize)  {
        List<User> userList = new ArrayList<>();
        try {
            ResponseList list = restClientUser.getUsers(list(page, pageSize));
            list.getResponse().stream()
                    .filter(Objects::nonNull)
                    .forEach(e -> userList.add(new UserDTOUserMapper().map(e)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return userList;
    }
}
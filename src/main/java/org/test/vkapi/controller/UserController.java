package org.test.vkapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.test.vkapi.dto.User;
import org.test.vkapi.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/list")
    public List<User> get(@RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return userService.list(page, pageSize);
    }

    @PatchMapping
    public void update() throws InterruptedException {
        userService.updateAllUsers();
    }
}

package org.test.vkapi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.vkapi.dto.User;
import org.test.vkapi.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/{id}")
    public User get(@PathVariable Long id) {
        return userService.get(id);
    }

    @GetMapping(value = "/list")
    public List<User> getList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return userService.list(page, pageSize);
    }

    @PatchMapping
    public void update() throws InterruptedException {
        userService.updateAllUsers();
    }

    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> download() throws InterruptedException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(userService.downloadFile().toByteArray());
    }
}

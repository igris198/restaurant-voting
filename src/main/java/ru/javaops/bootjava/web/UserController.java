package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.model.Role;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.security.SecurityUtil;
import ru.javaops.bootjava.service.UserService;
import ru.javaops.bootjava.util.ValidationUtil;

import java.util.Collections;

@RestController
@RequestMapping(value = UserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "UserController", description = "Operations with its own user")
public class UserController {
    public static final String REST_URL = "/api/users";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get your user")
    @GetMapping // 18
    public ResponseEntity<User> get() {
        return ResponseEntity.of(userService.get(SecurityUtil.authUserId()));
    }

    @Operation(summary = "Change your user")
    @PutMapping // 20
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody @Valid User user) {
        int id = SecurityUtil.authUserId();
        ValidationUtil.assureIdConsistent(user, id);
        userService.updateUser(id, user, Collections.singleton(Role.USER));
    }

    @Operation(summary = "Deleting your user")
    @DeleteMapping // 21
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser() {
        userService.delete(SecurityUtil.authUserId());
    }
}

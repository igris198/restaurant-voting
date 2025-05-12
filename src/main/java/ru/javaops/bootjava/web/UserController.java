package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.model.Role;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.security.SecurityUtil;
import ru.javaops.bootjava.util.ValidationUtil;

import java.util.Collections;

@RestController
@RequestMapping(value = UserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "UserController", description = "Operations with its own user")
public class UserController {
    public static final String REST_URL = "/api/users";

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary = "Get your user")
    @GetMapping // 18
    public ResponseEntity<User> get() {
        return ResponseEntity.of(userRepository.findById(SecurityUtil.authUserId()));
    }

    @Operation(summary = "Change your user")
    @PutMapping // 20
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateUser(@RequestBody @Valid User user) {
        int id = SecurityUtil.authUserId();
        ValidationUtil.assureIdConsistent(user, id);
        user.setRoles(Collections.singleton(Role.USER));
        ValidationUtil.checkNotFound(userRepository.save(user), id);
    }

    @Operation(summary = "Deleting your user")
    @DeleteMapping // 21
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteUser() {
        int id = SecurityUtil.authUserId();
        ValidationUtil.checkNotFound(userRepository.delete(id), id);
    }
}

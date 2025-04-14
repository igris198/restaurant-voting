package ru.javaops.bootjava.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Role;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.util.SecurityUtil;
import ru.javaops.bootjava.util.ValidationUtil;

import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping(value = UserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    static final String REST_URL = "/api/user";

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping // 18
    public ResponseEntity<User> get() {
        return ResponseEntity.of(userRepository.findById(SecurityUtil.authUserId()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // 19
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        ValidationUtil.checkIsNew(user);
        Assert.notNull(user, "user must not be null");
        user.setRoles(Collections.singleton(Role.USER));
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping // 20
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody User user) {
        Assert.notNull(user, "user must not be null");
        int id = SecurityUtil.authUserId();
        ValidationUtil.assureIdConsistent(user, id);
        user.setRoles(Collections.singleton(Role.USER));
        ValidationUtil.checkNotFound(userRepository.save(user), id);
    }

    @DeleteMapping // 21
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser() {
        int id = SecurityUtil.authUserId();
        ValidationUtil.checkNotFound(userRepository.delete(id), id);
    }
}

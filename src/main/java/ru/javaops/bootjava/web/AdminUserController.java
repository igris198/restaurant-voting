package ru.javaops.bootjava.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Role;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.util.ValidationUtil;

import java.net.URI;
import java.util.List;
import java.util.Set;

import static ru.javaops.bootjava.util.ValidationUtil.assureIdConsistent;
import static ru.javaops.bootjava.util.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserController {
    static final String REST_URL = "/api/admin/user";
    private final Logger log = LoggerFactory.getLogger(AdminUserController.class);
    private final UserRepository userRepository;

    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PatchMapping(value = "/{id}/role", consumes = MediaType.APPLICATION_JSON_VALUE) // 13
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createRoles(@PathVariable int id, @RequestBody Set<Role> roles) {
        log.info("AdminUserController createRoles. id = {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id = " + id + " not found"));
        user.setRoles(roles);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // 14
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        log.info("AdminUserController createUser");
        ValidationUtil.checkIsNew(user);
        Assert.notNull(user, "user must not be null");
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping // 15
    public List<User> getUsers() {
        log.info("AdminUserController getUsers");
        return userRepository.findAll();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE) // 16
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable int id, @RequestBody @Valid User user) {
        log.info("AdminUserController updateUser id={}", id);
        assureIdConsistent(user, id);
        Assert.notNull(user, "user must not be null");
        checkNotFound(userRepository.save(user), id);
    }

    @DeleteMapping("/{id}") // 17
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("AdminUserController delete id={}", id);
        userRepository.delete(id);
    }

    @GetMapping("/{id}") // 26
    public ResponseEntity<User> get(@PathVariable int id) {
        log.info("AdminUserController get id={}", id);
        return ResponseEntity.of(userRepository.findById(id));
    }

    @PatchMapping(value = "/{id}/enable") //25
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setEnabled(@PathVariable int id, @RequestParam boolean isEnabled) {
        log.info("AdminUserController setEnabled id={}", id);
        userRepository.enable(id, isEnabled);
    }
}

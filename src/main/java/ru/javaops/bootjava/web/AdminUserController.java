package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Role;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.service.UserService;
import ru.javaops.bootjava.to.AuthResponseTo;

import java.net.URI;
import java.util.List;
import java.util.Set;

import static ru.javaops.bootjava.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "AdminUserController", description = "Administrative operations with users")
public class AdminUserController {
    public static final String REST_URL = "/api/admin/users";

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Change user roles")
    @PatchMapping(value = "/{id}/role", consumes = MediaType.APPLICATION_JSON_VALUE) // 13
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createRoles(@PathVariable int id, @RequestBody @Valid Set<Role> roles) {
        userService.createRoles(id, roles);
    }

    @Operation(summary = "Adding user with any role")
    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE) // 14
    public ResponseEntity<AuthResponseTo> createUser(@RequestBody @Valid User user) {
        AuthResponseTo authResponseTo = userService.createAndAuthUser(user, user.getRoles()); // при самостоятельной регистрации пользователя будет только одна роль
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(authResponseTo.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(authResponseTo);
    }

    @Operation(summary = "Get a list of all users with roles")
    @GetMapping // 15
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @Operation(summary = "Change any user")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE) // 16
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable int id, @RequestBody @Valid User user) {
        assureIdConsistent(user, id);
        userService.updateUser(id, user, user.getRoles()); // при самостоятельном обновлении данных пользователя будет только одна роль
    }

    @Operation(summary = "Remove any user")
    @DeleteMapping("/{id}") // 17
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}") // 26
    public ResponseEntity<User> get(@PathVariable int id) {
        return ResponseEntity.of(userService.get(id));
    }

    @Operation(summary = "Allow/Deny user")
    @PatchMapping(value = "/{id}/enable") //25
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setEnabled(@PathVariable int id, @RequestParam boolean isEnabled) {
        userService.enable(id, isEnabled);
    }
}

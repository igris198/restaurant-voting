package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Role;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.service.UserService;
import ru.javaops.bootjava.to.AuthResponseTo;
import ru.javaops.bootjava.to.AuthTo;

import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping(value = AuthController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "AuthController", description = "Authorization and registration")
public class AuthController {
    public static final String REST_URL = "/api/auth";

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "User registration with USER role")
    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE) // 31
    public ResponseEntity<AuthResponseTo> createUser(@RequestBody @Valid User user) {
        AuthResponseTo authResponseTo = userService.createAndAuthUser(user, Collections.singleton(Role.USER)); // при регистрации пользователя администратором будет любой набор ролей
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(authResponseTo.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(authResponseTo);
    }

    @Operation(summary = "User login")
    @PostMapping("/login") // 30
    public ResponseEntity<AuthResponseTo> performLogin(@RequestBody @Valid AuthTo authTo) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authTo.email(), authTo.password()));
            return ResponseEntity.ok(userService.getAuthUser(authTo));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

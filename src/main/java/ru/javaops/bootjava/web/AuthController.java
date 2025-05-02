package ru.javaops.bootjava.web;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Role;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.security.JWTUtil;
import ru.javaops.bootjava.to.AuthResponseTo;
import ru.javaops.bootjava.to.AuthTo;
import ru.javaops.bootjava.util.ValidationUtil;

import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping(value = AuthController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    static final String REST_URL = "/api/auth";

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JWTUtil jwtUtil, AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE) // 31
    public ResponseEntity<AuthResponseTo> createUser(@RequestBody @Valid User user) {
        ValidationUtil.checkIsNew(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        User created = userRepository.save(user);
        String token = jwtUtil.generateToken(created.getEmail());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(new AuthResponseTo(
                created.id(),
                created.getEmail(),
                created.getRoles(),
                token));
    }

    @PostMapping("/login") // 30
    public AuthResponseTo performLogin(@RequestBody @Valid AuthTo authTo) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authTo.email(),
                        authTo.password());

        authenticationManager.authenticate(authInputToken);

        User user = ValidationUtil.checkNotFound(userRepository.getByEmail(authTo.email().toLowerCase()), "");
        String token = jwtUtil.generateToken(authTo.email());
        return new AuthResponseTo(
                user.id(),
                user.getEmail(),
                user.getRoles(),
                token);
    }
}

package ru.javaops.bootjava.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javaops.bootjava.model.Role;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.security.AuthorizedUser;
import ru.javaops.bootjava.security.JWTUtil;
import ru.javaops.bootjava.to.AuthResponseTo;
import ru.javaops.bootjava.to.AuthTo;
import ru.javaops.bootjava.util.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.javaops.bootjava.util.ValidationUtil.checkNotFound;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JWTUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = checkNotFound(userRepository.getByEmail(email.toLowerCase()), "User with email '" + email + "' was not found");
        return new AuthorizedUser(user);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void createRoles(int userId, Set<Role> roles) {
        User user = checkNotFound(userRepository.findById(userId).orElse(null), userId);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public AuthResponseTo createAndAuthUser(User user, Set<Role> roles) {
        Assert.notNull(user, "user must not be null");
        ValidationUtil.checkIsNew(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        User createdUser = userRepository.save(user);
        String token = jwtUtil.generateToken(createdUser.getEmail());
        return new AuthResponseTo(
                createdUser.id(),
                createdUser.getEmail(),
                createdUser.getRoles(),
                token);
    }

    @Cacheable("users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void updateUser(int userId, User user, Set<Role> roles) {
        user.setRoles(roles);
        checkNotFound(userRepository.save(user), userId);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int userId) {
        ValidationUtil.checkNotFound(userRepository.delete(userId), userId);
    }

    public Optional<User> get(int userId) {
        return userRepository.findById(userId);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void enable(int userId, boolean isEnabled) {
        userRepository.enable(userId, isEnabled);
    }

    public AuthResponseTo getAuthUser(AuthTo authTo) throws BadCredentialsException {
        User user = ValidationUtil.checkNotFound(userRepository.getByEmail(authTo.email().toLowerCase()), "");
        String token = jwtUtil.generateToken(authTo.email());
        return new AuthResponseTo(user.id(), user.getEmail(), user.getRoles(), token);
    }
}

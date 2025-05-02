package ru.javaops.bootjava.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.security.AuthorizedUser;
import ru.javaops.bootjava.util.ValidationUtil;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = ValidationUtil.checkNotFound(userRepository.getByEmail(email.toLowerCase()), "User with email '" + email + "' was not found");
        return new AuthorizedUser(user);
    }
}

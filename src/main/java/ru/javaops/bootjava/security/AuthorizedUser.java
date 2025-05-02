package ru.javaops.bootjava.security;

import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.to.AuthResponseTo;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {

    private AuthResponseTo authResponseTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                user.getRoles());
        setAuthResponseTo(new AuthResponseTo(
                user.getId(),
                user.getEmail(),
                user.getRoles(),
                null));
    }

    public AuthResponseTo getAuthResponseTo() {
        return authResponseTo;
    }

    public void setAuthResponseTo(AuthResponseTo authResponseTo) {
        this.authResponseTo = authResponseTo;
    }
}

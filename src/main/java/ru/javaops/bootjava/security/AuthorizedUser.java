package ru.javaops.bootjava.security;

import org.springframework.security.core.GrantedAuthority;
import ru.javaops.bootjava.model.Role;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.to.AuthResponseTo;

import java.util.Collection;
import java.util.Set;

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
                user.isEnabled(),
                user.getRoles(),
                null));
    }
    public void setAuthResponseTo(AuthResponseTo authResponseTo) {
        this.authResponseTo = authResponseTo;
    }

    public AuthResponseTo getAuthResponseTo() {
        return authResponseTo;
    }
}

package ru.javaops.bootjava.to;

import ru.javaops.bootjava.model.Role;

import java.util.Set;

public record AuthResponseTo(
        Integer id,
        String email,
        boolean enabled,
        Set<Role> roles,
        String accessToken) {
}

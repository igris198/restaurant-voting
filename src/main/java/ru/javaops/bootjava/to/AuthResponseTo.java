package ru.javaops.bootjava.to;

import ru.javaops.bootjava.model.Role;

import java.util.Set;

public record AuthResponseTo(
        Integer id,
        String email,
        Set<Role> roles,
        String accessToken) {
}

package ru.javaops.bootjava.to;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserTo(
        @NotBlank @Size(min = 1, max = 255) String name,
        @Email @NotBlank @Size(max = 128) String email,
        @NotBlank @Size(min = 6, max = 128) String password,
        boolean enabled) {
    public UserTo(String name, String email, String password, boolean enabled) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }
}

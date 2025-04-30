package ru.javaops.bootjava.to;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthTo(
        @Email @NotBlank @Size(max = 128) String email,
        @NotBlank @Size(min = 6, max = 128) String password) {
}

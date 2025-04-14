package ru.javaops.bootjava.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestaurantTo(
        @NotNull int Id,
        @NotBlank @Size(min = 1, max = 255) String name) {
}

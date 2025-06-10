package ru.javaops.bootjava.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MealTo(
        @NotBlank @Size(min = 2, max = 255) String name,
        @NotNull BigDecimal price) {
}

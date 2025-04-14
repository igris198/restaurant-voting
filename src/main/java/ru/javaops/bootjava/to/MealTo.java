package ru.javaops.bootjava.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MealTo(
        @NotBlank String name,
        @NotNull BigDecimal price) {
}

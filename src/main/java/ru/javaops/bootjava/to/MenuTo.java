package ru.javaops.bootjava.to;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record MenuTo(@NotEmpty @Valid List<MealTo> meals) {
}

package ru.javaops.bootjava.to;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record MenuTo(@NotEmpty @Valid Set<MealTo> meals) {
}

package ru.javaops.bootjava.to;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record MenuTo(
        @NotNull LocalDate menuDate,
        @NotEmpty @Valid List<MealTo> meals
) {
}

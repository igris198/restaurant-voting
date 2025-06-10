package ru.javaops.bootjava.to;

import jakarta.validation.constraints.NotNull;

public record VoteTo(
        @NotNull int restaurantId
) {
}

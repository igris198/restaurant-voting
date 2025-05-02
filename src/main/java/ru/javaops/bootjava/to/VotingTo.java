package ru.javaops.bootjava.to;

import java.time.LocalDate;

public record VotingTo(
        int Id,
        int userId,
        int restaurantId,
        LocalDate votingDate) {
}

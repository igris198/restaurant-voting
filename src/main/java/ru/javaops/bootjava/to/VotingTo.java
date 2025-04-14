package ru.javaops.bootjava.to;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record VotingTo(
        @NotNull int Id,
        @NotNull int userId,
        @NotNull int restaurantId,
        @NotNull LocalDate votingDate) {
    public VotingTo(
            int Id,
            int userId,
            int restaurantId,
            LocalDate votingDate) {
        this.Id = Id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.votingDate = votingDate;
    }
}

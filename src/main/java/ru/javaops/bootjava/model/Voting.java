package ru.javaops.bootjava.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "voting", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "voting_date"}, name = "voting_unique_user_date_idx")})
public class Voting extends AbstractBaseEntity {
    @Column(name = "voting_date", nullable = false)
    @NotNull
    private LocalDate votingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Voting() {
    }

    public Voting(User user, Restaurant restaurant) {
        this(null, user, restaurant);
    }

    public Voting(Integer id, User user, Restaurant restaurant) {
        super(id);
        this.votingDate = LocalDate.now();
        this.user = user;
        this.restaurant = restaurant;
    }

    public @NotNull LocalDate getVotingDate() {
        return votingDate;
    }

    public void setVotingDate(@NotNull LocalDate votingDate) {
        this.votingDate = votingDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

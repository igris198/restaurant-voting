package ru.javaops.bootjava.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "voting", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "voting_date"}, name = "voting_unique_user_date_idx")})
@Getter
@Setter
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
}

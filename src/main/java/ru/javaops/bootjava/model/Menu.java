package ru.javaops.bootjava.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "menu_date"}, name = "menu_unique_restaurant_date_idx")})
public class Menu extends AbstractBaseEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    Set<Meal> meals;
    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate menuDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Restaurant restaurant;

    public Menu() {
    }

    public Menu(LocalDate menuDate, Restaurant restaurant) {
        this.menuDate = (menuDate == null) ? LocalDate.now() : menuDate;
        this.restaurant = restaurant;
    }

    public void setMeals(Set<Meal> meals) {
        meals.forEach(meal -> meal.setMenu(this));
        this.meals = meals;
    }
}

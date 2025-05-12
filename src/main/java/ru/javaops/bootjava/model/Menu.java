package ru.javaops.bootjava.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "menu_date"}, name = "menu_unique_restaurant_date_idx")})
public class Menu extends AbstractBaseEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    List<Meal> meals;

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

    public Menu(LocalDate menuDate, Restaurant restaurant, List<Meal> meals) {
        this(menuDate, restaurant);
        meals.forEach(meal -> meal.setMenu(this));
        this.meals = meals;
    }

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<Meal> meals) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        meals.forEach(meal -> meal.setMenu(this));
        this.meals = meals;
    }

    public @NotNull LocalDate getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(@NotNull LocalDate menuDate) {
        this.menuDate = menuDate;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

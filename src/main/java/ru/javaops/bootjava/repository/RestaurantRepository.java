package ru.javaops.bootjava.repository;

import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {

    List<Restaurant> getWithMenusAndMeals(Integer restaurantId, LocalDate menuDate);

    List<RestaurantTo> getAllTos();

    Optional<RestaurantTo> getTo(Integer restaurantId);

    <S extends Restaurant> S save(S entity);

    void deleteById(Integer integer);

    Restaurant getReferenceById(Integer restaurantId);

    Optional<Restaurant> findById(Integer restaurantId);

}

package ru.javaops.bootjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menus m LEFT JOIN FETCH m.meals " +
            "WHERE r.id=COALESCE(:restaurantId, r.id) AND " +
            "m.menuDate=COALESCE(:menuDate, CURRENT_DATE)" +
            "ORDER BY r.id ASC, m.menuDate DESC")
    List<Restaurant> getWithMenusAndMeals(@Param("restaurantId") Integer restaurantId, @Param("menuDate") LocalDate menuDate);

    @Query("SELECT new ru.javaops.bootjava.to.RestaurantTo(r.id, r.name) FROM Restaurant r")
    List<RestaurantTo> getAllTos();

    @Query("SELECT new ru.javaops.bootjava.to.RestaurantTo(r.id, r.name) FROM Restaurant r WHERE r.id=:restaurantId")
    Optional<RestaurantTo> getTo(@Param("restaurantId") Integer restaurantId);
}

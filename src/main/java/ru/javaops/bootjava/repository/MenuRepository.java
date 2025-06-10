package ru.javaops.bootjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.model.Menu;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.meals JOIN FETCH m.restaurant " +
            "WHERE m.restaurant.id=:restaurantId AND m.menuDate=:menuDate ")
    Optional<Menu> getMenu(@Param("restaurantId") Integer restaurantId, @Param("menuDate") LocalDate menuDate);

    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.restaurant.id=:id AND m.menuDate=:menuDate")
    int deleteByRestaurantAndDate(@Param("id") int id, @Param("menuDate") LocalDate menuDate);

}
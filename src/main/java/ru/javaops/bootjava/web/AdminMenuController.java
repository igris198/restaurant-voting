package ru.javaops.bootjava.web;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Meal;
import ru.javaops.bootjava.model.Menu;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.repository.MenuRepository;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.to.MealTo;
import ru.javaops.bootjava.to.MenuTo;
import ru.javaops.bootjava.util.ValidationUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuController {
    static final String REST_URL = "/api/admin/restaurant/{id}/menu/{date}";

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public AdminMenuController(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // 10
    public ResponseEntity<Menu> addMenu(
            @PathVariable int id,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody @Valid MenuTo menuTo) {
        Set<MealTo> mealTos = menuTo.meals();
        Assert.notNull(mealTos, "meals in menu must not be null");

        Restaurant restaurant = ValidationUtil.checkNotFound(restaurantRepository.findById(id).orElse(null), id);
        Menu menu = new Menu(date, restaurant);
        menu.setMeals(meatTosToMeals(mealTos));
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(restaurant.id(), created.getMenuDate()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // 11
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMenu(
            @PathVariable int id,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody @Valid MenuTo menuTo) {
        Set<MealTo> mealTos = menuTo.meals();
        Assert.notNull(mealTos, "meals in menu must not be null");

        Menu menu = ValidationUtil.checkNotFound(
                menuRepository.getMenu(id, date).stream().findAny().orElse(null),
                "Menu with restaurantId = " + id + " and menuDate = " + date + "not found");

        menu.getMeals().clear();
        menu.setMeals(meatTosToMeals(mealTos));

        ValidationUtil.checkNotFound(menuRepository.save(menu), menu.id());
    }

    @DeleteMapping // 12
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenu(
            @PathVariable int id,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        menuRepository.deleteByRestaurantAndDate(id, date);
    }

    private Set<Meal> meatTosToMeals(Set<MealTo> mealTos) {
        return mealTos.stream().map(mealTo -> new Meal(null, mealTo.name(), mealTo.price())).collect(Collectors.toSet());
    }
}

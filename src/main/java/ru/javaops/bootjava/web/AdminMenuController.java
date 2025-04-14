package ru.javaops.bootjava.web;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant with id = " + id + " not found"));

        Set<MealTo> mealTos = menuTo.meals();
        Assert.notNull(mealTos, "meals in menu must not be null");

        Menu menu = new Menu(date, restaurant);
        menu.setMeals(meatTosToMeals(mealTos));
        Assert.notNull(menu, "menu must not be null");

        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(restaurant.getId(), created.getMenuDate()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // 11
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMenu(
            @PathVariable int id,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody @Valid MenuTo menuTo) {
        Assert.notNull(menuTo, "menu must not be null");
        Menu menu = menuRepository.getMenu(id, date).stream().findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu with restaurantId = " + id + " and menuDate = " + date + "not found"));

        Set<MealTo> mealTos = menuTo.meals();
        Assert.notNull(mealTos, "meals in menu must not be null");

        menu.getMeals().clear();
        menu.setMeals(meatTosToMeals(mealTos));
        Assert.notNull(menu, "menu must not be null");
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

package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.service.RestaurantService;
import ru.javaops.bootjava.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "RestaurantController", description = "Common restaurant operations")
public class RestaurantController {
    public static final String REST_URL = "/api/restaurants";

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Operation(summary = "List of all restaurants")
    @GetMapping // 3
    public List<RestaurantTo> findAll() {
        return restaurantService.getAllTos();
    }

    @Operation(summary = "Find a restaurant by id")
    @GetMapping("/{id}") // 4
    public ResponseEntity<RestaurantTo> get(@PathVariable int id) {
        return ResponseEntity.of(restaurantService.getTo(id));
    }

    @Operation(summary = "View restaurant menu by date")
    @GetMapping("/with-menus") // 2
    public List<Restaurant> get(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "restaurantId", required = false) Integer restaurantId) {
        return restaurantService.getWithMenusAndMeals(date, restaurantId);
    }

    @Operation(summary = "View the menus of all restaurants for today.")
    @GetMapping("/with-menus/today") // 1
    @Cacheable("restaurantMenuToday")
    public List<Restaurant> getToday() {
        return restaurantService.getWithMenusAndMeals(null, null);
    }

}

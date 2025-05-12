package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.repository.DataJpaRestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "RestaurantMenuController", description = "Common menu operations")
public class RestaurantMenuController {
    public static final String REST_URL = "/api/restaurants/menus";

    private final DataJpaRestaurantRepository restaurantRepository;

    public RestaurantMenuController(DataJpaRestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Operation(summary = "View restaurant menu by date")
    @GetMapping // 2
    public List<Restaurant> get(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "restaurantId", required = false) Integer restaurantId) {
        return restaurantRepository.getWithMenusAndMeals(restaurantId, date);
    }
}

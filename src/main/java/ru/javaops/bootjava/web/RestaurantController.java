package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.bootjava.service.RestaurantService;
import ru.javaops.bootjava.to.RestaurantTo;

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
}

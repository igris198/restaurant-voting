package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.repository.RestaurantRepository;

import java.net.URI;

import static ru.javaops.bootjava.util.ValidationUtil.assureIdConsistent;
import static ru.javaops.bootjava.util.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "AdminRestaurantController", description = "Restaurant administration")
public class AdminRestaurantController {
    static final String REST_URL = "/api/admin/restaurant";

    private final RestaurantRepository restaurantRepository;

    public AdminRestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Operation(summary = "Addition of restaurant")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // 7
    public ResponseEntity<Restaurant> create(@RequestBody @Valid Restaurant restaurant) {
        Restaurant created = restaurantRepository.save(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "Restaurant removal")
    @DeleteMapping("/{id}") // 8
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        restaurantRepository.deleteById(id);
    }

    @Operation(summary = "Restaurant change")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE) // 9
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @RequestBody @Valid Restaurant restaurant) {
        assureIdConsistent(restaurant, id);
        checkNotFound(restaurantRepository.save(restaurant), id);
    }
}

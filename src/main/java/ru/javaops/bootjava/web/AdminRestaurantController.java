package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.repository.DataJpaRestaurantRepository;

import java.net.URI;

import static ru.javaops.bootjava.util.ValidationUtil.assureIdConsistent;
import static ru.javaops.bootjava.util.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "AdminRestaurantController", description = "Restaurant administration")
public class AdminRestaurantController {
    public static final String REST_URL = "/api/admin/restaurants";

    private final DataJpaRestaurantRepository restaurantRepository;

    public AdminRestaurantController(DataJpaRestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Operation(summary = "Addition of restaurant")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // 7
    @Transactional
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
    @Transactional
    public void delete(@PathVariable int id) {
        restaurantRepository.deleteById(id);
    }

    @Operation(summary = "Restaurant change")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE) // 9
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@PathVariable int id, @RequestBody @Valid Restaurant restaurant) {
        assureIdConsistent(restaurant, id);
        checkNotFound(restaurantRepository.save(restaurant), id);
    }
}

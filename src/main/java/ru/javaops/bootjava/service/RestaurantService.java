package ru.javaops.bootjava.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.repository.DataJpaRestaurantRepository;
import ru.javaops.bootjava.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ru.javaops.bootjava.util.ValidationUtil.checkNotFound;

@Service
public class RestaurantService {

    private final DataJpaRestaurantRepository restaurantRepository;

    public RestaurantService(DataJpaRestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @CacheEvict(value = "restaurantTos", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @CacheEvict(value = "restaurantTos", allEntries = true)
    public void update(int restaurantId, Restaurant restaurant) {
        checkNotFound(restaurantRepository.save(restaurant), restaurantId);
    }

    @CacheEvict(value = "restaurantTos", allEntries = true)
    public void delete(int restaurantId) {
        restaurantRepository.deleteById(restaurantId);
    }

    @Cacheable("restaurantTos")
    public List<RestaurantTo> getAllTos() {
        return restaurantRepository.getAllTos();
    }

    public Optional<RestaurantTo> getTo(int restaurantId) {
        return restaurantRepository.getTo(restaurantId);
    }

    public List<Restaurant> getWithMenusAndMeals(LocalDate date, Integer restaurantId) {
        return restaurantRepository.getWithMenusAndMeals(restaurantId, date);
    }

}

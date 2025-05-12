package ru.javaops.bootjava.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaRestaurantRepository implements RestaurantRepository {

    private final CrudRestaurantRepository crudRestaurantRepository;

    public DataJpaRestaurantRepository(CrudRestaurantRepository crudRestaurantRepository) {
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Override
    public List<Restaurant> getWithMenusAndMeals(Integer restaurantId, LocalDate menuDate) {
        return crudRestaurantRepository.getWithMenusAndMeals(restaurantId, menuDate);
    }

    @Cacheable("restaurantTos")
    @Override
    public List<RestaurantTo> getAllTos() {
        return crudRestaurantRepository.getAllTos();
    }

    @Override
    public Optional<RestaurantTo> getTo(Integer restaurantId) {
        return crudRestaurantRepository.getTo(restaurantId);
    }

    @CacheEvict(value = "restaurantTos", allEntries = true)
    @Override
    public <S extends Restaurant> S save(S entity) {
        return crudRestaurantRepository.save(entity);
    }

    @CacheEvict(value = "restaurantTos", allEntries = true)
    @Override
    public void deleteById(Integer integer) {
        crudRestaurantRepository.deleteById(integer);
    }

    @Override
    public Restaurant getReferenceById(Integer restaurantId) {
        return crudRestaurantRepository.getReferenceById(restaurantId);
    }

    @Override
    public Optional<Restaurant> findById(Integer restaurantId) {
        return crudRestaurantRepository.findById(restaurantId);
    }
}

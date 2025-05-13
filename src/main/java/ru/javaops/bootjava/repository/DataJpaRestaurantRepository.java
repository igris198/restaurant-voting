package ru.javaops.bootjava.repository;

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

    @Override
    public List<RestaurantTo> getAllTos() {
        return crudRestaurantRepository.getAllTos();
    }

    @Override
    public Optional<RestaurantTo> getTo(Integer restaurantId) {
        return crudRestaurantRepository.getTo(restaurantId);
    }

    @Override
    public <S extends Restaurant> S save(S entity) {
        return crudRestaurantRepository.save(entity);
    }

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

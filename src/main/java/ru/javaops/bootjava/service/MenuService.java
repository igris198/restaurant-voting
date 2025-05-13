package ru.javaops.bootjava.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javaops.bootjava.model.Meal;
import ru.javaops.bootjava.model.Menu;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.repository.MenuRepository;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.to.MealTo;
import ru.javaops.bootjava.to.MenuTo;
import ru.javaops.bootjava.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Menu createMenu(int restaurantId, LocalDate date, MenuTo menuTo) {
        List<MealTo> mealTos = menuTo.meals();
        Assert.notNull(mealTos, "meals in menu must not be null");
        Restaurant restaurant = ValidationUtil.checkNotFound(restaurantRepository.findById(restaurantId).orElse(null), restaurantId);

        return menuRepository.save(new Menu(date, restaurant, mealTosToMeals(mealTos)));
    }

    @Transactional
    public void updateMenu(int restaurantId, LocalDate date, MenuTo menuTo) {
        List<MealTo> mealTos = menuTo.meals();
        Assert.notNull(mealTos, "meals in menu must not be null");

        Menu menu = ValidationUtil.checkNotFound(
                menuRepository.getMenu(restaurantId, date),
                "Menu with restaurantId = " + restaurantId + " and menuDate = " + date + "not found");

        menu.getMeals().clear();
        menu.getMeals().addAll(mealTosToMeals(mealTos));
        ValidationUtil.checkNotFound(menuRepository.save(menu), menu.id());
    }

    public void deleteMenu(int restaurantId, LocalDate date) {
        menuRepository.deleteByRestaurantAndDate(restaurantId, date);
    }

    private List<Meal> mealTosToMeals(List<MealTo> mealTos) {
        return mealTos.stream().map(mealTo -> new Meal(null, mealTo.name(), mealTo.price())).toList();
    }
}

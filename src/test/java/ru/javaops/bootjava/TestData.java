package ru.javaops.bootjava;

import ru.javaops.bootjava.model.*;
import ru.javaops.bootjava.to.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ru.javaops.bootjava.model.AbstractBaseEntity.START_SEQ;

public class TestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int GUEST_ID = START_SEQ + 2;

    public static final int RESTO_ID = START_SEQ + 3;

    public static final int INSERTED_ID = START_SEQ + 20;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "$2a$10$C8XHUDql9pRRWZGCrdPouufaY8KrpjfC5dQuu573iorYhDUN501Km", true, null, Set.of(Role.USER));
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "$2a$10$3AC7nMQnJuXjPAssQy2frOvS/rkzVESLxOjJ5wiE8JTjyMtXCvwe6", true, null, Set.of(Role.ADMIN, Role.USER));
    public static final User guest = new User(GUEST_ID, "Guest", "guest@gmail.com", "$2a$10$noLk0msQB6fmeJCEGTu9yun.Sr8heXFa5nTSkm84uHd3xtdEwK2B2", false, null, Collections.emptySet());

    public static final List<User> users = List.of(user, admin, guest);

    public static final User testUser = new User(INSERTED_ID, "Test User", "testuser@gmail.com", "guest", true, null, Set.of(Role.USER));

    public static final AuthTo testAuthTo = new AuthTo("user@yandex.ru", "password");
    public static final AuthTo testWrongAuthTo = new AuthTo("user@yandex.ru", "WrongPassword");

    public static final AuthResponseTo testAuthResponseTo = new AuthResponseTo(INSERTED_ID, "testuser@gmail.com", Set.of(Role.USER), null);
    public static final AuthResponseTo userAuthResponseTo = new AuthResponseTo(USER_ID, "user@yandex.ru", Set.of(Role.USER), null);

    public static final Restaurant restaurant = new Restaurant(RESTO_ID, "First resto");

    public static List<Restaurant> restaurantsWithMenus() {
        Restaurant testRestaurant = new Restaurant(restaurant);
        testRestaurant.setMenus(Set.of(getMenuRorResto()));
        return List.of(testRestaurant);
    }

    public static final RestaurantTo restaurantTo1 = new RestaurantTo(RESTO_ID, "First resto");
    public static final RestaurantTo restaurantTo2 = new RestaurantTo(RESTO_ID + 1, "Second resto");
    public static final RestaurantTo restaurantTo3 = new RestaurantTo(RESTO_ID + 2, "Третий ресторан");
    public static final List<RestaurantTo> restaurantTos = List.of(restaurantTo1, restaurantTo2, restaurantTo3);

    public static final Restaurant newRestaurant = new Restaurant(INSERTED_ID, "Test resto");

    public static final MealTo mealTo0 = new MealTo("Вода", new BigDecimal("55"));
    public static final MealTo mealTo1 = new MealTo("Салат", new BigDecimal("100"));
    public static final MealTo mealTo2 = new MealTo("Шашлык", new BigDecimal("500"));
    public static final MealTo mealTo3 = new MealTo("Десерт", new BigDecimal("350"));

    public static final List<MealTo> mealTosForCreate = List.of(mealTo1, mealTo2, mealTo3);
    public static final List<MealTo> mealTosForUpdate = List.of(mealTo0, mealTo1, mealTo2);

    public static final Meal meal0 = new Meal(START_SEQ+20, "Вода", new BigDecimal("55"));
    public static final Meal meal1 = new Meal(START_SEQ+21, "Салат", new BigDecimal("100"));
    public static final Meal meal2 = new Meal(START_SEQ+22,"Шашлык", new BigDecimal("500"));
    public static final Meal meal3 = new Meal(START_SEQ+23,"Десерт", new BigDecimal("350"));

    public static final Meal mealForMenu1 = new Meal(START_SEQ+20, "Завтрак", new BigDecimal("500.01"));
    public static final Meal mealForMenu2 = new Meal(START_SEQ+21, "Салат", new BigDecimal("1000.02"));
    public static final List<Meal> mealsForResto  = List.of(mealForMenu1, mealForMenu2);

    public static Menu getMenuRorResto() {
        return new Menu(null, LocalDate.now().minusDays(1), restaurant, mealsForResto);
    }

    public static final List<Meal> mealsForCreate  = List.of(meal1, meal2, meal3);
    public static final List<Meal> mealsForUpdate = List.of(meal0, meal1, meal2);

    public static MenuTo getNewMenuTo() {
        return new MenuTo(mealTosForCreate);
    }

    public static MenuTo getUpdatedMenuTo() {
        return new MenuTo(mealTosForUpdate);
    }

    public static Menu getNewMenu() {
        return new Menu(null, LocalDate.now().plusDays(1), restaurant, mealsForCreate);
    }

    public static Menu getUpdatedMenu() {
        return new Menu(START_SEQ+8, LocalDate.now(), restaurant, mealsForUpdate);
    }

    public static User getUpdatedUser() {
        User updated = new User(user);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Set.of(Role.USER));
        return updated;
    }

    public static final VotingTo insertedVotingTo = new VotingTo(INSERTED_ID, USER_ID, RESTO_ID, LocalDate.now());
    public static final VotingTo votingTo = new VotingTo(START_SEQ+18, ADMIN_ID, RESTO_ID, LocalDate.now());
    public static final VotingTo votingTo2 = new VotingTo(START_SEQ+19, USER_ID, RESTO_ID, LocalDate.now().minusDays(1));

    public static final List<VotingTo> votingTos = List.of(votingTo);
    public static final List<VotingTo> allVotingTos = List.of(votingTo, votingTo2);
}

package ru.javaops.bootjava;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.util.json.JsonUtil;
import ru.javaops.bootjava.web.AdminRestaurantController;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.TestData.*;

public class AdminRestaurantControllerTest extends AbstractControllerTest {
    private static final MatcherFactory.Matcher<Restaurant> RESTO_MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void create() throws Exception {
        perform(MockMvcRequestBuilders.post(AdminRestaurantController.REST_URL)
                .header("Authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(RESTO_MATCHER.contentJson(newRestaurant));

        Restaurant resultRestaurant = restaurantRepository.findById(INSERTED_ID).orElse(null);
        RESTO_MATCHER.assertMatch(resultRestaurant, newRestaurant);

    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(AdminRestaurantController.REST_URL + "/{id}", RESTO_ID)
                .header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(restaurantRepository.findById(RESTO_ID).isPresent());
    }

    @Test
    void update() throws Exception {
        Restaurant updatedRestaurant = newRestaurant;
        updatedRestaurant.setId(RESTO_ID);

        perform(MockMvcRequestBuilders.put(AdminRestaurantController.REST_URL + "/{id}", RESTO_ID)
                .header("Authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedRestaurant)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Restaurant resultRestaurant = restaurantRepository.findById(RESTO_ID).orElse(null);
        RESTO_MATCHER.assertMatch(resultRestaurant, newRestaurant);
    }

    @Test
    void updateWrongAuth() throws Exception {
        Restaurant updatedRestaurant = newRestaurant;
        updatedRestaurant.setId(RESTO_ID);

        perform(MockMvcRequestBuilders.put(AdminRestaurantController.REST_URL + "/{id}", RESTO_ID)
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedRestaurant)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}

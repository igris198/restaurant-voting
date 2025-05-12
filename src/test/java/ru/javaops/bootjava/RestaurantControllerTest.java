package ru.javaops.bootjava;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.bootjava.to.RestaurantTo;
import ru.javaops.bootjava.web.RestaurantController;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.TestData.*;

public class RestaurantControllerTest extends AbstractControllerTest {
    private static final MatcherFactory.Matcher<RestaurantTo> RESTO_TO_MATCHER = MatcherFactory.usingEqualsComparator(RestaurantTo.class);

    @Test
    void findAll() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantController.REST_URL)
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTO_TO_MATCHER.contentJson(restaurantTos));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantController.REST_URL + "/{id}", RESTO_ID)
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTO_TO_MATCHER.contentJson(restaurantTo1));
    }
}

package ru.javaops.bootjava;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.bootjava.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.TestData.RESTO_ID;
import static ru.javaops.bootjava.TestData.restaurantsWithMenus;

public class RestaurantMenuControllerTest extends AbstractControllerTest {
    private static final MatcherFactory.Matcher<Restaurant> RESTO_MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);

    @Test
    void get() throws Exception {
        List<Restaurant> restaurants = restaurantsWithMenus();

        perform(MockMvcRequestBuilders.get(RestaurantMenuController.REST_URL)
                .header("Authorization", adminToken)
                .param("date", LocalDate.now().minusDays(1).toString())
                .param("restaurantId", String.valueOf(RESTO_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTO_MATCHER.contentJson(restaurants));
    }
}

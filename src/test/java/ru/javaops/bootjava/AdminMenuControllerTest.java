package ru.javaops.bootjava;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.bootjava.model.Menu;
import ru.javaops.bootjava.repository.MenuRepository;
import ru.javaops.bootjava.to.MenuTo;
import ru.javaops.bootjava.util.ValidationUtil;
import ru.javaops.bootjava.util.exception.NotFoundException;
import ru.javaops.bootjava.util.json.JsonUtil;
import ru.javaops.bootjava.web.AdminMenuController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.TestData.getNewMenu;
import static ru.javaops.bootjava.model.AbstractBaseEntity.START_SEQ;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminMenuControllerTest extends AbstractControllerTest {
    private static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant", "menu.restaurant");

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void addMenu() throws Exception {
        MenuTo newMenuTo = TestData.getNewMenuTo();

        ResultActions action = perform(MockMvcRequestBuilders.post(
                        AdminMenuController.REST_URL,
                        TestData.restaurant.id(),
                        LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE))
                .header("Authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Menu resultMenu = MENU_MATCHER.readFromJson(action);
        resultMenu.getMeals().forEach(meal -> meal.setMenu(null));
        Menu newMenu = getNewMenu();
        newMenu.setId(resultMenu.id());
        newMenu.getMeals().forEach(meal -> meal.setMenu(null));
        MENU_MATCHER.assertMatch(resultMenu, newMenu);
        Menu savedMenu = menuRepository.getMenu(TestData.restaurant.id(), LocalDate.now().plusDays(1));
        savedMenu.getMeals().forEach(meal -> meal.setMenu(null));
        MENU_MATCHER.assertMatch(savedMenu, newMenu);
    }

    @Test
    void updateMenu() throws Exception {
        MenuTo newMenuTo = TestData.getUpdatedMenuTo();
        perform(MockMvcRequestBuilders.put(
                        AdminMenuController.REST_URL,
                        TestData.restaurant.id(),
                        LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                .header("Authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Menu resultMenu = menuRepository.getMenu(START_SEQ + 3, LocalDate.now());
        resultMenu.getMeals().forEach(meal -> meal.setMenu(null));
        Menu updatedMenu = TestData.getUpdatedMenu();
        updatedMenu.getMeals().forEach(meal -> meal.setMenu(null));
        MENU_MATCHER.assertMatch(resultMenu, updatedMenu);
    }

    @Test
    void deleteMenu() throws Exception {
        perform(MockMvcRequestBuilders.delete(
                        AdminMenuController.REST_URL,
                        TestData.restaurant.id(),
                        LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                .header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> ValidationUtil.checkNotFound(menuRepository.getMenu(TestData.restaurant.id(), LocalDate.now()), ""));
    }

    @Test
    void deleteMenuUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(
                        AdminMenuController.REST_URL,
                        TestData.restaurant.id(),
                        LocalDate.now().format(DateTimeFormatter.ISO_DATE)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
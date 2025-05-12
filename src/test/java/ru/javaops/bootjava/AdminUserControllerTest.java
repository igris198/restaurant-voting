package ru.javaops.bootjava;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.bootjava.model.Role;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.util.json.JsonUtil;
import ru.javaops.bootjava.web.AdminUserController;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.TestData.*;

public class AdminUserControllerTest extends AbstractControllerTest {
    private static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingEqualsComparator(User.class);
    private static final MatcherFactory.Matcher<Role> ROLE_MATCHER = MatcherFactory.usingEqualsComparator(Role.class);

    @Autowired
    private UserRepository userRepository;

    @Test
    void createRoles() throws Exception {
        Set<Role> roles = Set.of(Role.USER, Role.ADMIN);
        perform(MockMvcRequestBuilders.patch(AdminUserController.REST_URL + "/{id}/role", USER_ID)
                .header("Authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(roles)))
                .andDo(print())
                .andExpect(status().isNoContent());
        User updatedUser = userRepository.findById(USER_ID).orElse(null);
        assertNotNull(updatedUser);
        ROLE_MATCHER.assertMatch(updatedUser.getRoles(), roles);
    }

    @Test
    void getUsers() throws Exception {
        perform(MockMvcRequestBuilders.get(AdminUserController.REST_URL)
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(new ArrayList<>(users)));
    }

    @Test
    void updateUser() throws Exception {
        User updatedUser = getUpdatedUser();
        updatedUser.setId(null);

        perform(MockMvcRequestBuilders.put(AdminUserController.REST_URL + "/{id}", USER_ID)
                .header("Authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedUser)))
                .andDo(print())
                .andExpect(status().isNoContent());
        User resultUser = userRepository.findById(USER_ID).orElse(null);
        USER_MATCHER.assertMatch(resultUser, getUpdatedUser());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(AdminUserController.REST_URL + "/{id}", USER_ID)
                .header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(userRepository.findById(USER_ID).isPresent());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(AdminUserController.REST_URL + "/{id}", USER_ID)
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void setDisabled() throws Exception {
        perform(MockMvcRequestBuilders.patch(AdminUserController.REST_URL + "/{id}/enable", USER_ID)
                .header("Authorization", adminToken)
                .param("isEnabled", "false"))
                .andDo(print())
                .andExpect(status().isNoContent());
        User updatedUser = userRepository.findById(USER_ID).orElse(null);
        assertNotNull(updatedUser);
        assertFalse(updatedUser.isEnabled());
    }

    @Test
    void setEnabled() throws Exception {
        perform(MockMvcRequestBuilders.patch(AdminUserController.REST_URL + "/{id}/enable", GUEST_ID)
                .header("Authorization", adminToken)
                .param("isEnabled", "true"))
                .andDo(print())
                .andExpect(status().isNoContent());
        User updatedUser = userRepository.findById(GUEST_ID).orElse(null);
        assertNotNull(updatedUser);
        assertTrue(updatedUser.isEnabled());
    }
}


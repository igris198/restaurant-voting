package ru.javaops.bootjava;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.bootjava.model.User;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.to.AuthResponseTo;
import ru.javaops.bootjava.util.json.JsonUtil;
import ru.javaops.bootjava.web.AuthController;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.bootjava.TestData.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AuthControllerTest extends AbstractControllerTest {
    private static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "password", "registered");
    private static final MatcherFactory.Matcher<AuthResponseTo> AUTH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(AuthResponseTo.class, "accessToken");

    @Autowired
    private UserRepository userRepository;

    @Test
    void createUser() throws Exception {
        User newTestUser = new User(testUser);
        newTestUser.setId(null);
        perform(MockMvcRequestBuilders.post(AuthController.REST_URL + "/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTestUser)))
                .andExpect(status().isCreated())
                .andExpect(AUTH_MATCHER.contentJson(testAuthResponseTo));
        User resultUser = userRepository.findById(INSERTED_ID).orElse(null);
        assertNotNull(resultUser);
        USER_MATCHER.assertMatch(resultUser, testUser);
    }

    @Test
    void performSuccessLogin() throws Exception {
        perform(MockMvcRequestBuilders.post(AuthController.REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(testAuthTo)))
                .andExpect(status().isOk())
                .andExpect(AUTH_MATCHER.contentJson(userAuthResponseTo));
    }

    @Test
    void performWrongPasswordLogin() throws Exception {
        perform(MockMvcRequestBuilders.post(AuthController.REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(testWrongAuthTo)))
                .andExpect(status().isUnauthorized());
    }
}

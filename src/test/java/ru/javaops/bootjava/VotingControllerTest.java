package ru.javaops.bootjava;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.model.Voting;
import ru.javaops.bootjava.repository.VotingRepository;
import ru.javaops.bootjava.to.VotingTo;
import ru.javaops.bootjava.util.VotingUtil;
import ru.javaops.bootjava.web.VotingController;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class VotingControllerTest extends AbstractControllerTest {
    private static final MatcherFactory.Matcher<VotingTo> MATCHER = MatcherFactory.usingEqualsComparator(VotingTo.class);

    @Autowired
    VotingRepository votingRepository;

    @Test
    @CacheEvict(value = "users", allEntries = true)
    void create() throws Exception {
        ResultActions actions = perform(MockMvcRequestBuilders.post(VotingController.REST_URL + "/{restaurantId}", TestData.RESTO_ID)
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        VotingTo resultVotingTo = MATCHER.readFromJson(actions);
        MATCHER.assertMatch(resultVotingTo, TestData.insertedVotingTo);
        Voting createdVoting = votingRepository.findById(resultVotingTo.Id()).orElse(null);
        assertNotNull(createdVoting);
        MATCHER.assertMatch(VotingUtil.getTo(createdVoting), TestData.insertedVotingTo);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(VotingController.REST_URL + "/{restaurantId}", TestData.RESTO_ID)
                .header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getVoting() throws Exception {
        perform(MockMvcRequestBuilders.get(VotingController.REST_URL)
                .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(TestData.votingTos));

    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(VotingController.REST_URL + "/all")
                .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentJson(TestData.allVotingTos));
    }

/*  нерабочий тест. Разобраться позже
    @Test
    public void updateBefore11() throws Exception {
        try (MockedStatic<LocalTime> mocked = mockStatic(LocalTime.class);
             MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            LocalTime fixedTime = LocalTime.of(10, 30);
            mocked.when(LocalTime::now).thenReturn(fixedTime);
            mockedSecurityUtil.when(SecurityUtil::authUserId).thenReturn(ADMIN_ID);

            LocalTime now = LocalTime.now();
            System.out.println(now);
            votingController.update(MenuTestData.RESTO_ID + 2);
            Voting updatedVoting = votingRepository.findByUserIdAndCurrentDate(ADMIN_ID).orElse(null);
            MATCHER.assertMatch(VotingUtil.getTo(updatedVoting), MenuTestData.updatedVotingTo);
        }
    }

 */
}

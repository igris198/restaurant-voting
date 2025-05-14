package ru.javaops.bootjava.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.model.Voting;
import ru.javaops.bootjava.repository.DataJpaRestaurantRepository;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.repository.VotingRepository;
import ru.javaops.bootjava.security.SecurityUtil;
import ru.javaops.bootjava.to.VotingTo;
import ru.javaops.bootjava.util.ValidationUtil;
import ru.javaops.bootjava.util.VotingUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VotingService {

    private final VotingRepository votingRepository;
    private final DataJpaRestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public VotingService(VotingRepository votingRepository, DataJpaRestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.votingRepository = votingRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VotingTo create(Integer restaurantId) {
        Restaurant restaurant = ValidationUtil.checkNotFound(restaurantRepository.getReferenceById(restaurantId), restaurantId);

        Voting voting = new Voting(userRepository.getReferenceById(SecurityUtil.authUserId()), restaurant);
        Voting createdVoting = votingRepository.save(voting);
        return VotingUtil.getTo(createdVoting);
    }

    @Transactional
    public void update(Integer restaurantId) {
        Restaurant restaurant = ValidationUtil.checkNotFound(restaurantRepository.getReferenceById(restaurantId), restaurantId);

        int userId = SecurityUtil.authUserId();
        Voting voting = ValidationUtil.checkNotFound(votingRepository.findByUserIdAndCurrentDate(userId).orElse(null), userId);

        LocalTime now = LocalTime.now();
        if (now.isAfter(LocalTime.of(11, 0))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "vote cannot be changed after 11 a.m.");
        }
        voting.setRestaurant(restaurant);
        votingRepository.save(voting);
    }

    @Transactional(readOnly = true)
    public List<VotingTo> getVoting() {
        return VotingUtil.getTos(votingRepository.findByVotingDate(LocalDate.now()));
    }

    @Transactional(readOnly = true)
    public List<VotingTo> getAll() {
        return VotingUtil.getTos(votingRepository.findAll());
    }
}

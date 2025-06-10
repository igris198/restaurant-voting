package ru.javaops.bootjava.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.model.Voting;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.repository.VotingRepository;
import ru.javaops.bootjava.security.SecurityUtil;
import ru.javaops.bootjava.to.VotingTo;
import ru.javaops.bootjava.util.ValidationUtil;
import ru.javaops.bootjava.util.VotingUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class VotingService {
    private static final LocalTime VOTING_TIME = LocalTime.of(11, 0);

    private final VotingRepository votingRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public VotingService(VotingRepository votingRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
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
    public void update(int id, int restaurantId) {
        Restaurant restaurant = ValidationUtil.checkNotFound(restaurantRepository.getReferenceById(restaurantId), restaurantId);
        Voting voting = ValidationUtil.checkNotFound(votingRepository.findByVotingDateAndUser_Id(LocalDate.now(), SecurityUtil.authUserId()).orElse(null), id);

        LocalTime now = LocalTime.now();
        if (now.isAfter(VOTING_TIME)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "vote cannot be changed after 11 a.m.");
        }
        voting.setRestaurant(restaurant);
        votingRepository.save(voting);
    }

    public List<VotingTo> getVoting() {
        Optional<Voting> voting = votingRepository.findByVotingDateAndUser_Id(LocalDate.now(), SecurityUtil.authUserId());
        return voting.isPresent() ? VotingUtil.getTos(List.of(voting.get())) : VotingUtil.getTos(Collections.emptyList()) ;
    }

    public List<VotingTo> getAll() {
        return VotingUtil.getTos(votingRepository.findAllByUser_Id(SecurityUtil.authUserId()));
    }
}

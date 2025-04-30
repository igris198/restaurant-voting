package ru.javaops.bootjava.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.model.Voting;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.repository.VotingRepository;
import ru.javaops.bootjava.to.VotingTo;
import ru.javaops.bootjava.security.SecurityUtil;
import ru.javaops.bootjava.util.VotingUtil;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(VotingController.REST_URL)
public class VotingController {
    static final String REST_URL = "/api/voting";

    VotingRepository votingRepository;
    RestaurantRepository restaurantRepository;
    UserRepository userRepository;

    public VotingController(VotingRepository votingRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.votingRepository = votingRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{restaurantId}") // 22
    @Transactional
    public ResponseEntity<VotingTo> create(@PathVariable Integer restaurantId) {
        Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
        Assert.notNull(restaurant, "restaurant with id = " + restaurantId + " not found");

        int userId = SecurityUtil.authUserId();
        Voting voting = new Voting(userRepository.getReferenceById(userId), restaurant);

        Voting createdVoting = votingRepository.save(voting);
        VotingTo votingTo = VotingUtil.getTo(createdVoting);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{restaurantId}")
                .buildAndExpand(votingTo.Id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(votingTo);
    }

    @PutMapping("/{restaurantId}") // 27
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer restaurantId) {
        Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
        Assert.notNull(restaurant, "restaurant with id = " + restaurantId + " not found");

        int userId = SecurityUtil.authUserId();
        Voting voting = votingRepository.findByUserIdAndCurrentDate(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        if (LocalTime.now().isAfter(LocalTime.of(11, 0))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "vote cannot be changed");
        }
        voting.setRestaurant(restaurant);
        votingRepository.save(voting);
    }

    @GetMapping // 23
    public List<VotingTo> getVoting() {
        return VotingUtil.getTos(votingRepository.findByVotingDate(LocalDate.now()));
    }

    @GetMapping("/all") // 24
    public List<VotingTo> getAll() {
        return VotingUtil.getTos(votingRepository.findAll());
    }

}

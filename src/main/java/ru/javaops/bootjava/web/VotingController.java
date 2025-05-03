package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.model.Restaurant;
import ru.javaops.bootjava.model.Voting;
import ru.javaops.bootjava.repository.RestaurantRepository;
import ru.javaops.bootjava.repository.UserRepository;
import ru.javaops.bootjava.repository.VotingRepository;
import ru.javaops.bootjava.security.SecurityUtil;
import ru.javaops.bootjava.to.VotingTo;
import ru.javaops.bootjava.util.ValidationUtil;
import ru.javaops.bootjava.util.VotingUtil;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(VotingController.REST_URL)
@Tag(name = "UserController", description = "Voting operations")
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

    @Operation(summary = "Vote for the restaurant by its id")
    @PostMapping("/{restaurantId}") // 22
    @Transactional
    public ResponseEntity<VotingTo> create(@PathVariable Integer restaurantId) {
        Restaurant restaurant = ValidationUtil.checkNotFound(restaurantRepository.getReferenceById(restaurantId), restaurantId);

        int userId = SecurityUtil.authUserId();
        Voting voting = new Voting(userRepository.getReferenceById(userId), restaurant);

        Voting createdVoting = votingRepository.save(voting);
        VotingTo votingTo = VotingUtil.getTo(createdVoting);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{restaurantId}")
                .buildAndExpand(votingTo.Id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(votingTo);
    }

    @Operation(summary = "Change vote")
    @PutMapping("/{restaurantId}") // 27
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer restaurantId) {
        Restaurant restaurant = ValidationUtil.checkNotFound(restaurantRepository.getReferenceById(restaurantId), restaurantId);

        int userId = SecurityUtil.authUserId();
        Voting voting = ValidationUtil.checkNotFound(votingRepository.findByUserIdAndCurrentDate(userId).orElse(null), userId);

        if (LocalTime.now().isAfter(LocalTime.of(11, 0))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "vote cannot be changed");
        }
        voting.setRestaurant(restaurant);
        votingRepository.save(voting);
    }

    @Operation(summary = "Get the voting list for today")
    @GetMapping // 23
    public List<VotingTo> getVoting() {
        return VotingUtil.getTos(votingRepository.findByVotingDate(LocalDate.now()));
    }

    @Operation(summary = "List of votes")
    @GetMapping("/all") // 24
    public List<VotingTo> getAll() {
        return VotingUtil.getTos(votingRepository.findAll());
    }

}

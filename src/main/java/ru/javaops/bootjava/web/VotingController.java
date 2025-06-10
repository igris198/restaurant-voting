package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.vote.AbstractAclVoter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.service.VotingService;
import ru.javaops.bootjava.to.VoteTo;
import ru.javaops.bootjava.to.VotingTo;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(VotingController.REST_URL)
@Tag(name = "VotingController", description = "Voting operations")
public class VotingController {
    public static final String REST_URL = "/api/votes";

    private final VotingService votingService;

    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    @Operation(summary = "Vote for the restaurant by its id")
    @PostMapping // 22
    public ResponseEntity<VotingTo> create(@RequestParam int restaurantId) {
        VotingTo votingTo = votingService.create(restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{votingId}")
                .buildAndExpand(votingTo.Id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(votingTo);
    }

    @Operation(summary = "Change vote")
    @PutMapping("/{voteId}") // 27
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int voteId, @RequestParam int newRestaurantId) {
        votingService.update(voteId, newRestaurantId);
    }

    @Operation(summary = "Get a vote today")
    @GetMapping("/today") // 23
    public List<VotingTo> getVoting() {
        return votingService.getVoting();
    }

    @Operation(summary = "Get user voting for all days")
    @GetMapping // 24
    public List<VotingTo> getAll() {
        return votingService.getAll();
    }

}

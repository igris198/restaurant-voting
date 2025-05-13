package ru.javaops.bootjava.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.bootjava.service.VotingService;
import ru.javaops.bootjava.to.VotingTo;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(VotingController.REST_URL)
@Tag(name = "UserController", description = "Voting operations")
public class VotingController {
    public static final String REST_URL = "/api/votes";

    private final VotingService votingService;

    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    @Operation(summary = "Vote for the restaurant by its id")
    @PostMapping("/{restaurantId}") // 22
    public ResponseEntity<VotingTo> create(@PathVariable Integer restaurantId) {
        VotingTo votingTo = votingService.create(restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{restaurantId}")
                .buildAndExpand(votingTo.Id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(votingTo);
    }

    @Operation(summary = "Change vote")
    @PutMapping("/{restaurantId}") // 27
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer restaurantId) {
        votingService.update(restaurantId);
    }

    @Operation(summary = "Get the voting list for today")
    @GetMapping // 23
    public List<VotingTo> getVoting() {
        return votingService.getVoting();
    }

    @Operation(summary = "List of votes")
    @GetMapping("/all") // 24
    public List<VotingTo> getAll() {
        return votingService.getAll();
    }

}

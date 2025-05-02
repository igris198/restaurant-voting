package ru.javaops.bootjava.util;

import ru.javaops.bootjava.model.Voting;
import ru.javaops.bootjava.to.VotingTo;

import java.util.List;

public class VotingUtil {
    public static VotingTo getTo(Voting voting) {
        return new VotingTo(voting.id(), voting.getUser().id(), voting.getRestaurant().id(), voting.getVotingDate());
    }

    public static List<VotingTo> getTos(List<Voting> votings) {
        return votings.stream().map(VotingUtil::getTo).toList();
    }
}

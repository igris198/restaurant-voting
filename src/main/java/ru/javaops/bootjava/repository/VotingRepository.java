package ru.javaops.bootjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.model.Voting;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VotingRepository extends JpaRepository<Voting, Integer> {

    @Query("SELECT v FROM Voting v WHERE v.user.id = :userId AND v.votingDate = CURRENT_DATE")
    Optional<Voting> findByUserIdAndCurrentDate(@Param("userId") Integer userId);

    List<Voting> findByVotingDate(LocalDate votingDate);
}

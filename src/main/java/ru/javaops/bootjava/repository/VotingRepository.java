package ru.javaops.bootjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.model.Voting;
import ru.javaops.bootjava.security.SecurityUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VotingRepository extends JpaRepository<Voting, Integer> {

    Optional<Voting> findByVotingDateAndUser_Id(LocalDate votingDate, int userId);

    List<Voting> findAllByUser_Id(int userId);
}

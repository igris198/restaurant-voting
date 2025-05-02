package ru.javaops.bootjava.repository;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.enabled=:isEnabled WHERE u.id=:id")
    int enable(@Param("id") int id, @Param("isEnabled") boolean isEnabled);

    @QueryHints({
            @QueryHint(name = "hql.DistinctTokenPassingMode", value = "NEVER")
    })
    User getByEmail(String email);
}

package com.magabyzr.ecommercemg.repositories;

import com.magabyzr.ecommercemg.dtos.UserSummary;
import com.magabyzr.ecommercemg.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    @EntityGraph(attributePaths = {"tags", "addresses"})
    Optional<User> findByEmail(String email);

    //avoid the N+1 problem
    @EntityGraph
    @Query("select u from User u")
    List<User> findAllWithTags();

    @Query("select u.id as id, u.email as email from User u where u.profile.loyalty_points > :loyalty_points order by u.email")
    List<UserSummary> findLoyalUsers(@Param("loyalty_points") int loyalty_points);
}

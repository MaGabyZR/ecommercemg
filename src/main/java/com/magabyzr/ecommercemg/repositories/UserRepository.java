package com.magabyzr.ecommercemg.repositories;


import com.magabyzr.ecommercemg.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Change it from Crud to Jpa to use Dto
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}

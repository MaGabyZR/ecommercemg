package com.magabyzr.ecommercemg.repositories;


import com.magabyzr.ecommercemg.entities.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {

}

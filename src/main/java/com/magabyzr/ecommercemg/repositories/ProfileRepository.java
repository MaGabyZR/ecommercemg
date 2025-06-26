package com.magabyzr.ecommercemg.repositories;

import com.magabyzr.ecommercemg.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}
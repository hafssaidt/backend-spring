package org.idtaleb.skyline.repositories;

import org.idtaleb.skyline.entities.UserApp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserApp, String> {
    Optional<UserApp> findByEmail(String email);
}

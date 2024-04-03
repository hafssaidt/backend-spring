package org.idtaleb.skylin.repositories;

import org.idtaleb.skylin.entities.UserApp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserApp, String> {
    Optional<UserApp> findByEmail(String email);
}

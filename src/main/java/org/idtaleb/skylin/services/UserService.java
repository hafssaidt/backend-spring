package org.idtaleb.skylin.services;

import org.idtaleb.skylin.entities.UserApp;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserApp createUser(UserApp user);

    UserApp getUserById(String userId);

    UserApp getUser(String email);

    UserApp updateUser(String userId, UserApp user);

    void deleteUser(String userId);
}

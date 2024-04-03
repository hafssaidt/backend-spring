package org.idtaleb.skylin.servicesImpl;

import org.idtaleb.skylin.entities.UserApp;
import org.idtaleb.skylin.repositories.UserRepository;
import org.idtaleb.skylin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserApp createUser(UserApp user) {
        Optional<UserApp> userChecked = userRepository.findByEmail(user.getEmail());
        if (userChecked.isPresent())
            throw new RuntimeException("user is already exists!");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreationDate(LocalDate.now());
        UserApp userSaved = userRepository.save(user);
        return userSaved;
    }

    @Override
    public UserApp getUserById(String userId) {
        Optional<UserApp> userChecked = userRepository.findById(userId);
        if (userChecked.isEmpty())
            throw new RuntimeException("user not found!");
        return userChecked.get();
    }

    @Override
    public UserApp getUser(String email) {
        Optional<UserApp> userChecked = userRepository.findByEmail(email);
        if (userChecked.isEmpty())
            throw new UsernameNotFoundException(email);
        return userChecked.get();
    }

    @Override
    public UserApp updateUser(String userId, UserApp user) {
        UserApp userChecked = getUserById(userId);
        if (user.getFirstName() != null && !user.getFirstName().isEmpty())
            userChecked.setFirstName(user.getFirstName());
        if (user.getLastName() != null && !user.getLastName().isEmpty())
            userChecked.setLastName(user.getLastName());
        if (user.getPassword() != null && !user.getPassword().isEmpty())
            userChecked.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        UserApp userUpdated = userRepository.save(userChecked);
        return userUpdated;
    }

    @Override
    public void deleteUser(String userId) {
        UserApp userChecked = getUserById(userId);
        userRepository.delete(userChecked);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserApp> user = userRepository.findByEmail(email);
        if (user.isEmpty())
            throw new UsernameNotFoundException(email);
        return new User(user.get().getEmail(), user.get().getPassword(), new ArrayList<>());
    }

}

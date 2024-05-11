package ru.kata.spring.boot_security.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    void saveUser(User user);

    void deleteUser(Long id);
}

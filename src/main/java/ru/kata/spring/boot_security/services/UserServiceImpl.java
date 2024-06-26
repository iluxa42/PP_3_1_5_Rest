package ru.kata.spring.boot_security.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.repositories.UserRepository;
import ru.kata.spring.boot_security.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;

   @Autowired
   public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
   }

   @Override
   public User findByUsername(String username) {
      return userRepository.findByUsername(username);
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = findByUsername(username);

      if (user == null) {
         throw new UsernameNotFoundException(String.format("User '%s' not found", username));
      }

      return new org.springframework.security.core.userdetails.User(
              user.getUsername(), user.getPassword(), user.getAuthorities());
   }

   @Override
   public List<User> getAllUsers() {
      return userRepository.findAll();
   }

   @Override
   public Optional<User> getUserById(Long id) {
      return userRepository.findById(id);
   }

   @Override
   @Transactional
   public void saveUser(User user) {
      Optional<User> existingUser = userRepository.findById(user.getId());

      if (existingUser.isEmpty()
              || (!existingUser.get().getPassword().equals(user.getPassword()))) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
      }

      userRepository.save(user);
   }

   @Override
   @Transactional
   public void deleteUser(Long id) {
      userRepository.deleteById(id);
   }
}
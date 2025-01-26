package com.example.demo.service;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.example.demo.repository.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        Optional <User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            throw new IllegalStateException("User already exists");
        }
        user.setAge(Period.between(user.getBirth(), LocalDate.now()).getYears());
        return userRepository.save(user);
    }

    public void delete(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        userRepository.deleteById(id);
    }

    public void update(Long id, String email, String name) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        User user = optionalUser.get();
        if (email != null && !email.equals(user.getEmail())) {
            Optional <User> foundByEmail = userRepository.findByEmail(email);
            if (foundByEmail.isPresent()) {
                throw new IllegalStateException("User already exists");
            }
            user.setEmail(email);
        }
        if (name != null) {
            user.setName(name);
        }
        userRepository.save(user);
    }

    //    @Bean
//    public List<User> helloWorld() {
//        return List.of(
//                new User(1l, "Sergey", "ser@gmail.com", LocalDate.of(2000, 11, 12), 24),
//                new User(2l, "Mary", "mar@gmail.com", LocalDate.of(1990, 11, 12), 34),
//                new User(3l, "Ivan", "ivan@gmail.com", LocalDate.of(1998, 2, 12), 26));
//    }
}

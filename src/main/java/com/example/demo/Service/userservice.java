package com.example.demo.Service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.Model.User;
import com.example.demo.Repositroy.UserRepository;

@Service
public class userservice {

    @Autowired
    private UserRepository userRepository;

  public ResponseEntity<?> saveUser(User user) {

    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Email already exists");
    }   
    else {
        User savedUser = userRepository.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }
}
    
   public Optional<User> loginUser(User user) {
    Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

    if (existingUser.isPresent() &&
        existingUser.get().getPassword().equals(user.getPassword())) {

        return existingUser; // Login successful
    }

    return Optional.empty(); // Login failed
}
}
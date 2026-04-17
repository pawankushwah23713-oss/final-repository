package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Model.User;
import com.example.demo.Service.userservice;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import java.util.List;
import com.example.demo.Model.tempuser;
import com.example.demo.Repositroy.UserRepository;

@CrossOrigin(origins = "https://finalrepositoryfrontend.netlify.app")

@RestController
@RequestMapping("/api/users")
 // frontend allow
public class usercontroller {

    @Autowired
    private userservice userService; 

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
  
    @PostMapping("/login")
    public Optional<User> loginUser(@RequestBody User user) {
        return userService.loginUser(user);
    }

    @GetMapping
public List<User> getUsers(@RequestParam String email) {
    return userRepository.findAll()
            .stream()
            .filter(u -> !u.getEmail().equals(email))
            .toList();
}

    
}

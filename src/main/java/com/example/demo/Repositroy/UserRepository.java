package com.example.demo.Repositroy;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.Model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
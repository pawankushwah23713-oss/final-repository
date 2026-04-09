package com.example.demo.Repositroy;



import com.example.demo.Model.Product;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByemail(String email);
}
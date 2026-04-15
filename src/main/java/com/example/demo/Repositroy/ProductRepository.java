package com.example.demo.Repositroy;



import com.example.demo.Model.Product;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByemail(String email);

    @Query("{ $or: [ " +
       "{ 'name': { $regex: ?0, $options: 'i' } }, " +
       "{ 'category': { $regex: ?0, $options: 'i' } }, " +
       "{ 'description': { $regex: ?0, $options: 'i' } } " +
       "] }")
List<Product> searchProducts(String keyword);
}
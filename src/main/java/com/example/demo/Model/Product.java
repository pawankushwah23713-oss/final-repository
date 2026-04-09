package com.example.demo.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    private String id;

    private String email;
    private String username;
    private String password;

    private String name;
    private String description;
    private double price;
    private String sku;
    private String category;
    private String imageUrl;

    // ❤️ LIKE SYSTEM
    private List<String> likes; // store user emails who liked

    // 💬 COMMENT SYSTEM
    private List<Comment> comments;

    public Product(String email, String username, String password, String name, String description, double price, String sku, String category, String imageUrl) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
        this.category = category;
        this.imageUrl = imageUrl;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getEmail() {
        return email;   
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description; 
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;   }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public List<String> getLikes() {
        return likes;
    }
    public void setLikes(List<String> likes) {
        this.likes = likes;
    }
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

public List<String> getLikedBy() {
    return likes;
}
public void setLikedBy(List<String> likedBy) {
    this.likes = likedBy;
}
    



}
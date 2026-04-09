package com.example.demo.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;


@Document(collection = "users")
@AllArgsConstructor
@Data
@ Getter
@ Setter
public class User {

    @Id
    private String id;
    private String name;
     @Indexed(unique = true)
    private String email;
    private String password;
    private String provider;

    public User() {}

    public User(String name, String email, String password, String provider) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.provider = provider;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;  
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getProvider() {
        return provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }

}
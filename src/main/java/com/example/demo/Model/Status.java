package com.example.demo.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "statuses")
public class Status {

    @Id
    private String id;

    private String userId;
    private String username;

    private String contentUrl; // image/video URL
    private String type; // IMAGE / VIDEO / TEXT

    private String text; // for text status

    private LocalDateTime createdAt;

    @Indexed(expireAfterSeconds = 0)
    private LocalDateTime expiresAt;

    private List<String> seenBy = new ArrayList<>();

    // 🔹 Default Constructor
    public Status() {
    }

    // 🔹 Parameterized Constructor
    public Status(String userId, String username, String contentUrl, String type, String text) {
        this.userId = userId;
        this.username = username;
        this.contentUrl = contentUrl;
        this.type = type;
        this.text = text;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusHours(24);
        this.seenBy = new ArrayList<>();
    }

    // 🔹 Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public List<String> getSeenBy() {
        return seenBy;
    }

    public void setSeenBy(List<String> seenBy) {
        this.seenBy = seenBy;
    }
}
package com.example.demo.Model;

import lombok.*;

@Data
@NoArgsConstructor
@Builder
public class Comment {

    private String email;
    private String message;
        public Comment(String email, String message) {
            this.email = email;
            this.message = message;
        }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
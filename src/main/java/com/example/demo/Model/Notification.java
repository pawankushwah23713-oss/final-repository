package com.example.demo.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;
    private String type; // PRODUCT_ADD, PRODUCT_LIKE, PRODUCT_COMMENT
    private String sender;
    private String productName;
    private String message;
    private String productId;
    private String receiver; // who should receive this notification
    private boolean delivered = false; // default false

    public Notification() {}

    public Notification(String type, String sender, String productName, String message, String productId) {
        this.type = type;
        this.sender = sender;
        this.productName = productName;
        this.message = message;
        this.productId = productId;
    }

    // --- Getters and Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }

    public boolean isDelivered() { return delivered; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }
}
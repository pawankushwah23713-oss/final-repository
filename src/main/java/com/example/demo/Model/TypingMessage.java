package com.example.demo.Model; // 👈 apna package adjust kar lena

public class TypingMessage {

    private String sender;
    private String receiver;
    private boolean typing;

    // ✅ getters & setters

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isTyping() {
        return typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }
}
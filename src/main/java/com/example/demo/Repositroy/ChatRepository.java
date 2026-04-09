package com.example.demo.Repositroy;



import com.example.demo.Model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByReceiverAndDeliveredFalse(String receiver);
    List<ChatMessage> findByReceiverAndDelivered(String receiver, boolean delivered);
}  
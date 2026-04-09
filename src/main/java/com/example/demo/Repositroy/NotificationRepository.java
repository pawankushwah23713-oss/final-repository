package com.example.demo.Repositroy;

import com.example.demo.Model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByReceiverAndDeliveredFalse(String receiver);
}
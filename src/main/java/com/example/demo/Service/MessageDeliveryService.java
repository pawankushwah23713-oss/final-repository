package com.example.demo.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;  
import com.example.demo.Model.ChatMessage;
import com.example.demo.Repositroy.ChatRepository;

import org.springframework.beans.factory.annotation.Autowired;





@Service
public class MessageDeliveryService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRepository chatRepository;

    public MessageDeliveryService(SimpMessagingTemplate messagingTemplate,
                                  ChatRepository chatRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatRepository = chatRepository;
    }

    public void sendPendingMessages(String username) {

        List<ChatMessage> messages =
                chatRepository.findByReceiverAndDeliveredFalse(username);

        for (ChatMessage msg : messages) {

            messagingTemplate.convertAndSendToUser(
                    username,
                    "/queue/messages",
                    msg
            );

            msg.setDelivered(true);
        }

        chatRepository.saveAll(messages);
    }



    
}
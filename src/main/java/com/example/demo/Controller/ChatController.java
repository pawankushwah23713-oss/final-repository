package com.example.demo.Controller;

import com.example.demo.Model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import com.example.demo.Repositroy.ChatRepository;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Model.TypingMessage;
import com.example.demo.websocket.OnlineUserStore;



import java.util.List;

@CrossOrigin("*") // React app URL
@RestController
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRepository chatRepository;

    public ChatController(SimpMessagingTemplate messagingTemplate,
            ChatRepository chatRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatRepository = chatRepository;
    }
@MessageMapping("/chat")
public void sendMessage(ChatMessage message) {

    String sender = OnlineUserStore.normalize(message.getSender());
    String receiver = OnlineUserStore.normalize(message.getReceiver());

    System.out.println("📨 " + sender + " → " + receiver);

    boolean isReceiverOnline = OnlineUserStore.isOnline(receiver);

    if (isReceiverOnline) {
        messagingTemplate.convertAndSendToUser(
                receiver,
                "/queue/messages",
                message
        );
         
        System.out.println("🔥 Delivered");
    } else {
        System.out.println("💾 Stored (offline)");
    }
 


    message.setDelivered(isReceiverOnline);
    chatRepository.save(message);
}

    // 🔥 NEW API (PENDING FETCH + DELETE)
    @GetMapping("/api/pending")
    @ResponseBody
    public List<ChatMessage> getPendingMessages(@RequestParam String username) {

        System.out.println("📥 Fetch pending for: " + username);

        List<ChatMessage> pending =
                chatRepository.findByReceiverAndDeliveredFalse(username);

        System.out.println("📦 Found: " + pending.size());

        // ✅ mark delivered
        for (ChatMessage msg : pending) {
            msg.setDelivered(true);
        }

        // 💾 update DB
        chatRepository.saveAll(pending);

        // 🗑️ delete after showing (tera requirement)
        chatRepository.deleteAll(pending);

        return pending;
    }
    @MessageMapping("/typing")
public void typing(TypingMessage msg) {
    messagingTemplate.convertAndSendToUser(
        msg.getReceiver(),
        "/queue/typing",
        msg
    );
}
}
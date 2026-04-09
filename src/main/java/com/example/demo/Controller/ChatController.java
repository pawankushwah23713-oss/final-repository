package com.example.demo.Controller;

import com.example.demo.Model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import com.example.demo.Repositroy.ChatRepository;
import com.example.demo.websocket.OnlineUsers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Model.TypingMessage;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // React app URL
@RestController
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRepository chatRepository;

    public ChatController(SimpMessagingTemplate messagingTemplate,
            ChatRepository chatRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatRepository = chatRepository;
    }

    // ✅ EXISTING CODE (UNCHANGED)
  @MessageMapping("/chat")
public void sendMessage(ChatMessage message) {

    System.out.println("📨 " + message.getSender() + " → " + message.getReceiver());

    try {
        // 🔥 TRY LIVE SEND
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/queue/messages",
                message
        );

        // ✅ assume delivered
        message.setDelivered(true);
        System.out.println("🔥 Sent live");

    } catch (Exception e) {

        // ❌ FAIL → store only
        message.setDelivered(false);
        System.out.println("💾 User offline, storing in DB");
    }

    // 💾 ALWAYS SAVE (important)
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
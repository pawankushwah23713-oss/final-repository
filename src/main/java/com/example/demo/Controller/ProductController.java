package com.example.demo.Controller;

import com.example.demo.Model.Product;
import com.cloudinary.Cloudinary;
import com.example.demo.Model.Comment;
import com.example.demo.Model.Notification;
import com.example.demo.Model.tempuser;
import com.example.demo.Repositroy.ProductRepository;
import com.example.demo.Repositroy.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.cloudinary.utils.ObjectUtils;

@CrossOrigin(origins = "https://finalrepositoryfrontend.netlify.app")

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    // --- Add Product ---
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        Product saved = productRepository.save(product);

        // Always store notification in DB
        Notification notif = new Notification(
                "PRODUCT_ADD",
                product.getEmail(),
                saved.getName(),
                null,
                saved.getId()
        );
        notificationRepository.save(notif);

        // Send to all online users
        sendToOnlineUsers(notif);

        return saved;
    }

    // --- Toggle Like ---
    @PutMapping("/like/{id}")
    public Product toggleLike(@PathVariable String id, @RequestParam String email) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (product.getLikedBy() == null) product.setLikedBy(new ArrayList<>());

        boolean liked = product.getLikedBy().contains(email);
        if (liked) product.getLikedBy().remove(email);
        else product.getLikedBy().add(email);

        Product saved = productRepository.save(product);

        // Notify product owner (if not self)
        if (!email.equals(product.getEmail())) {
            Notification notif = new Notification(
                    "PRODUCT_LIKE",
                    email,
                    product.getName(),
                    null,
                    product.getId()
            );
            notif.setReceiver(product.getEmail());

            // Always save to DB
            notificationRepository.save(notif);

            // Try sending live
            sendToUserIfOnline(product.getEmail(), notif);
        }

        return saved;
    }

    // --- Delete a comment with WebSocket notification ---
@DeleteMapping("/comment/{productId}/{index}")
public Product deleteComment(
        @PathVariable String productId,
        @PathVariable int index,
        @RequestParam String email) {

    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

    if (product.getComments() == null || index < 0 || index >= product.getComments().size()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid comment index");
    }

    Comment comment = product.getComments().get(index);

    // Only comment owner can delete
    if (!comment.getEmail().equals(email)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete others' comments");
    }

    // Remove comment
    product.getComments().remove(index);
    Product saved = productRepository.save(product);

    // ✅ Notify product owner via WebSocket (if online)
    if (!email.equals(product.getEmail())) {
        Notification notif = new Notification(
                "COMMENT_DELETE",
                email,
                product.getName(),
                comment.getMessage(),
                product.getId()
        );
        notif.setReceiver(product.getEmail());

        // Save to DB
        notificationRepository.save(notif);

        // Send live if online
        sendToUserIfOnline(product.getEmail(), notif);
    }

    // ✅ Optionally, notify all online users (like product page update)
    sendToOnlineUsers(new Notification(
            "COMMENT_DELETE_GLOBAL",
            email,
            product.getName(),
            comment.getMessage(),
            product.getId()
    ));

    return saved;
}

    // --- Add Comment ---
    @PostMapping("/comment/{id}")
    public Product addComment(@PathVariable String id, @RequestBody Comment comment) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (product.getComments() == null) product.setComments(new ArrayList<>());
        product.getComments().add(comment);
        Product saved = productRepository.save(product);

        if (!comment.getEmail().equals(product.getEmail())) {
            Notification notif = new Notification(
                    "PRODUCT_COMMENT",
                    comment.getEmail(),
                    product.getName(),
                    comment.getMessage(),
                    product.getId()
            );
            notif.setReceiver(product.getEmail());

            // Always save to DB
            notificationRepository.save(notif);

            // Try sending live
            sendToUserIfOnline(product.getEmail(), notif);
        }

        return saved;
    }

    // --- Send to specific online user ---
    private void sendToUserIfOnline(String username, Notification notif) {
        try {
            messagingTemplate.convertAndSendToUser(username, "/queue/messages", notif);
        } catch (Exception ignored) {
        }
    }

    // --- Send to all online users ---
    private void sendToOnlineUsers(Notification notif) {
        try {
            messagingTemplate.convertAndSend("/topic/products", notif);
        } catch (Exception ignored) {
        }
    }

    // --- Fetch pending notifications for offline users ---
    @GetMapping("/pending")
    public List<Notification> getPendingNotifications(@RequestParam String username) {
        List<Notification> pending = notificationRepository.findByReceiverAndDeliveredFalse(username);
        pending.forEach(n -> n.setDelivered(true)); // mark delivered
        notificationRepository.saveAll(pending);
        return pending;
    }

    // --- Get all products for a user ---
    @PostMapping("/profile")
    public List<Product> getAllProducts(@RequestBody tempuser user) {
        return productRepository.findByemail(user.getEmail());
    }

    // --- Get all products ---
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


@Autowired
private Cloudinary cloudinary;

@DeleteMapping("/{id}")
public Product deleteProduct(@PathVariable String id, @RequestBody tempuser user) {
    String email = user.getEmail();

    Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

    if (!product.getEmail().equals(email)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this product");
    }

    // 🔥 Delete image from Cloudinary
    if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
        try {
            String imageUrl = product.getImageUrl();

            // Step 1: "/upload/" ke baad ka part lo
            String publicId = imageUrl.substring(imageUrl.indexOf("/upload/") + 8);

            // Step 2: version remove karo (v1234567890/)
            if (publicId.startsWith("v")) {
                publicId = publicId.substring(publicId.indexOf("/") + 1);
            }

            // Step 3: extension hatao (.jpg, .png)
            publicId = publicId.substring(0, publicId.lastIndexOf("."));

            System.out.println("Deleting Cloudinary publicId: " + publicId);

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            System.out.println("Cloudinary response: " + result);

        } catch (Exception e) {
            System.err.println("Cloudinary delete error: " + e.getMessage());
        }
    }

    // DB se delete
    productRepository.delete(product);

    // Notification
    Notification notif = new Notification(
            "PRODUCT_DELETE",
            email,
            product.getName(),
            null,
            product.getId()
    );
    notificationRepository.save(notif);
    sendToOnlineUsers(notif);

    return product;
}
// --- 🔍 Search Products ---
@GetMapping("/search")
public List<Product> searchProducts(@RequestParam String q) {

    System.out.println("🔍 Searching products: " + q);

    return productRepository.searchProducts(q);
}

}

package com.example.demo.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Model.Status;
import com.example.demo.Service.StatusService;
import java.util.List;
import com.example.demo.Repositroy.StatusRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@CrossOrigin(origins = "http://localhost:3000") // React app URL
@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private StatusService service;
    @Autowired
    StatusRepository statusRepository;


    @PostMapping("/add")
    public Status addStatus(@RequestBody Status status) {
        return service.createStatus(status);
    }

    @GetMapping("/all")
    public List<Status> getAllStatuses() {
        return service.getActiveStatuses();
    }

    @PostMapping("/seen/{id}")
    public void seen(@PathVariable String id, @RequestParam String userId) {
        service.markAsSeen(id, userId);
    }

@Autowired
private Cloudinary cloudinary;

@DeleteMapping("/{id}")
public void deleteStatus(
        @PathVariable String id,
        @RequestParam String username
) throws Exception {

    Status status = statusRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Status not found"));

    // check owner
    if (!status.getUsername().equals(username)) {
        throw new RuntimeException("Not allowed to delete");
    }

    // 🔥 Cloudinary delete (FIXED for video)
    if (status.getContentUrl() != null && !status.getContentUrl().isEmpty()) {
        try {
            String url = status.getContentUrl();

            String publicId = url.substring(url.indexOf("/upload/") + 8);

            if (publicId.startsWith("v")) {
                publicId = publicId.substring(publicId.indexOf("/") + 1);
            }

            publicId = publicId.substring(0, publicId.lastIndexOf("."));

            System.out.println("Deleting Cloudinary publicId: " + publicId);

            // ✅ ONLY CHANGE HERE
            cloudinary.uploader().destroy(
                publicId,
                ObjectUtils.asMap("resource_type", "video")
            );

        } catch (Exception e) {
            System.out.println("Cloudinary delete error: " + e.getMessage());
        }
    }

    // Mongo delete
    statusRepository.deleteById(id);
}
}
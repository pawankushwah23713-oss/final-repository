package com.example.demo.Controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.example.demo.Model.Music;
import com.example.demo.Repositroy.MusicRepository;

@RestController
@RequestMapping("/music")
@CrossOrigin(origins = "https://finalrepositoryfrontend.netlify.app")

public class MusicController {

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private Cloudinary cloudinary;

    // 🎵 1. Upload Music
    @PostMapping("/upload")
    public Music uploadMusic(@RequestParam("file") MultipartFile file,
                             @RequestParam("title") String title,
                             @RequestParam("artist") String artist,
                             @RequestParam("username") String username) {
        try {

            // Upload to Cloudinary (IMPORTANT for audio)
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", "video")
            );

            String url = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            Music music = new Music();
            music.setTitle(title);
            music.setArtist(artist);
            music.setUsername(username);
            music.setUrl(url);
            music.setPublicId(publicId);
            music.setLikes(0);
           

            return musicRepository.save(music);

        } catch (Exception e) {
            throw new RuntimeException("Upload failed ❌ " + e.getMessage());
        }
    }

    // 📥 2. Get All Music
    @GetMapping("/all")
    public List<Music> getAllMusic() {
        return musicRepository.findAll();
    }

    // 👤 3. Get Music by Username
    @GetMapping("/user/{username}")
    public List<Music> getUserMusic(@PathVariable String username) {
        return musicRepository.findByUsername(username);
    }

    // 🔍 4. Search Music (title)
    @GetMapping("/search")
    public List<Music> searchMusic(@RequestParam String query) {
        return musicRepository.findByTitleContainingIgnoreCase(query);
    }

    // ❤️ 5. Like Music
    @PutMapping("/like/{id}")
    public Music likeMusic(@PathVariable String id) {
        Music music = musicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));

        music.setLikes(music.getLikes() + 1);

        return musicRepository.save(music);
    }

  @DeleteMapping("/delete/{id}")
public ResponseEntity<?> deleteMusic(
        @PathVariable String id,
        @RequestParam String username) {

    Music music = musicRepository.findById(id).orElse(null);

    if (music == null) {
        return ResponseEntity.badRequest().body("Music not found");
    }

    // 🔥 Security check
    if (!music.getUsername().equals(username)) {
        return ResponseEntity.status(403).body("Unauthorized");
    }

    // 🔥 Cloudinary se delete (CORRECT WAY)
    if (music.getPublicId() != null && !music.getPublicId().isEmpty()) {
        try {
            System.out.println("Deleting Cloudinary publicId: " + music.getPublicId());

            Map result = cloudinary.uploader().destroy(
                    music.getPublicId(),
                    ObjectUtils.asMap("resource_type", "video") // 🎵 audio ke liye important
            );

            System.out.println("Cloudinary response: " + result);

        } catch (Exception e) {
            System.err.println("Cloudinary delete error: " + e.getMessage());
        }
    }

    // 🔥 MongoDB se delete
    musicRepository.delete(music);

    return ResponseEntity.ok("Deleted successfully");
}

    // 🎯 7. Get Single Music
    @GetMapping("/{id}")
    public Music getSingleMusic(@PathVariable String id) {
        return musicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));
    }
}

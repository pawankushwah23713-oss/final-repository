package com.example.demo.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/file")
@CrossOrigin("*")
public class FileController {

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {

        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // ☁️ Upload to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",   // image + video dono
                            "folder", "status-app"     // optional folder
                    )
            );

            // ✅ Direct Cloudinary URL return
            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
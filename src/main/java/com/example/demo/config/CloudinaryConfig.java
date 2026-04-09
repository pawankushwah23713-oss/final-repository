package com.example.demo.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dgjpkuhof");
        config.put("api_key", "614712415662633");
        config.put("api_secret", "GVqPVOMvM1rFFf1vsJoCOAQ8EMI");

        return new Cloudinary(config);
    }
}
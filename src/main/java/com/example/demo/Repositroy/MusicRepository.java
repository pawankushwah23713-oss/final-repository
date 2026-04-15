package com.example.demo.Repositroy;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.Model.Music;

public interface MusicRepository extends MongoRepository<Music, String> {

    List<Music> findByUsername(String username);

    List<Music> findByTitleContainingIgnoreCase(String title);
}
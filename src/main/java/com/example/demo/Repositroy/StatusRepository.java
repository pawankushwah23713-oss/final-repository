package com.example.demo.Repositroy;
import com.example.demo.Model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface StatusRepository extends MongoRepository<Status, String> {

    List<Status> findByExpiresAtAfter(LocalDateTime now);

    List<Status> findByUserIdAndExpiresAtAfter(String userId, LocalDateTime now);
}
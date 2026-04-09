package com.example.demo.Service;
import com.example.demo.Model.Status;
import com.example.demo.Repositroy.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Service
public class StatusService {

    @Autowired
    private StatusRepository repo;

    public Status createStatus(Status status) {
        status.setCreatedAt(LocalDateTime.now());
        status.setExpiresAt(LocalDateTime.now().plusHours(24));
        status.setSeenBy(new ArrayList<>());

        return repo.save(status);
    }

    public List<Status> getActiveStatuses() {
        return repo.findByExpiresAtAfter(LocalDateTime.now());
    }

    public void markAsSeen(String statusId, String userId) {
        Status status = repo.findById(statusId).orElseThrow();

        if (!status.getSeenBy().contains(userId)) {
            status.getSeenBy().add(userId);
            repo.save(status);
        }
    }
}
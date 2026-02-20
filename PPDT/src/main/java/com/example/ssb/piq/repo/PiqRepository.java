package com.example.ssb.piq.repo;

import com.example.ssb.piq.entity.PiqForm;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PiqRepository extends MongoRepository<PiqForm, String> {
    Optional<PiqForm> findByUserId(String userId);
}

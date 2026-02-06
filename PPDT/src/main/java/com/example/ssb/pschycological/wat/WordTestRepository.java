package com.example.ssb.pschycological.wat;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WordTestRepository extends MongoRepository<WordTest, String> {
    // No need custom methods, MongoRepository already provides findById, deleteById, etc.
}

package com.example.ssb.gto.lecturette.repo;

import com.example.ssb.gto.lecturette.Entity.Lecturette;
import com.example.ssb.gto.lecturette.dto.basic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LecturetteRepository extends MongoRepository<Lecturette, String> {
    List<Lecturette> findByTitleContainingIgnoreCase(String keyword);
    List<Lecturette> findByCategoryContainingIgnoreCase(String category);
    List<basic> findAllBy();


}

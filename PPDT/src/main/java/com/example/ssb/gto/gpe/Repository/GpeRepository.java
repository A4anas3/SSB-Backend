package com.example.ssb.gto.gpe.Repository;


import com.example.ssb.gto.gpe.Entity.Gpe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GpeRepository extends MongoRepository<Gpe, String> {
    List<Gpe> findBySampleTrue();


}


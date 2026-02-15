package com.example.ssb.ppdt.Repo;



import com.example.ssb.ppdt.Entity.PPDTImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PPDTImageRepository extends JpaRepository<PPDTImage, Long> {



    List<PPDTImage> findAllByIsSampleFalse();


}


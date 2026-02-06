package com.example.ssb.gto.gpe.service;



import com.example.ssb.gto.gpe.Entity.Gpe;
import com.example.ssb.gto.gpe.Repository.GpeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GpeService {


    private final GpeRepository gpeRepository;


    public List<Gpe> getSampleGpe() {
        return gpeRepository.findBySampleTrue();
    }

    // ✅ Get All
    public List<Gpe> getAllGpe() {
        return gpeRepository.findAll();
    }

    // ✅ Get By ID
    public Gpe getById(String id) {
        return gpeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GPE not found with id: " + id));
    }

    // ✅ Create
    public Gpe create(Gpe gpe) {
        return gpeRepository.save(gpe);
    }



    // ✅ Patch (Partial Update)
    public Gpe patch(String id, Gpe gpe) {
        Gpe existing = getById(id);


        existing.setSample(gpe.isSample());
        if (gpe.getImageUrl() != null) existing.setImageUrl(gpe.getImageUrl());
        if (gpe.getQuestion() != null) existing.setQuestion(gpe.getQuestion());
        if(gpe.getOverview()!=null) existing.setOverview(gpe.getOverview());

        if (gpe.getPlans() != null) existing.setPlans(gpe.getPlans());

        return gpeRepository.save(existing);
    }

    // ✅ Delete
    public void delete(String id) {
        gpeRepository.deleteById(id);
    }

    // ✅ Toggle isSample
    public Gpe toggleSample(String id) {
        Gpe gpe = getById(id);
        gpe.setSample(!gpe.isSample());
        return gpeRepository.save(gpe);
    }
}


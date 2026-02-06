package com.example.ssb.gto.lecturette.service;

import com.example.ssb.gto.lecturette.Entity.Lecturette;
import com.example.ssb.gto.lecturette.dto.basic;
import com.example.ssb.gto.lecturette.repo.LecturetteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturetteService {

    private final LecturetteRepository lecturetteRepository;

    public List<Lecturette> getAllLecturettes() {
        return lecturetteRepository.findAll();
    }

    public Lecturette getLecturetteById(String id) {
        return lecturetteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecturette not found with id: " + id));
    }

    public void createLecturette(Lecturette lecturette) {
        lecturetteRepository.save(lecturette);
    }

    public Lecturette updateLecturette(String id, Lecturette lecturette) {
        Lecturette existing = lecturetteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecturette not found with id: " + id));

        existing.setTitle(lecturette.getTitle());
        existing.setIntroduction(lecturette.getIntroduction());
        existing.setSubHeadings(lecturette.getSubHeadings());
        existing.setConclusion(lecturette.getConclusion());
        existing.setCategory(lecturette.getCategory()); // ✅ ADD THIS

        return lecturetteRepository.save(existing);
    }


    public void deleteLecturette(String id) {
        if (!lecturetteRepository.existsById(id)) {
            throw new RuntimeException("Lecturette not found with id: " + id);
        }
        lecturetteRepository.deleteById(id);
    }

    public Lecturette patchLecturette(String id, Lecturette lecturette) {
        Lecturette existing = lecturetteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecturette not found with id: " + id));


        if (lecturette.getTitle() != null) {
            existing.setTitle(lecturette.getTitle());
        }
        if (lecturette.getIntroduction() != null) {
            existing.setIntroduction(lecturette.getIntroduction());
        }
        if (lecturette.getSubHeadings() != null) {
            existing.setSubHeadings(lecturette.getSubHeadings());
        }
        if (lecturette.getConclusion() != null) {
            existing.setConclusion(lecturette.getConclusion());
        }
        if (lecturette.getCategory() != null) {
            existing.setCategory(lecturette.getCategory());
        }


        return lecturetteRepository.save(existing);
    }
    public List<Lecturette> searchByTitle(String keyword) {
        return lecturetteRepository.findByTitleContainingIgnoreCase(keyword);
    }
    public List<Lecturette> searchByCategory(String category) {
        return lecturetteRepository.findByCategoryContainingIgnoreCase(category);
    }

    public List<basic> getBasicLecturettes() {
        return lecturetteRepository.findAll()
                .stream()
                .map(l -> new basic(
                        l.getId(),
                        l.getTitle(),
                        l.getUrl()
                ))
                .toList();
    }





}

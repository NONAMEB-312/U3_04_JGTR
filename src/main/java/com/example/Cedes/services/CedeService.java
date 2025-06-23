package com.example.Cedes.services;

import com.example.Cedes.models.Cede;
import com.example.Cedes.repositories.CedeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CedeService {
    private final CedeRepository cedeRepository;

    public CedeService(CedeRepository cedeRepository) {
        this.cedeRepository = cedeRepository;
    }

    public List<Cede> findAll() {
        return cedeRepository.findAll();
    }

    public Cede findById(Integer id) {
        return cedeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cede no encontrada"));
    }

    public Cede save(Cede cede) {
        return cedeRepository.save(cede);
    }

    public Cede update(Integer id, Cede cedeDetails) {
        Cede cede = findById(id);
        cede.setEstado(cedeDetails.getEstado());
        cede.setMunicipio(cedeDetails.getMunicipio());
        return cedeRepository.save(cede);
    }

    public void delete(Integer id) {
        cedeRepository.deleteById(id);
    }
}

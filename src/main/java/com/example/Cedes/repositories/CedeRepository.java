package com.example.Cedes.repositories;

import com.example.Cedes.models.Cede;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CedeRepository extends JpaRepository<Cede, Integer> {
    boolean existsByClave(String clave);
}
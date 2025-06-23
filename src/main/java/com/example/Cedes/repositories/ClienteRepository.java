package com.example.Cedes.repositories;

import com.example.Cedes.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    boolean existsByEmail(String email);
}
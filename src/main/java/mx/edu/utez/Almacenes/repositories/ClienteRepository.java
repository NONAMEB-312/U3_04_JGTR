package mx.edu.utez.Almacenes.repositories;

import mx.edu.utez.Almacenes.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    boolean existsByEmail(String email);
}
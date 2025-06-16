package utez.edu.mx.almacenes.repository;

import utez.edu.mx.almacenes.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente save(Cliente cliente);
    List<Cliente> findAll();
    Optional<Cliente> findById(Long id);
    void deleteById(Long id);
}

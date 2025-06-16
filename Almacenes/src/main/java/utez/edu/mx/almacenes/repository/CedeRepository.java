package utez.edu.mx.almacenes.repository;

import utez.edu.mx.almacenes.model.Cede;

import java.util.List;
import java.util.Optional;

public interface CedeRepository {
    Cede save (Cede save);
    List<Cede> findAll();
    Optional<Cede> findById(Long id);
    void deletedBy(Long id);
}

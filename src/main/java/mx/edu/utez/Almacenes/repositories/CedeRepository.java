package mx.edu.utez.Almacenes.repositories;

import mx.edu.utez.Almacenes.models.Cede;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CedeRepository extends JpaRepository<Cede, Integer> {
    boolean existsByClave(String clave);
}
package mx.edu.utez.Almacenes.repositories;

import mx.edu.utez.Almacenes.models.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
    boolean existsByClave(String clave);
}

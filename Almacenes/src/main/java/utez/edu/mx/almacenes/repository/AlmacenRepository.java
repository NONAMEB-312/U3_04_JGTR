package utez.edu.mx.almacenes.repository;

import utez.edu.mx.almacenes.model.Almacen;

import java.util.List;
import java.util.Optional;

public interface AlmacenRepository {
    Almacen save(Almacen almacen);
    List<Almacen> findAll();
    Optional<Almacen> findById(Long id);
    void deleteById(Long id);
    List<Almacen> findByCedeId(Long cedeId);
}

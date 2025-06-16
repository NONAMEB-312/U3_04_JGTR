package utez.edu.mx.almacenes.repository;

import org.springframework.stereotype.Repository;
import utez.edu.mx.almacenes.model.Almacen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AlmacenRepositoryImpl implements AlmacenRepository{
    private final List<Almacen> almacenes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Almacen save(Almacen almacen) {
        if (almacen.getId() == null) {
            almacen.setId(idGenerator.getAndIncrement());
            almacenes.add(almacen);
        } else {
            almacenes.removeIf(a -> a.getId().equals(almacen.getId()));
            almacenes.add(almacen);
        }
        return almacen;
    }

    @Override
    public List<Almacen> findAll() {
        return new ArrayList<>(almacenes);
    }

    @Override
    public Optional<Almacen> findById(Long id) {
        return almacenes.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    @Override
    public void deleteById(Long id) {
        almacenes.removeIf(a -> a.getId().equals(id));
    }

    @Override
    public List<Almacen> findByCedeId(Long cedeId) {
        return almacenes.stream()
                .filter(a -> a.getCedeId() != null && a.getCedeId().equals(cedeId))
                .toList();
    }
}

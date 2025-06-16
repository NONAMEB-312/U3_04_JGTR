package utez.edu.mx.almacenes.repository;

import org.springframework.stereotype.Repository;
import utez.edu.mx.almacenes.model.Cede;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CedeRepositoryImpl implements CedeRepository {
    private final List<Cede> cedes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Cede save(Cede cede) {
        if (cede.getId() == null) {
            cede.setId(idGenerator.getAndIncrement());
            cedes.add(cede);
        } else {
            cedes.removeIf(c -> c.getId().equals(cede.getId()));
            cedes.add(cede);
        }
        return cede;
    }

    @Override
    public List<Cede> findAll() {
        return new ArrayList<>(cedes);
    }

    @Override
    public Optional<Cede> findById(Long id) {
        return cedes.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public void deletedById(Long id) {
        cedes.removeIf(c -> c.getId().equals(id));
    }

}

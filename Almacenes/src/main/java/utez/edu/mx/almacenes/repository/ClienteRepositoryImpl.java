package utez.edu.mx.almacenes.repository;

import org.springframework.stereotype.Repository;
import utez.edu.mx.almacenes.model.Cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ClienteRepositoryImpl implements ClienteRepository{
    private final List<Cliente> clientes = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Cliente save(Cliente cliente) {
        if (cliente.getId() == null) {
            cliente.setId(idGenerator.getAndIncrement());
            clientes.add(cliente);
        } else {
            clientes.removeIf(c -> c.getId().equals(cliente.getId()));
            clientes.add(cliente);
        }
        return cliente;
    }

    @Override
    public List<Cliente> findAll() {
        return new ArrayList<>(clientes);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return clientes.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public void deleteById(Long id) {
        clientes.removeIf(c -> c.getId().equals(id));
    }

}

package utez.edu.mx.almacenes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.almacenes.model.Almacen;
import utez.edu.mx.almacenes.repository.AlmacenRepository;
import utez.edu.mx.almacenes.repository.CedeRepository;

import java.util.List;

@Service
public class AlmacenService {

    @Autowired
    private AlmacenRepository almacenRepository;
    @Autowired
    private CedeRepository cedeRepository;
    @Autowired
    private ClienteService clienteService;

    public AlmacenService(AlmacenRepository almacenRepository,
                          CedeRepository cedeRepository,
                          ClienteService clienteService) {
        this.almacenRepository = almacenRepository;
        this.cedeRepository = cedeRepository;
        this.clienteService = clienteService;
    }

    public Almacen crearAlmacen(Almacen almacen) {
        cedeRepository.findById(almacen.getCedeId())
                .orElseThrow(() -> new RuntimeException("Cede no encontrada"));

        String claveCede = cedeRepository.findById(almacen.getCedeId()).get().getClave();
        almacen.setClave(claveCede + "-A" + almacen.getId());

        return almacenRepository.save(almacen);
    }

    public List<Almacen> listarAlmacenes() {
        return almacenRepository.findAll();
    }

    public Almacen buscarAlmacenPorId(Long id) {
        return almacenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
    }

    public String alquilarAlmacen(Long almacenId, Long clienteId) {
        Almacen almacen = buscarAlmacenPorId(almacenId);
        clienteService.buscarClientePorId(clienteId); // Valida que el cliente exista
        almacen.setClienteId(clienteId);
        almacenRepository.save(almacen);
        return "Almacén " + almacenId + " alquilado al cliente " + clienteId;
    }

    public void eliminarAlmacen(Long id) {
        almacenRepository.deleteById(id);
    }

    public List<Almacen> listarAlmacenesPorCede(Long cedeId) {
        return almacenRepository.findByCedeId(cedeId);
    }
}

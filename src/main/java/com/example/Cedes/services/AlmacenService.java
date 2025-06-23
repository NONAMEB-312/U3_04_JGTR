package com.example.Cedes.services;

import com.example.Cedes.models.Almacen;
import com.example.Cedes.models.Cede;
import com.example.Cedes.models.Cliente;
import com.example.Cedes.repositories.AlmacenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AlmacenService {
    private final AlmacenRepository almacenRepository;
    private final CedeService cedeService;
    private final ClienteService clienteService;

    public AlmacenService(AlmacenRepository almacenRepository, CedeService cedeService, ClienteService clienteService) {
        this.almacenRepository = almacenRepository;
        this.cedeService = cedeService;
        this.clienteService = clienteService;
    }

    public List<Almacen> findAll() {
        return almacenRepository.findAll();
    }

    public Almacen findById(Integer id) {
        return almacenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
    }

    @Transactional
    public Almacen save(Almacen almacen) {
        Cede cede = cedeService.findById(almacen.getCede().getId());
        almacen.setCede(cede);

        if (almacen.getFechaRegistro() == null) {
            almacen.setFechaRegistro(LocalDate.now());
        }

        Almacen savedAlmacen = almacenRepository.save(almacen);

        if (savedAlmacen.getClave() == null || savedAlmacen.getClave().startsWith("TEMP-")) {
            String nuevaClave = String.format("%s-A%d", cede.getClave(), savedAlmacen.getId());
            savedAlmacen.setClave(nuevaClave);
            return almacenRepository.save(savedAlmacen);
        }

        return savedAlmacen;
    }

    @Transactional
    public Almacen update(Integer id, Almacen almacenDetails) {
        Almacen almacen = findById(id);

        // Actualizar solo los campos permitidos
        almacen.setPrecioVenta(almacenDetails.getPrecioVenta());
        almacen.setPrecioRenta(almacenDetails.getPrecioRenta());
        almacen.setTamanio(almacenDetails.getTamanio());

        // Si cambia la cede, actualizar la clave
        if (almacenDetails.getCede() != null &&
                !almacenDetails.getCede().getId().equals(almacen.getCede().getId())) {
            Cede nuevaCede = cedeService.findById(almacenDetails.getCede().getId());
            almacen.setCede(nuevaCede);
            String nuevaClave = String.format("%s-A%d", nuevaCede.getClave(), almacen.getId());
            almacen.setClave(nuevaClave);
        }

        return almacenRepository.save(almacen);
    }

    @Transactional
    public Almacen rentarAlmacen(Integer almacenId, Integer clienteId) {
        Almacen almacen = findById(almacenId);
        Cliente cliente = clienteService.findById(clienteId);

        if (almacen.getStatus() != Almacen.Status.DISPONIBLE) {
            throw new IllegalStateException("El almacén no está disponible");
        }

        almacen.setStatus(Almacen.Status.RENTADO);
        almacen.setCliente(cliente);
        return almacenRepository.save(almacen);
    }

    @Transactional
    public Almacen venderAlmacen(Integer almacenId, Integer clienteId) {
        Almacen almacen = findById(almacenId);
        Cliente cliente = clienteService.findById(clienteId);

        if (almacen.getStatus() != Almacen.Status.DISPONIBLE) {
            throw new IllegalStateException("El almacén no está disponible para venta");
        }

        almacen.setStatus(Almacen.Status.VENDIDO);
        almacen.setCliente(cliente);
        return almacenRepository.save(almacen);
    }

    @Transactional
    public Almacen liberarAlmacen(Integer almacenId) {
        Almacen almacen = findById(almacenId);
        almacen.setStatus(Almacen.Status.DISPONIBLE);
        almacen.setCliente(null);
        return almacenRepository.save(almacen);
    }

    public void delete(Integer id) {
        almacenRepository.deleteById(id);
    }
}
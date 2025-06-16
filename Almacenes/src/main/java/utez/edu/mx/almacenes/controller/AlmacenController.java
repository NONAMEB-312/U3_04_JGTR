package utez.edu.mx.almacenes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.almacenes.model.Almacen;
import utez.edu.mx.almacenes.service.AlmacenService;

import java.util.List;

@RestController
@RequestMapping("/api/almacenes")
public class AlmacenController {

    @Autowired
    private AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    // Crear almacén (relacionado con cedeId)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Almacen crearAlmacen(@RequestBody Almacen almacen) {
        return almacenService.crearAlmacen(almacen);
    }

    // Listar todos los almacenes
    @GetMapping
    public List<Almacen> listarAlmacenes() {
        return almacenService.listarAlmacenes();
    }

    // Buscar almacén por ID
    @GetMapping("/{id}")
    public Almacen buscarAlmacenPorId(@PathVariable Long id) {
        return almacenService.buscarAlmacenPorId(id);
    }

    // Alquilar almacén a cliente (relación por IDs)
    @PutMapping("/{id}/alquilar")
    public String alquilarAlmacen(@PathVariable Long id, @RequestBody Long clienteId) {
        return almacenService.alquilarAlmacen(id, clienteId);
    }

    // Eliminar almacén
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarAlmacen(@PathVariable Long id) {
        almacenService.eliminarAlmacen(id);
    }
}

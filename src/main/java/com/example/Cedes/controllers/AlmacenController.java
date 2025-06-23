package com.example.Cedes.controllers;

import com.example.Cedes.models.Almacen;
import com.example.Cedes.services.AlmacenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/almacenes")
public class AlmacenController {
    private final AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    @GetMapping
    public List<Almacen> getAllAlmacenes() {
        return almacenService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Almacen> getAlmacenById(@PathVariable Integer id) {
        return ResponseEntity.ok(almacenService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Almacen> createAlmacen(@RequestBody Almacen almacen) {
        return ResponseEntity.ok(almacenService.save(almacen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Almacen> updateAlmacen(@PathVariable Integer id, @RequestBody Almacen almacenDetails) {
        return ResponseEntity.ok(almacenService.update(id, almacenDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlmacen(@PathVariable Integer id) {
        almacenService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{almacenId}/rentar")
    public ResponseEntity<Almacen> rentarAlmacen(
            @PathVariable Integer almacenId,
            @RequestBody RentarAlmacenRequest request) {
        Almacen almacen = almacenService.rentarAlmacen(almacenId, request.getClienteId());
        return ResponseEntity.ok(almacen);
    }

    @PostMapping("/{almacenId}/vender")
    public ResponseEntity<Almacen> venderAlmacen(
            @PathVariable Integer almacenId,
            @RequestBody VenderAlmacenRequest request) {
        Almacen almacen = almacenService.venderAlmacen(almacenId, request.getClienteId());
        return ResponseEntity.ok(almacen);
    }

    @PostMapping("/{almacenId}/liberar")
    public ResponseEntity<Almacen> liberarAlmacen(@PathVariable Integer almacenId) {
        Almacen almacen = almacenService.liberarAlmacen(almacenId);
        return ResponseEntity.ok(almacen);
    }

}

// DTOs para las solicitudes
class RentarAlmacenRequest {
    private Integer clienteId;

    // getters y setters
    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
}

class VenderAlmacenRequest {
    private Integer clienteId;

    // getters y setters
    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
}

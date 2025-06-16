package utez.edu.mx.almacenes.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.almacenes.model.Cede;
import utez.edu.mx.almacenes.service.CedeService;

import java.util.List;

@RestController
@RequestMapping("/api/cedes")
@RequiredArgsConstructor
public class CedeController {

    @Autowired
    private CedeService   cedeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cede crearCede(@RequestBody Cede cede) {
        return cedeService.crearCede(cede);
    }

    @GetMapping
    public List<Cede> listarCedes() {
        return cedeService.listarCedes();
    }

    @GetMapping("/{id}")
    public Cede buscarPorId(@PathVariable Long id) {
        return cedeService.buscarPorId(id).orElseThrow();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCede(@PathVariable Long id) {
        cedeService.eliminarCede(id);
    }

}

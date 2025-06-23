package com.example.Cedes.controllers;

import com.example.Cedes.models.Cede;
import com.example.Cedes.services.CedeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cedes")
public class CedeController {
    private final CedeService cedeService;

    public CedeController(CedeService cedeService) {
        this.cedeService = cedeService;
    }

    @GetMapping
    public List<Cede> getAllCedes() {
        return cedeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cede> getCedeById(@PathVariable Integer id) {
        return ResponseEntity.ok(cedeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Cede> createCede(@RequestBody Cede cede) {
        return ResponseEntity.ok(cedeService.save(cede));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cede> updateCede(@PathVariable Integer id, @RequestBody Cede cedeDetails) {
        return ResponseEntity.ok(cedeService.update(id, cedeDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCede(@PathVariable Integer id) {
        cedeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
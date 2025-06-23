package com.example.Cedes.repositories;

import com.example.Cedes.models.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
    boolean existsByClave(String clave);
}

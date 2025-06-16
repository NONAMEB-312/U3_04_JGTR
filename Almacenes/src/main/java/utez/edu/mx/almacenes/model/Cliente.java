package utez.edu.mx.almacenes.model;

import lombok.Data;

@Data
public class Cliente {
    private Long id;
    private String nombreCompleto;
    private String telefono;
    private String email;

}

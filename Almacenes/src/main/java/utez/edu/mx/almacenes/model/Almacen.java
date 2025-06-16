package utez.edu.mx.almacenes.model;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Almacen {
    private Long id;
    private String clave;
    private LocalDate fechaRegistro;
    private BigDecimal precioVenta;
    private BigDecimal precioRenta;
    private String tamanio;
    private Long cedeId;

}

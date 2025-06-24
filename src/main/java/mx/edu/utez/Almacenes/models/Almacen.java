package mx.edu.utez.Almacenes.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "almacenes")
public class Almacen {

    public Almacen(Integer id, String clave, LocalDate fechaRegistro, Double precioVenta,
                   Double precioRenta, Tamanio tamanio, Status status, Cede cede, Cliente cliente) {
        this.id = id;
        this.clave = clave;
        this.fechaRegistro = fechaRegistro;
        this.precioVenta = precioVenta;
        this.precioRenta = precioRenta;
        this.tamanio = tamanio;
        this.status = status;
        this.cede = cede;
        this.cliente = cliente;
    }

    public Almacen() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String clave;

    @Column(nullable = false)
    private LocalDate fechaRegistro;

    @Column(nullable = false)
    private Double precioVenta;

    @Column(nullable = false)
    private Double precioRenta;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tamanio tamanio;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "cede_id", nullable = false)
    private Cede cede;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Double getPrecioRenta() {
        return precioRenta;
    }

    public void setPrecioRenta(Double precioRenta) {
        this.precioRenta = precioRenta;
    }

    public Tamanio getTamanio() {
        return tamanio;
    }

    public void setTamanio(Tamanio tamanio) {
        this.tamanio = tamanio;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Cede getCede() {
        return cede;
    }

    public void setCede(Cede cede) {
        this.cede = cede;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            this.fechaRegistro = LocalDate.now();
        }
        if (status == null) {
            this.status = Status.DISPONIBLE;
        }
        // Generar una clave temporal que se actualizará después
        this.clave = "TEMP-" + UUID.randomUUID().toString().substring(0, 4);
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    public void generarClave() {
        if (cede != null && cede.getClave() != null && id != null) {
            // Formato: [clave cede]-A[id]
            this.clave = String.format("%s-A%d", cede.getClave(), id);
        }
    }

    public enum Tamanio {
        G, M, P
    }

    public enum Status {
        DISPONIBLE,
        RENTADO,
        VENDIDO
    }
}
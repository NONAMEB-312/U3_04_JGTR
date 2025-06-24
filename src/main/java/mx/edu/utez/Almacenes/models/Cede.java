package mx.edu.utez.Almacenes.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Data
@Entity
@Table(name = "cedes")
public class Cede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String clave;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String municipio;

    public Cede() {
    }

    public Cede(Integer id, String clave, String estado, String municipio) {
        this.id = id;
        this.clave = clave;
        this.estado = estado;
        this.municipio = municipio;
    }

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    @PrePersist
    public void prePersist() {
        // Generar parte inicial de la clave (sin ID)
        LocalDate today = LocalDate.now();
        Random random = new Random();
        int randomNum = random.nextInt(9000) + 1000;
        this.clave = String.format("CTMP-%s-%d",
                today.format(DateTimeFormatter.ofPattern("ddMMyyyy")),
                randomNum);
    }

    @PostPersist
    public void postPersist() {
        // Reemplazar CTMP con el ID real después de la persistencia
        this.clave = this.clave.replace("CTMP", "C" + this.id);
    }
}
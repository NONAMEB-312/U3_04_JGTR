package utez.edu.mx.almacenes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.almacenes.model.Cede;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CedeService {
    @Autowired
    private DataSource dataSource;

    @Autowired
    public CedeService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Cede crearCede(Cede cede) {
        String sqlInsert = "INSERT INTO cede (clave, estado, municipio) VALUES (?, ?, ?)";
        String sqlUpdate = "UPDATE cede SET clave = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {

            // Insertamos primero con clave temporal
            stmtInsert.setString(1, "TEMP"); // Clave temporal
            stmtInsert.setString(2, cede.getEstado());
            stmtInsert.setString(3, cede.getMunicipio());
            stmtInsert.executeUpdate();

            // Obtenemos el ID generado
            long id;
            try (ResultSet rs = stmtInsert.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getLong(1);
                } else {
                    throw new RuntimeException("No se pudo obtener el ID de la cede");
                }
            }

            // Generamos la clave definitiva
            String clave = "C" + id + "-"
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "-"
                    + String.format("%04d", (int)(Math.random() * 10000));

            // Actualizamos con la clave real
            stmtUpdate.setString(1, clave);
            stmtUpdate.setLong(2, id);
            stmtUpdate.executeUpdate();

            cede.setId(id);
            cede.setClave(clave);
            return cede;

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear cede: " + e.getMessage(), e);
        }
    }

    public List<Cede> listarCedes() {
        List<Cede> cedes = new ArrayList<>();
        String sql = "SELECT * FROM cede";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cede cede = new Cede();
                cede.setId(rs.getLong("id"));
                cede.setClave(rs.getString("clave"));
                cede.setEstado(rs.getString("estado"));
                cede.setMunicipio(rs.getString("municipio"));
                cedes.add(cede);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar cedes: " + e.getMessage(), e);
        }
        return cedes;
    }

    public Optional<Cede> buscarPorId(Long id) {
        String sql = "SELECT * FROM cede WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cede cede = new Cede();
                    cede.setId(rs.getLong("id"));
                    cede.setClave(rs.getString("clave"));
                    cede.setEstado(rs.getString("estado"));
                    cede.setMunicipio(rs.getString("municipio"));
                    return Optional.of(cede);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cede: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void eliminarCede(Long id) {
        String sql = "DELETE FROM cede WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("No se encontró la cede con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cede: " + e.getMessage(), e);
        }
    }
}
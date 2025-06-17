package utez.edu.mx.almacenes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utez.edu.mx.almacenes.model.Almacen;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlmacenService {
    private final DataSource dataSource;
    private final CedeService cedeService;
    private final ClienteService clienteService;

    @Autowired
    public AlmacenService(DataSource dataSource, CedeService cedeService, ClienteService clienteService) {
        this.dataSource = dataSource;
        this.cedeService = cedeService;
        this.clienteService = clienteService;
    }

    public Almacen crearAlmacen(Almacen almacen) {
        // Validar que la cede exista
        cedeService.buscarPorId(almacen.getCedeId())
                .orElseThrow(() -> new RuntimeException("Cede no encontrada"));

        String sql = "INSERT INTO almacen (clave, fecha_registro, precio_venta, precio_renta, tamanio, cede_id, cliente_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Generar clave: [claveCede]-A[id] (el ID se obtendrá después)
            String claveCede = cedeService.buscarPorId(almacen.getCedeId()).get().getClave();
            String claveTemporal = claveCede + "-A0"; // Temporal hasta obtener el ID

            stmt.setString(1, claveTemporal);
            stmt.setDate(2, Date.valueOf(almacen.getFechaRegistro()));
            stmt.setBigDecimal(3, almacen.getPrecioVenta());
            stmt.setBigDecimal(4, almacen.getPrecioRenta());
            stmt.setString(5, almacen.getTamanio());
            stmt.setLong(6, almacen.getCedeId());
            stmt.setLong(7, almacen.getClienteId());
            stmt.executeUpdate();

            // Obtener ID generado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    // Actualizar con la clave real
                    String claveReal = claveCede + "-A" + id;
                    actualizarClaveAlmacen(id, claveReal);

                    almacen.setId(id);
                    almacen.setClave(claveReal);
                    return almacen;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear almacén: " + e.getMessage(), e);
        }
        throw new RuntimeException("No se pudo crear el almacén");
    }

    private void actualizarClaveAlmacen(Long id, String clave) {
        String sql = "UPDATE almacen SET clave = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, clave);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar clave de almacén: " + e.getMessage(), e);
        }
    }

    public List<Almacen> listarAlmacenes() {
        List<Almacen> almacenes = new ArrayList<>();
        String sql = "SELECT * FROM almacen";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                almacenes.add(mapearAlmacen(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar almacenes: " + e.getMessage(), e);
        }
        return almacenes;
    }

    public Almacen buscarAlmacenPorId(Long id) {
        String sql = "SELECT * FROM almacen WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearAlmacen(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar almacén: " + e.getMessage(), e);
        }
        throw new RuntimeException("Almacén no encontrado");
    }

    public String alquilarAlmacen(Long almacenId, Long clienteId) {
        // Validar que el cliente exista
        clienteService.buscarClientePorId(clienteId);

        String sql = "UPDATE almacen SET cliente_id = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, clienteId);
            stmt.setLong(2, almacenId);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Almacén no encontrado");
            }
            return "Almacén " + almacenId + " alquilado al cliente " + clienteId;
        } catch (SQLException e) {
            throw new RuntimeException("Error al alquilar almacén: " + e.getMessage(), e);
        }
    }

    public void eliminarAlmacen(Long id) {
        String sql = "DELETE FROM almacen WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Almacén no encontrado");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar almacén: " + e.getMessage(), e);
        }
    }

    public List<Almacen> listarAlmacenesPorCede(Long cedeId) {
        List<Almacen> almacenes = new ArrayList<>();
        String sql = "SELECT * FROM almacen WHERE cede_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cedeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    almacenes.add(mapearAlmacen(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar almacenes por cede: " + e.getMessage(), e);
        }
        return almacenes;
    }

    private Almacen mapearAlmacen(ResultSet rs) throws SQLException {
        Almacen almacen = new Almacen();
        almacen.setId(rs.getLong("id"));
        almacen.setClave(rs.getString("clave"));
        almacen.setFechaRegistro(rs.getDate("fecha_registro").toLocalDate());
        almacen.setPrecioVenta(rs.getBigDecimal("precio_venta"));
        almacen.setPrecioRenta(rs.getBigDecimal("precio_renta"));
        almacen.setTamanio(rs.getString("tamanio"));
        almacen.setCedeId(rs.getLong("cede_id"));
        almacen.setClienteId(rs.getLong("cliente_id"));
        return almacen;
    }
}
package com.mycompany.grupo9_transversal_sgulp.persistencia;

import com.mycompany.grupo9_transversal_sgulp.modelo.Materia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Grupo9
 */
public class MateriaData {

    private final miConexion conexion;

    public MateriaData(miConexion conexion) {
        this.conexion = conexion;
    }
        public boolean ningunaMateria() throws SQLException {
        String sql = "SELECT COUNT(*) FROM materia";
        try (Statement st = conexion.buscarConexion().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return true;
        }
    }
        
    public int insertarMateria(Materia materia) throws SQLException {
        String sql = "INSERT INTO materia (nombre, estado) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, materia.getNombre());
            ps.setBoolean(2, materia.isEstado());
            int filas;
            try {
                filas = ps.executeUpdate();
            } catch (SQLException ex) {
                throw new SQLException("No se pudo crear la materia", ex);
            }
            if (filas != 1) {
                throw new SQLException("No se pudo crear la materia");
            }

            int idMateria = 0;
            boolean hayId = false;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idMateria = rs.getInt(1);
                    hayId = !rs.wasNull() && idMateria != 0;
                }
            } catch (SQLException ex) {
                throw new SQLException("No se pudo obtener el id generado de la materia", ex);
            }
            if (!hayId) {
                throw new SQLException("No se pudo obtener el id generado de la materia");
            }
            materia.setIdMateria(idMateria);
            return idMateria;
        }
    }

    public void actualizarMateria(Materia materia) throws SQLException {
        String sql = "UPDATE materia SET nombre = ?,estado = ? WHERE idMateria = ?";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql)) {
            ps.setString(1, materia.getNombre());
            ps.setBoolean(2, materia.isEstado());
            ps.setInt(3, materia.getIdMateria());
            int filas;
            filas = ps.executeUpdate();
            if (filas != 1) {
                throw new SQLException("No se pudo actualizar la materia, el id= " + materia.getIdMateria() + " no existe");
            }
        }
    }

    public void borrarMateria(int idMateria, boolean cascade) throws SQLException {
        borrarMateria(idMateria, cascade, true);
    }

    public void borrarMateria(int idMateria, boolean cascade, boolean checkIfExists) throws SQLException {
        Connection conn = conexion.buscarConexion();
        boolean autoCommitPrevio = conn.getAutoCommit();
        try {
            conn.setAutoCommit(false);
            if (cascade) {
                String sqlCursadas = "DELETE FROM cursada WHERE idMateria = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlCursadas)) {
                    ps.setInt(1, idMateria);
                    ps.executeUpdate();
                }
            }
            String sql = "DELETE FROM materia WHERE idMateria = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idMateria);
                int filas = ps.executeUpdate();
                if (checkIfExists && filas != 1) {
                    throw new SQLException("No se pudo borrar la materia, el id= " + idMateria + " no existe");
                }
            }
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                ex.addSuppressed(rollbackEx);
            }
            if (miConexion.SQLSTATE_VIOLACION_INTEGRIDAD.equals(ex.getSQLState())) {
                throw new SQLException("No se pudo borrar la materia, el id= " + idMateria + " tiene cursadas asociadas", ex);
            }
            throw ex;
        } finally {
            conn.setAutoCommit(autoCommitPrevio);
        }
    }

    public void bajaLogicaMateria(int idMateria) throws SQLException {
        cambiarEstado(idMateria, false);
    }

    public void reactivacionLogicaMateria(int idMateria) throws SQLException {
        cambiarEstado(idMateria, true);
    }

    private void cambiarEstado(int idMateria, boolean estado) throws SQLException {
        Materia materia = buscarMateria(idMateria);
        String accion = estado ? "reactivar" : "dar de baja";
        if (materia == null) {
            throw new SQLException("No se pudo " + accion + " la materia, el id= " + idMateria + " no existe");
        }
        if (materia.isEstado() == estado) {
            /* La materia ya está en el estado pedido; no se modifica.
             String texto = estado ? "activa" : "inactiva";
             throw new SQLException("No se pudo " + accion + " la materia, el id= " + idMateria + " ya está " + texto);
            */
            return;
        }
        String sql = "UPDATE materia SET estado = ? WHERE idMateria = ?";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql)) {
            ps.setBoolean(1, estado);
            ps.setInt(2, idMateria);
            int filas = ps.executeUpdate();
            if (filas != 1) {
                throw new SQLException("No se pudo " + accion + " la materia, el id= " + idMateria + " no existe");
            }
        }
    }

    public Materia buscarMateria(int idMateria) throws SQLException {
        String sql = "SELECT idMateria, nombre, estado FROM materia WHERE idMateria = ?";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql)) {
            ps.setInt(1, idMateria);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Materia(rs.getInt("idMateria"), rs.getString("nombre"), rs.getBoolean("estado"));
                }
                return null;
            }
        }
    }

    public List<Materia> listarMaterias() throws SQLException {
        List<Materia> materias = new ArrayList<>();
        String sql = "SELECT idMateria, nombre, estado FROM materia";
        try (Statement st = conexion.buscarConexion().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                materias.add(new Materia(
                        rs.getInt("idMateria"),
                        rs.getString("nombre"),
                        rs.getBoolean("estado")
                ));
            }
        }
        return materias;
    }

   
}

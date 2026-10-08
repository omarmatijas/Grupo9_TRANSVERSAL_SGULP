package com.mycompany.grupo9_transversal_sgulp.persistencia;

import com.mycompany.grupo9_transversal_sgulp.modelo.Materia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Grupo9
 */
public class MateriaData {

    private final miConexion conexion;

    public MateriaData(miConexion conexion) {
        this.conexion = conexion;
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
}

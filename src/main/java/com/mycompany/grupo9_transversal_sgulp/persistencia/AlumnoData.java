/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.grupo9_transversal_sgulp.persistencia;

import com.mycompany.grupo9_transversal_sgulp.modelo.Alumno;
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
public class AlumnoData {

    private final miConexion conexion;

    public AlumnoData(miConexion conexion) {
        this.conexion = conexion;
    }

    public boolean ningunAlumno() throws SQLException {
        String sql = "SELECT COUNT(*) FROM alumno";
        try (Statement st = conexion.buscarConexion().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return true;
        }
    }

    public int insertarAlumno(Alumno alumno) throws SQLException {
        String sql = "INSERT INTO alumno (dni, nombre, fecNac, activo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, alumno.getDni());
            ps.setString(2, alumno.getNombre());
            ps.setDate(3, alumno.getFecNac());
            ps.setBoolean(4, alumno.isActivo());
            int filas;
            try {
                filas = ps.executeUpdate();
            } catch (SQLException ex) {
                throw new SQLException("No se pudo crear el alumno", ex);
            }
            if (filas != 1) {
                throw new SQLException("No se pudo crear el alumno");
            }

            int idAlumno = 0;
            boolean hayId = false;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idAlumno = rs.getInt(1);
                    hayId = !rs.wasNull() && idAlumno != 0;
                }
            } catch (SQLException ex) {
                throw new SQLException("No se pudo obtener el id generado del alumno", ex);
            }
            if (!hayId) {
                throw new SQLException("No se pudo obtener el id generado del alumno");
            }
            alumno.setIdAlumno(idAlumno);
            return idAlumno;
        }
    }

    public List<Alumno> listarAlumnos() throws SQLException {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT idAlumno, dni, nombre, fecNac, activo FROM alumno";
        try (Statement st = conexion.buscarConexion().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                alumnos.add(parsearRs(rs));
            }
        }
        return alumnos;
    }

    public Alumno buscarAlumnoPorDni(int dni) throws SQLException {
        String sql = "SELECT idAlumno, dni, nombre, fecNac, activo FROM alumno WHERE dni = ?";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql)) {
            ps.setInt(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return parsearRs(rs);
                }
                return null;
            }
        }
    }

    private static Alumno parsearRs(ResultSet rs) throws SQLException {
        return new Alumno(
                rs.getInt("idAlumno"),
                rs.getInt("dni"),
                rs.getString("nombre"),
                rs.getDate("fecNac"),
                rs.getBoolean("activo")
        );
    }

    public Alumno buscarAlumno(int idAlumno) throws SQLException {
        String sql = "SELECT idAlumno, dni, nombre, fecNac, activo FROM alumno WHERE idAlumno = ?";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql)) {
            ps.setInt(1, idAlumno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return parsearRs(rs);
                }
                return null;
            }
        }
    }
}

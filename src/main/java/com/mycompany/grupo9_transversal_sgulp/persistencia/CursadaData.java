/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.grupo9_transversal_sgulp.persistencia;

import com.mycompany.grupo9_transversal_sgulp.modelo.Alumno;
import com.mycompany.grupo9_transversal_sgulp.modelo.Cursada;
import com.mycompany.grupo9_transversal_sgulp.modelo.Materia;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author grupo9
 */
public class CursadaData {
    
    private final miConexion conexion;

    public CursadaData(miConexion conexion) {
        this.conexion = conexion;
    }

    public boolean ningunaCursada() throws SQLException {
        String sql = "SELECT COUNT(*) FROM cursada";
        try (Statement st = conexion.buscarConexion().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
            return true;
        }
    }

    public boolean existeCursada(Cursada cursada) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cursada WHERE idAlumno = ? AND idMateria = ? AND cursa = ?";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql)) {
            ps.setInt(1, cursada.getAlumno().getIdAlumno());
            ps.setInt(2, cursada.getMateria().getIdMateria());
            ps.setInt(3, cursada.getCursa());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
                return false;
            }
        }
    }

    public Cursada buscarCursada(Cursada cursada) throws SQLException {
        String sql = "SELECT * FROM cursada WHERE idAlumno = ? AND idMateria = ? AND cursa = ?";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql)) {
            ps.setInt(1, cursada.getAlumno().getIdAlumno());
            ps.setInt(2, cursada.getMateria().getIdMateria());
            ps.setInt(3, cursada.getCursa());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return parsearRS(rs);
                }
                return null;
            }
        }
    }
    
    public int inscribir(Cursada cursada) throws SQLException {
        String sql = "INSERT INTO cursada (idAlumno, idMateria, nota, asist, cursa) VALUES (?, ?, 0, 0, ?)";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cursada.getAlumno().getIdAlumno());
            ps.setInt(2, cursada.getMateria().getIdMateria());
            ps.setInt(3, cursada.getCursa());
            int filas;
            try {
                filas = ps.executeUpdate();
            } catch (SQLException ex) {
                throw new SQLException("No se pudo registrar la cursada", ex);
            }
            if (filas != 1) {
                throw new SQLException("No se pudo registrar la cursada");
            }

            int idCursada = 0;
            boolean hayId = false;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idCursada = rs.getInt(1);
                    hayId = !rs.wasNull() && idCursada != 0;
                }
            } catch (SQLException ex) {
                throw new SQLException("No se pudo obtener el id generado de la cursada", ex);
            }
            if (!hayId) {
                throw new SQLException("No se pudo obtener el id generado de la cursada");
            }
            cursada.setIdCursada(idCursada);
            return idCursada;
        }
    }
    
    public boolean anularInscripcion(Cursada cursada) throws SQLException {
        String sql = "DELETE FROM cursada WHERE idAlumno = ? AND idMateria = ? AND cursa = ?";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql)) {
            ps.setInt(1, cursada.getAlumno().getIdAlumno());
            ps.setInt(2, cursada.getMateria().getIdMateria());
            ps.setInt(3, cursada.getCursa());
            
            int filas;
            try {
                filas = ps.executeUpdate();
            } catch (SQLException ex) {
                throw new SQLException("No se pudo anular la cursada", ex);
            }
            if (filas != 1) {
                throw new SQLException("No se pudo anular la cursada");
            }
        }
        return true;
    }
    
    public List<Cursada> listarMateriasCursadas() throws SQLException {
        List<Cursada> cursadas = new ArrayList<>();
        String sql = "SELECT * FROM cursada";
        try (Statement st = conexion.buscarConexion().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cursadas.add(parsearRS(rs));
            }
        }
        return cursadas;
    }
    
    public List<Materia> listarMateriasNoCursadas(int idAlumno, int anio) throws SQLException {
        List<Materia> materias = new ArrayList<>();
        String sql = "SELECT * FROM materia WHERE estado = 1 AND idMateria NOT IN " + 
                "(SELECT idMateria FROM cursada WHERE idAlumno = ? AND cursa = ?) " +
                "ORDER BY nombre";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql)) {
            ps.setInt(1, idAlumno);
            ps.setInt(2, anio);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    materias.add(new Materia(
                            rs.getInt("idMateria"),
                            rs.getString("nombre"),
                            rs.getBoolean("estado")
                    ));
                }
            }
        }
        return materias;
    }
    
    public List<Alumno> listarAlumnosPorMateria(int idMateria) throws SQLException {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT * FROM alumno a JOIN cursada c ON c.idAlumno = a.idAlumno "
                + "AND c.idMateria = ? ORDER BY nombre";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql)) {
            ps.setInt(1, idMateria);
            
            try (ResultSet rs = ps.executeQuery(sql)) {
                while (rs.next()) {
                    alumnos.add(new Alumno(
                            rs.getInt("idAlumno"),
                            rs.getInt("dni"),
                            rs.getString("nombre"),
                            rs.getDate("fecNac"),
                            rs.getBoolean("activo")
                    ));
                }
            }
        }
        return alumnos;
    }
    
    public void actualizarEstadoAlumno(int idAlumno) throws SQLException {
        String sql = "UPDATE alumno SET estado = (SELECT COUNT(*) > 0 "
                + "FROM cursada WHERE idAlumno = ?)";
        try (PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql)) {
            ps.setInt(1, idAlumno);
        }
    }
    
    // actualizarNota
    public void actualizarNta(int idAlumno, int idMateria, int anio, double nota){
        String sql = "UPDATE cursada SET nota = ? WHERE idAlumno = ? AND idMateria = ? AND cursa = ?";
        
        try {
            PreparedStatement ps =conexion.buscarConexion().prepareStatement(sql);
            ps.setDouble(1, nota);
            ps.setInt(2, idAlumno);
            ps.setInt(3, idMateria);
            ps.setInt(4, anio);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CursadaData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // marcarAsistencia
    public void marcarAsistencia(int idAlumno, int idMateria, int anio) {
        String sql = "UPDATE cursada SET asist = asist + 1 "
                + "WHERE idAlumno = ? AND idMateria = ? AND cursa = ? AND asist < ?";

        try {
            PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql);
            ps.setInt(1, idAlumno);
            ps.setInt(2, idMateria);
            ps.setInt(3, anio);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                System.out.println("No se marcó asistencia (inscripción inexistente o ya tiene 30 clases)");
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CursadaData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // listarCursadasPorAlumno
    public List<Cursada> listarMateriasPorAlumno(int idAlumno) {
        List<Cursada> cursadas = new ArrayList<>();
        String sql = "SELECT c.idCursada, c.nota, c.asist, c.cursa, "
                + "m.idMateria, m.nombre AS nombreMateria, m.estado "
                + "FROM cursada c JOIN materia m ON c.idMateria = m.idMateria "
                + "WHERE c.idAlumno = ?";

        try {
            PreparedStatement ps = conexion.buscarConexion().prepareStatement(sql);
            ps.setInt(1, idAlumno);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Materia m = new Materia(rs.getInt("idMateria"),
                        rs.getString("nombreMateria"), rs.getBoolean("estado"));
                Alumno a = new Alumno();
                a.setIdAlumno(idAlumno);

                Cursada c = new Cursada(rs.getInt("idCursada"), a, m,
                        rs.getFloat("nota"), rs.getFloat("asist"), rs.getInt("cursa"));
                cursadas.add(c);
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CursadaData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cursadas;
    }
    
    
    public Cursada parsearRS(ResultSet rs) throws SQLException {
        AlumnoData alumnoData = new AlumnoData(conexion);
        MateriaData materiaData = new MateriaData(conexion);
        return new Cursada(
                rs.getInt("idCursada"),
                alumnoData.buscarAlumno(rs.getInt("idAlumno")),
                materiaData.buscarMateria(rs.getInt("idMateria")),
                rs.getFloat("nota"),
                rs.getFloat("asist"),
                rs.getInt("cursa")
        );
    }
}

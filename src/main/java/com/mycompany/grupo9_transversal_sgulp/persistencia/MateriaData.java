
package com.mycompany.grupo9_transversal_sgulp.persistencia;

import com.mycompany.grupo9_transversal_sgulp.modelo.Materia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MateriaData {
     private Connection con = null;

    public MateriaData(miConexion conexion) {
        this.con = conexion.buscarConexion();
    }

    public void guardarMateria(Materia m) {    // obj materia sin id válido
        String sql = "INSERT INTO materia(nombre, estado) VALUES (?,?)";  //1

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); //2
            ps.setString(1, m.getNombre());
            ps.setBoolean(2, m.isEstado());
            ps.executeUpdate();     // 3

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                m.setIdMateria(rs.getInt(1));
            } else {
                System.out.println("No se pudo obtener el ID");
            }
            ps.close();
            System.out.println("Materia guardada!");
        } catch (SQLException ex) {
            System.out.println("No pude insertar: " + ex.getMessage());
        }
    }   // INSERT INTO

    public Materia buscarMateria(int id) {
        Materia m = null;
        String sql = "SELECT * FROM materia WHERE idMateria = ?";  //1

        try {
            PreparedStatement ps = con.prepareStatement(sql);    // 2
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();  //3
            while (rs.next()) {  // 4
                m = new Materia();
                m.setIdMateria(rs.getInt("idMateria"));
                m.setNombre(rs.getString("nombre"));
                m.setEstado(rs.getBoolean("estado"));
            }
            ps.close(); // 5

        } catch (SQLException ex) {
            Logger.getLogger(MateriaData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return m;
    }  // SELECT 1 MATERIA

    public List<Materia> listarMaterias() {
        Materia m;
        List<Materia> materias = new ArrayList<>();
        String sql = "SELECT * FROM materia";  // 1

        try {
            PreparedStatement ps = con.prepareStatement(sql); // 2
            ResultSet rs = ps.executeQuery();  // 3
            while (rs.next()) {     // 4
                m = new Materia();
                m.setIdMateria(rs.getInt("idMateria"));
                m.setNombre(rs.getString("nombre"));
                m.setEstado(rs.getBoolean("estado"));
                materias.add(m);
            }
            ps.close();   // 5

        } catch (SQLException ex) {
            Logger.getLogger(MateriaData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return materias;
    } // SELECT *

    public List<Materia> listarMateriasActivas() {
        List<Materia> materias = new ArrayList<>();
        String sql = "SELECT * FROM materia WHERE estado = 1";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Materia m = new Materia();
                m.setIdMateria(rs.getInt("idMateria"));
                m.setNombre(rs.getString("nombre"));
                m.setEstado(rs.getBoolean("estado"));
                materias.add(m);
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(MateriaData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return materias;
    }

    public void actualizarMateria(Materia m) {
        String sql = "UPDATE materia SET nombre = ?, estado = ? WHERE idMateria = ?";  //1

        try {
            PreparedStatement ps = con.prepareStatement(sql); //2
            ps.setString(1, m.getNombre());
            ps.setBoolean(2, m.isEstado());
            ps.setInt(3, m.getIdMateria());
            ps.executeUpdate();     // 3
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(MateriaData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  // UPDATE SET

    public void bajaLogicaMateria(int id) {
        String sql = "UPDATE materia SET estado = 0 WHERE idMateria = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(MateriaData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  // baja lógica

    public void borrarMateria(int id) {
        String sql = "DELETE FROM materia WHERE idMateria = ?";  //1

        try {
            PreparedStatement ps = con.prepareStatement(sql); //2
            ps.setInt(1, id);
            ps.executeUpdate();     // 3
            ps.close();  // 4

        } catch (SQLException ex) {
            Logger.getLogger(MateriaData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }// DELETE
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.grupo9_transversal_sgulp;

import com.mycompany.grupo9_transversal_sgulp.modelo.Alumno;
import com.mycompany.grupo9_transversal_sgulp.persistencia.AlumnoData;
import com.mycompany.grupo9_transversal_sgulp.persistencia.miConexion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

/**
 *
 * @author Grupo9
 */
public class Grupo9_TRANSVERSAL_SGULP {

    private static final String HOST = "localhost";
    private static final String PORT = "3307";
    private static final String DB_NAME = "grupo9_universidad";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mariadb://" + HOST + ":" + PORT + "/" + DB_NAME;
        miConexion conexion = new miConexion(
                url,
                USER,
                PASSWORD
        );
        Connection conn = conexion.buscarConexion();
        if (conn != null) {
            System.out.println("conected");
        } else {
            System.out.println("connection error");
        }
    }

    public static void insertarAlumnosIniciales(AlumnoData alumnoData) throws SQLException {
        Alumno[] alumnos = new Alumno[]{
            new Alumno(34421846, "Franco Magallanes", Date.valueOf("1989-01-02"), true),
            new Alumno(34877066, "Jessica Auriol", Date.valueOf("1989-10-20"), true),
            new Alumno(35915707, "Gonzalo Exequiel Martin", Date.valueOf("1991-06-10"), true),
            new Alumno(30334915, "Omar Matijas", Date.valueOf("1983-07-06"), true)
        };
        for (Alumno alumno : alumnos) {
            // alumnoData.insertarAlumno(alumno);
        }
    }
}

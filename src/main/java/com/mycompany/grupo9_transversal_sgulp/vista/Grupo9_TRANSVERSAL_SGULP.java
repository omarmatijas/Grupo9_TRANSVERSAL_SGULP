/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.grupo9_transversal_sgulp.vista;

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
    private static final String PORT = "3306";
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
       try {
            if (conexion.buscarConexion() == null) {
                throw new SQLException("Error de conexión: verifique usuario, contraseña, nombre de bbdd, host y puerto (tal vez alternar entre 3306 y 3307)");
            }
            AlumnoData alumnoData = new AlumnoData(conexion);
            verificarAlumnos(alumnoData);
            listarAlumnos(alumnoData);
        }finally {
            conexion.cerrarConexion();
        }
    }

    public static void listarAlumnos(AlumnoData alumnoData) throws SQLException {
        for (Alumno alumno : alumnoData.listarAlumnos()) {
            System.out.println(alumno);
        }
    }

    public static void verificarAlumnos(AlumnoData alumnoData) throws SQLException {
        if (alumnoData.ningunAlumno()) {
            insertarAlumnosIniciales(alumnoData);
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
            alumnoData.insertarAlumno(alumno);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.grupo9_transversal_sgulp.vista;

import com.mycompany.grupo9_transversal_sgulp.modelo.Alumno;
import com.mycompany.grupo9_transversal_sgulp.persistencia.AlumnoData;
import com.mycompany.grupo9_transversal_sgulp.persistencia.miConexion;
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
        try {
            if (conexion.buscarConexion() == null) {
                throw new SQLException("Error de conexión: verifique usuario, contraseña, nombre de bbdd, host y puerto (tal vez alternar entre 3306 y 3307)");
            }
            AlumnoData alumnoData = new AlumnoData(conexion);
            verificarAlumnos(alumnoData);
            listarAlumnos(alumnoData);
            System.out.println("--------------------------------");
            testAlumnoData(alumnoData);
            System.out.println("--------**********---------");
            listarAlumnos(alumnoData);

        } finally {
            conexion.cerrarConexion();
        }
    }

    public static void testAlumnoData(AlumnoData alumnoData) throws SQLException {
        Alumno encontrado = alumnoData.buscarAlumno(1);
        if (encontrado != null) {
            System.out.println("Alumno encontrado: " + encontrado);
        } else {
            System.out.println("No existe un alumno con ese id");
        }

        int DNI = 30334915;
        encontrado = alumnoData.buscarAlumnoPorDni(DNI);
        if (encontrado != null) {
            System.out.println("Alumno DNI encontrado: " + encontrado);
        } else {
            System.out.println("No existe un alumno con DNI " + DNI);
        }
        // Prueba A: actualizar un alumno que existe.
        Alumno encontrado2 = alumnoData.buscarAlumnoPorDni(35915707);
        if (encontrado2 != null) {
            String nombreAnterior = encontrado2.getNombre();
            encontrado2.setNombre("Pepe Argento");
            alumnoData.actualizarAlumno(encontrado2);
            Alumno actualizado = alumnoData.buscarAlumnoPorDni(35915707);
            String nombreNuevo = actualizado.getNombre();
            System.out.println("Se actualizo nombre de " + nombreAnterior + " a " + nombreNuevo);
            actualizado.setNombre(nombreAnterior);
            alumnoData.actualizarAlumno(actualizado);
            System.out.println("Se actualizo nombre de " + nombreNuevo + " a " + nombreAnterior);
        }
        // Prueba B: actualizar un alumno que no existe
        Alumno inexistente = new Alumno(999, 11111111, "Alumno Fantasma", Date.valueOf("2000-01-01"), true);
        try {
            alumnoData.actualizarAlumno(inexistente);
            System.out.println("ERROR: se actualizó un alumno que no existe");
        } catch (SQLException ex) {
            System.out.println("OK, se detecto el error: " + ex.getMessage());
        }

        // Prueba: baja lógica
        Alumno paraBaja = alumnoData.buscarAlumnoPorDni(34877066);
        if (paraBaja != null) {
            alumnoData.bajaLogicaAlumno(paraBaja.getIdAlumno());
            Alumno dadoDeBaja = alumnoData.buscarAlumno(paraBaja.getIdAlumno());
            System.out.println("Baja lógica de " + dadoDeBaja + ", activo=" + dadoDeBaja.isActivo());
        } else {
            System.out.println("No existe el alumno para la baja lógica");
        }

        // Prueba: reactivación lógica
        Alumno paraReactivar = alumnoData.buscarAlumnoPorDni(34877066);
        if (paraReactivar != null) {
            alumnoData.reactivacionLogicaAlumno(paraReactivar.getIdAlumno());
            Alumno reactivado = alumnoData.buscarAlumno(paraReactivar.getIdAlumno());
            System.out.println("Reactivación lógica de " + reactivado + ", activo=" + reactivado.isActivo());
        } else {
            System.out.println("No existe el alumno para la reactivación lógica");
        }

        int idBorrar = (int) (Math.random() * 10);
        alumnoData.borrarAlumno(idBorrar, false, false);
        Alumno borrado = alumnoData.buscarAlumno(idBorrar);
        if (borrado == null) {
            System.out.println("Se borró el alumno id= " + idBorrar);
        } else {
            System.out.println("ERROR: el alumno id= " + idBorrar + " sigue existiendo");
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
            new Alumno(34421846, "Franco Magallanes", Date.valueOf("1989-01-02")),
            new Alumno(34877066, "Jessica Auriol", Date.valueOf("1989-10-20")),
            new Alumno(35915707, "Gonzalo Exequiel Martin Asis", Date.valueOf("1991-06-10")),
            new Alumno(30334915, "Omar Matijas", Date.valueOf("1983-07-06"))
        };
        for (Alumno alumno : alumnos) {
            alumnoData.insertarAlumno(alumno);
        }
    }
}

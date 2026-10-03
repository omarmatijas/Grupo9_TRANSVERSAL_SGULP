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
}

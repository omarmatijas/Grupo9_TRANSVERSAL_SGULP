/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.grupo9_transversal_sgulp;

import com.mycompany.grupo9_transversal_sgulp.persistencia.miConexion;
import java.sql.Connection;

/**
 *
 * @author Grupo9
 */
public class Grupo9_TRANSVERSAL_SGULP {

    private static final String HOST = "localhost";
    private static final String PORT = "3307";
    private static final String DB_NAME = "grupo9_universidad";

    public static void main(String[] args) {
        String url = "jdbc:mariadb://" + HOST + ":" + PORT + "/" + DB_NAME;
        miConexion conexion = new miConexion(
                url,
                "root",
                ""
        );
        Connection conn = conexion.buscarConexion();
        if (conn != null) {
            System.out.println("conected");
        } else {
            System.out.println("connection error");
        }
    }
}

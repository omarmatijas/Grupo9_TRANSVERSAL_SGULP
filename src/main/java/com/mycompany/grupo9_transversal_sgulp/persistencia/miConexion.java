/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.grupo9_transversal_sgulp.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Grupo9
 */
public class miConexion {
    private final String url;
    private final String usuario;
    private final String password;

    private static Connection conexion = null;

    public miConexion(String url, String usuario, String password) {
        this.url = url;
        this.usuario = usuario;
        this.password = password;
    }

    public Connection buscarConexion() {
        try {
            // Se reconecta si es la primera vez o si la conexión se cerró
            if (conexion == null || conexion.isClosed()) {
                Class.forName("org.mariadb.jdbc.Driver");
                conexion = DriverManager.getConnection(url, usuario, password);
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("No se pudo cargar el driver de MariaDB. ¿Está la dependencia en el pom.xml? " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("No se pudo conectar a la base de datos: " + ex.getMessage());
        }
        return conexion;
    }

    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error al cerrar la conexión: " + ex.getMessage());
        }
    }
}

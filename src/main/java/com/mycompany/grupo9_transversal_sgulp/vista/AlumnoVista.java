/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.grupo9_transversal_sgulp.vista;

import com.mycompany.grupo9_transversal_sgulp.modelo.Alumno;
import com.mycompany.grupo9_transversal_sgulp.modelo.Cursada;
import com.mycompany.grupo9_transversal_sgulp.persistencia.AlumnoData;
import com.mycompany.grupo9_transversal_sgulp.persistencia.CursadaData;
import com.mycompany.grupo9_transversal_sgulp.persistencia.miConexion;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Grupo9
 */
public class AlumnoVista extends javax.swing.JInternalFrame {

    private static final int CLASES_TOTALES = 30;

    private AlumnoData alumnoData;
    private CursadaData cursadaData;
    private DefaultTableModel modeloTabla;
    private DefaultTableModel modeloMaterias;

    public AlumnoVista(miConexion conexion) {
        alumnoData = new AlumnoData(conexion);
        cursadaData = new CursadaData(conexion);
        initComponents();
        armarTabla();
        armarTablaMaterias();
        cargarTabla();
        limpiar();

    }

    private void armarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Fecha de nacimiento");
        modeloTabla.addColumn("Estado");
        tablaAlumnos.setModel(modeloTabla);
    }

    private void armarTablaMaterias() {
        modeloMaterias = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        modeloMaterias.addColumn("Año");
        modeloMaterias.addColumn("Materia");
        modeloMaterias.addColumn("Nota");
        modeloMaterias.addColumn("Asistencia");
        tablaMaterias.setModel(modeloMaterias);
        tablaMaterias.setAutoCreateRowSorter(true);
        tablaMaterias.getColumnModel().getColumn(0).setPreferredWidth(70);
        tablaMaterias.getColumnModel().getColumn(1).setPreferredWidth(180);
        tablaMaterias.getColumnModel().getColumn(2).setPreferredWidth(70);
        tablaMaterias.getColumnModel().getColumn(3).setPreferredWidth(130);
    }

    private void mostrarAlumno(Alumno a) {
        txtId.setText(String.valueOf(a.getIdAlumno()));
        txtDni.setText(String.valueOf(a.getDni()));
        txtNombre.setText(a.getNombre());
        dcFechaNac.setDate(a.getFecNac());
        chkActivo.setSelected(a.isActivo());
        cargarMaterias(a.getIdAlumno());
        actualizarBotonesEstado();
    }

    private void limpiar() {
        txtId.setText("");
        txtDni.setText("");
        txtNombre.setText("");
        dcFechaNac.setDate(null);
        chkActivo.setSelected(true);
        tablaAlumnos.clearSelection();
        if (modeloMaterias != null) {
            modeloMaterias.setRowCount(0);
        }
        actualizarBotonesEstado();
    }

    private void actualizarBotonesEstado() {
        boolean cargado = !txtId.getText().isEmpty();
        boolean activo = chkActivo.isSelected();
        btnAlta.setEnabled(cargado && !activo);
        btnBaja.setEnabled(cargado && activo);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            for (Alumno a : alumnoData.listarAlumnos()) {
                modeloTabla.addRow(new Object[]{
                    a.getIdAlumno(),
                    a.getDni(),
                    a.getNombre(),
                    a.getFecNac(),
                    a.isActivo() ? "Activo" : "Inactivo"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los alumnos: " + ex.getMessage());
        }
    }

    private void cargarMaterias(int idAlumno) {
        modeloMaterias.setRowCount(0);
        List<Cursada> cursadas;
        try {
            cursadas = cursadaData.listarMateriasPorAlumno(idAlumno);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar las materias: " + ex.getMessage());
            return;
        }
        for (Cursada c : cursadas) {
            modeloMaterias.addRow(new Object[]{
                c.getCursa(),
                c.getMateria().getNombre(),
                textoNota(c.getNota()),
                textoAsistencia(c.getAsist())
            });
        }
    }

    private static String textoNota(float nota) {
        if (nota == 0f) {
            return "";
        }
        return String.format(Locale.US, "%.1f", nota);
    }

    private static String textoAsistencia(float asist) {
        int clases = Math.round(asist);
        if (clases < 0) {
            clases = 0;
        } else if (clases > CLASES_TOTALES) {
            clases = CLASES_TOTALES;
        }
        int porcentaje = Math.round(clases * 100f / CLASES_TOTALES);
        return clases + "/" + CLASES_TOTALES + " (" + porcentaje + "%)";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dcFechaNac = new com.toedter.calendar.JDateChooser();
        txtId = new javax.swing.JTextField();
        txtDni = new javax.swing.JTextField();
        chkActivo = new javax.swing.JCheckBox();
        btnBuscar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnBaja = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnAlta = new javax.swing.JButton();
        txtNombre = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAlumnos = new javax.swing.JTable();
        lblId = new javax.swing.JLabel();
        lblDni = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblFecNac = new javax.swing.JLabel();
        lblMaterias = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaMaterias = new javax.swing.JTable();

        setClosable(true);
        setTitle("Gestión de Alumnos");

        txtId.setEditable(false);
        txtId.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        chkActivo.setText("Activo");
        chkActivo.setEnabled(false);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        btnNuevo.setBackground(new java.awt.Color(102, 255, 255));
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);

        btnGuardar.setBackground(new java.awt.Color(153, 204, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnBaja.setBackground(new java.awt.Color(255, 153, 51));
        btnBaja.setText("Baja");
        btnBaja.addActionListener(this::btnBajaActionPerformed);

        btnEliminar.setBackground(new java.awt.Color(255, 51, 51));
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnAlta.setBackground(new java.awt.Color(153, 255, 153));
        btnAlta.setText("Alta");
        btnAlta.addActionListener(this::btnAltaActionPerformed);

        tablaAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaAlumnos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaAlumnosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaAlumnos);

        lblId.setText("ID: ");

        lblDni.setText("DNI: ");

        lblNombre.setText("Nombre: ");

        lblFecNac.setText("Fec. Nac.:");

        lblMaterias.setText("Materias:");

        tablaMaterias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tablaMaterias);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnNuevo)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblId)
                                    .addComponent(lblDni)
                                    .addComponent(lblNombre)
                                    .addComponent(lblFecNac))
                                .addGap(18, 37, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtNombre)
                                        .addComponent(txtDni)
                                        .addComponent(dcFechaNac, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(chkActivo)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBuscar)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(92, 92, 92))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAlta)
                                .addGap(18, 18, 18)
                                .addComponent(btnBaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(55, 55, 55))
                            .addComponent(lblMaterias, javax.swing.GroupLayout.Alignment.LEADING))))
                .addGap(52, 52, 52))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAlta, btnBaja, btnNuevo});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(btnEliminar)
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblId))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDni)
                            .addComponent(btnBuscar))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNombre))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dcFechaNac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblFecNac)
                            .addComponent(btnGuardar))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkActivo)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAlta)
                    .addComponent(btnBaja))
                .addGap(2, 2, 2)
                .addComponent(lblMaterias, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // Validación del DNI
        int dni;
        try {
            dni = Integer.parseInt(txtDni.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El DNI debe ser numérico.");
            return;
        }
        if (dni <= 0) {
            JOptionPane.showMessageDialog(this, "El DNI debe ser mayor que cero.");
            return;
        }

        // Validación del nombre
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
            return;
        }
        if (nombre.length() > 30) {
            JOptionPane.showMessageDialog(this, "El nombre no puede superar los 30 caracteres.");
            return;
        }

        // Validacion de la fecha de nacimiento
        if (dcFechaNac.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Elegí una fecha de nacimiento.");
            return;
        }
        LocalDate fecNac = aLocalDate(dcFechaNac.getDate());
        if (fecNac.isAfter(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "La fecha de nacimiento no puede ser futura.");
            return;
        }
        java.sql.Date fecha = java.sql.Date.valueOf(fecNac);
        try {
            if (txtId.getText().isEmpty()) {
                Alumno nuevo = new Alumno(dni, nombre, fecha);
                alumnoData.insertarAlumno(nuevo);
                JOptionPane.showMessageDialog(this, "Alumno guardado con id " + nuevo.getIdAlumno());
            } else {
                int id = Integer.parseInt(txtId.getText());
                Alumno a = new Alumno(id, dni, nombre, fecha, chkActivo.isSelected());
                alumnoData.actualizarAlumno(a);
                JOptionPane.showMessageDialog(this, "Alumno actualizado.");
            }
            limpiar();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        try {
            int dni;

            try {
                dni = Integer.parseInt(txtDni.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingresá un DNI numérico.");
                return;
            }
            Alumno a = alumnoData.buscarAlumnoPorDni(dni);
            if (a == null) {
                JOptionPane.showMessageDialog(this, "No existe un alumno con ese DNI.");
            } else {
                mostrarAlumno(a);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo buscar el alumno: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        limpiar();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAltaActionPerformed
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccioná un alumno de la tabla.");
            return;
        }
        try {
            alumnoData.reactivacionLogicaAlumno(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, "Alumno dado de alta.");
            limpiar();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo dar de alta: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnAltaActionPerformed

    private void btnBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBajaActionPerformed
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccioná un alumno de la tabla.");
                return;
            }
            alumnoData.bajaLogicaAlumno(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, "Alumno dado de baja.");
            limpiar();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo dar de baja: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnBajaActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccioná un alumno de la tabla.");
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Eliminar definitivamente al alumno?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            alumnoData.borrarAlumno(Integer.parseInt(txtId.getText()), false);
            JOptionPane.showMessageDialog(this, "Alumno eliminado.");
            limpiar();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void tablaAlumnosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaAlumnosMouseClicked
        int fila = tablaAlumnos.getSelectedRow();
        if (fila >= 0) {
            int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            Alumno a = null;
            try {
                a = alumnoData.buscarAlumno(id);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo cargar el alumno: " + ex.getMessage());
            }
            if (a != null) {
                mostrarAlumno(a);
            }
        }

    }//GEN-LAST:event_tablaAlumnosMouseClicked

    private LocalDate aLocalDate(Date fecha) {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlta;
    private javax.swing.JButton btnBaja;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JCheckBox chkActivo;
    private com.toedter.calendar.JDateChooser dcFechaNac;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDni;
    private javax.swing.JLabel lblFecNac;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblMaterias;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JTable tablaAlumnos;
    private javax.swing.JTable tablaMaterias;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables

}

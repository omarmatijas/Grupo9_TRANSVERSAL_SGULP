/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.grupo9_transversal_sgulp.vista;

import com.mycompany.grupo9_transversal_sgulp.modelo.Cursada;
import com.mycompany.grupo9_transversal_sgulp.modelo.Materia;
import com.mycompany.grupo9_transversal_sgulp.persistencia.CursadaData;
import com.mycompany.grupo9_transversal_sgulp.persistencia.MateriaData;
import com.mycompany.grupo9_transversal_sgulp.persistencia.miConexion;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Grupo9
 */
public class CursadaPorMateria extends javax.swing.JInternalFrame {

    private static final int CLASES_TOTALES = 30;

    private MateriaData materiaData;
    private CursadaData cursadaData;
    private DefaultTableModel modeloTabla;
    private List<Materia> materias = new ArrayList<>();
    private List<Cursada> cursadasEnTabla = new ArrayList<>();
    private boolean actualizando;

    public CursadaPorMateria(miConexion conexion) {
        materiaData = new MateriaData(conexion);
        cursadaData = new CursadaData(conexion);
        initComponents();
        spAnio.setModel(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, 2100, 1));
        armarTabla();
        cargarMaterias();
    }

    private void armarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Nota");
        modeloTabla.addColumn("Asistencia");
        tablaAlumnos.setModel(modeloTabla);
    }

    private void cargarMaterias() {
        materias.clear();
        try {
            materias.addAll(materiaData.listarMaterias());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar las materias: " + ex.getMessage());
        }
        filtrarMaterias();
    }

    private void filtrarMaterias() {
        Materia seleccionada = (Materia) cboMateria.getSelectedItem();
        String filtro = txtFiltroMateria.getText().trim().toLowerCase(Locale.ROOT);
        actualizando = true;
        cboMateria.removeAllItems();
        int seleccionar = 0;
        int indice = 0;
        for (Materia m : materias) {
            if (!coincideFiltro(m, filtro)) {
                continue;
            }
            cboMateria.addItem(m);
            if (seleccionada != null && m.getIdMateria() == seleccionada.getIdMateria()) {
                seleccionar = indice;
            }
            indice++;
        }
        if (cboMateria.getItemCount() > 0) {
            cboMateria.setSelectedIndex(seleccionar);
        }
        actualizando = false;
        cargarAlumnos();
    }

    private void cargarAlumnos() {
        if (actualizando || modeloTabla == null) {
            return;
        }
        modeloTabla.setRowCount(0);
        cursadasEnTabla.clear();
        txtNota.setText("");
        txtAsistencia.setText("");
        Materia materia = (Materia) cboMateria.getSelectedItem();
        if (materia == null) {
            return;
        }
        int anio = (Integer) spAnio.getValue();
        try {
            cursadasEnTabla.addAll(cursadaData.listarCursadasPorMateria(materia.getIdMateria(), anio));
            for (Cursada c : cursadasEnTabla) {
                modeloTabla.addRow(new Object[]{
                    c.getAlumno().getDni(),
                    c.getAlumno().getNombre(),
                    textoNota(c.getNota()),
                    textoClases(c.getAsist())
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los alumnos: " + ex.getMessage());
        }
    }

    private static String textoNota(float nota) {
        if (nota == 0f) {
            return "";
        }
        return String.format(Locale.US, "%.1f", nota);
    }

    private static String textoClases(float asist) {
        int clases = Math.round(asist);
        if (clases < 0) {
            clases = 0;
        } else if (clases > CLASES_TOTALES) {
            clases = CLASES_TOTALES;
        }
        return String.valueOf(clases);
    }

    private static boolean coincideFiltro(Materia materia, String filtro) {
        if (filtro.isEmpty()) {
            return true;
        }
        String nombre = materia.getNombre() == null ? "" : materia.getNombre().toLowerCase(Locale.ROOT);
        return nombre.contains(filtro) || String.valueOf(materia.getIdMateria()).contains(filtro);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblAnio = new javax.swing.JLabel();
        spAnio = new javax.swing.JSpinner();
        lblFiltro = new javax.swing.JLabel();
        txtFiltroMateria = new javax.swing.JTextField();
        lblMateria = new javax.swing.JLabel();
        cboMateria = new javax.swing.JComboBox<>();
        lblAlumnos = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAlumnos = new javax.swing.JTable();
        lblNota = new javax.swing.JLabel();
        txtNota = new javax.swing.JTextField();
        lblAsistencia = new javax.swing.JLabel();
        txtAsistencia = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();

        setClosable(true);
        setTitle("Gestión de Asistencia");

        lblAnio.setText("Año: ");

        spAnio.addChangeListener(this::spAnioStateChanged);

        lblFiltro.setText("Nombre de materia:");

        txtFiltroMateria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroMateriaKeyReleased(evt);
            }
        });

        lblMateria.setText("Materia:");

        cboMateria.addActionListener(this::cboMateriaActionPerformed);

        lblAlumnos.setText("Alumnos inscriptos:");

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

        lblNota.setText("Nota:");

        txtNota.setEditable(true);

        lblAsistencia.setText("Asistencia:");

        txtAsistencia.setEditable(true);

        btnGuardar.setBackground(new java.awt.Color(153, 204, 255));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                    .addComponent(lblAlumnos)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblNota)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNota, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblAsistencia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblAnio)
                        .addGap(18, 18, 18)
                        .addComponent(spAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFiltro)
                        .addGap(18, 18, 18)
                        .addComponent(txtFiltroMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblMateria)
                        .addGap(18, 18, 18)
                        .addComponent(cboMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAnio)
                    .addComponent(spAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFiltro)
                    .addComponent(txtFiltroMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMateria)
                    .addComponent(cboMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(lblAlumnos)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNota)
                    .addComponent(txtNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAsistencia)
                    .addComponent(txtAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar))
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void spAnioStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spAnioStateChanged
        cargarAlumnos();
    }//GEN-LAST:event_spAnioStateChanged

    private void txtFiltroMateriaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroMateriaKeyReleased
        filtrarMaterias();
    }//GEN-LAST:event_txtFiltroMateriaKeyReleased

    private void cboMateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMateriaActionPerformed
        cargarAlumnos();
    }//GEN-LAST:event_cboMateriaActionPerformed

    private void tablaAlumnosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaAlumnosMouseClicked
        int fila = tablaAlumnos.getSelectedRow();
        if (fila < 0 || fila >= cursadasEnTabla.size()) {
            txtNota.setText("");
            txtAsistencia.setText("");
            return;
        }
        Cursada cursada = cursadasEnTabla.get(fila);
        txtNota.setText(textoNota(cursada.getNota()));
        txtAsistencia.setText(textoClases(cursada.getAsist()));
    }//GEN-LAST:event_tablaAlumnosMouseClicked

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        int fila = tablaAlumnos.getSelectedRow();
        if (fila < 0 || fila >= cursadasEnTabla.size()) {
            JOptionPane.showMessageDialog(this, "Seleccioná un alumno de la tabla.");
            return;
        }
        String texto = txtNota.getText().trim().replace(',', '.');
        float nota;
        if (texto.isEmpty()) {
            nota = 0f;
        } else {
            try {
                nota = Float.parseFloat(texto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La nota debe ser un número. Ej: 8.5");
                return;
            }
        }
        if (nota < 0f || nota > 10f) {
            JOptionPane.showMessageDialog(this, "La nota debe estar entre 0 y 10.");
            return;
        }
        Integer asist = leerAsistencia();
        if (asist == null) {
            return;
        }
        Cursada cursada = cursadasEnTabla.get(fila);
        try {
            cursadaData.actualizarNota(cursada.getAlumno().getIdAlumno(), cursada.getMateria().getIdMateria(), cursada.getCursa(), nota);
            cursadaData.actualizarAsistencia(cursada.getAlumno().getIdAlumno(), cursada.getMateria().getIdMateria(), cursada.getCursa(), asist);
            JOptionPane.showMessageDialog(this, "Nota y asistencia guardadas.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar: " + ex.getMessage());
            return;
        }
        int idCursada = cursada.getIdCursada();
        cargarAlumnos();
        for (int i = 0; i < cursadasEnTabla.size(); i++) {
            if (cursadasEnTabla.get(i).getIdCursada() == idCursada) {
                tablaAlumnos.setRowSelectionInterval(i, i);
                txtNota.setText(textoNota(nota));
                txtAsistencia.setText(String.valueOf(asist));
                return;
            }
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private Integer leerAsistencia() {
        String texto = txtAsistencia.getText().trim();
        if (texto.isEmpty()) {
            return 0;
        }
        try {
            int clases = Integer.parseInt(texto);
            if (clases < 0 || clases > CLASES_TOTALES) {
                JOptionPane.showMessageDialog(this, "La asistencia debe estar entre 0 y 30.");
                return null;
            }
            return clases;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La asistencia debe ser un número entero entre 0 y 30.");
            return null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<Materia> cboMateria;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAlumnos;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblAsistencia;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JLabel lblMateria;
    private javax.swing.JLabel lblNota;
    private javax.swing.JSpinner spAnio;
    private javax.swing.JTable tablaAlumnos;
    private javax.swing.JTextField txtAsistencia;
    private javax.swing.JTextField txtFiltroMateria;
    private javax.swing.JTextField txtNota;
    // End of variables declaration//GEN-END:variables
}

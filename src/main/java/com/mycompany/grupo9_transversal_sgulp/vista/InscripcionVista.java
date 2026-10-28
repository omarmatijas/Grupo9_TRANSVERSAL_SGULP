/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.grupo9_transversal_sgulp.vista;

import com.mycompany.grupo9_transversal_sgulp.modelo.Alumno;
import com.mycompany.grupo9_transversal_sgulp.modelo.Cursada;
import com.mycompany.grupo9_transversal_sgulp.modelo.Materia;
import com.mycompany.grupo9_transversal_sgulp.persistencia.AlumnoData;
import com.mycompany.grupo9_transversal_sgulp.persistencia.CursadaData;
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
public class InscripcionVista extends javax.swing.JInternalFrame {

    private static final int CLASES_TOTALES = 30;

    private AlumnoData alumnoData;
    private CursadaData cursadaData;
    private DefaultTableModel modeloTabla;
    private List<Materia> materiasEnTabla = new ArrayList<>();
    private List<Cursada> cursadasEnTabla = new ArrayList<>();
    private boolean actualizando;

    public InscripcionVista(miConexion conexion) {
        alumnoData = new AlumnoData(conexion);
        cursadaData = new CursadaData(conexion);
        initComponents();
        spAnio.setModel(new SpinnerNumberModel(LocalDate.now().getYear(), 2000, 2100, 1));
        rbNoInscriptas.setSelected(true);
        armarTabla();
        cargarAlumnos();
        cargarTabla();
    }

    private void armarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaMaterias.setModel(modeloTabla);
    }

    private void armarColumnas(boolean inscriptas) {
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(0);
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Materia");
        if (inscriptas) {
            modeloTabla.addColumn("Año");
            modeloTabla.addColumn("Nota");
            modeloTabla.addColumn("Asistencia");
            tablaMaterias.getColumnModel().getColumn(2).setPreferredWidth(60);
            tablaMaterias.getColumnModel().getColumn(3).setPreferredWidth(60);
            tablaMaterias.getColumnModel().getColumn(4).setPreferredWidth(90);
        }
    }

    private void cargarAlumnos() {
        try {
            for (Alumno a : alumnoData.listarAlumnos()) {
                if (a.isActivo()) {
                    cboAlumno.addItem(a);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los alumnos: " + ex.getMessage());
        }
    }

    private void cargarTabla() {
        boolean inscriptas = rbInscriptas.isSelected();
        armarColumnas(inscriptas);
        materiasEnTabla.clear();
        cursadasEnTabla.clear();
        txtNota.setText("");
        txtAsistencia.setText("");
        actualizarBotones();
        Alumno alumno = (Alumno) cboAlumno.getSelectedItem();
        if (alumno == null) {
            return;
        }
        int anio = (Integer) spAnio.getValue();
        boolean todosLosAnios = inscriptas && chkTodosLosAnios.isSelected();
        String filtro = txtFiltroMateria.getText().trim().toLowerCase(Locale.ROOT);
        try {
            if (!inscriptas) {
                materiasEnTabla.addAll(cursadaData.listarMateriasNoCursadas(alumno.getIdAlumno(), anio));
                if (!filtro.isEmpty()) {
                    materiasEnTabla.removeIf(m -> !coincideFiltro(m, filtro));
                }
                for (Materia m : materiasEnTabla) {
                    modeloTabla.addRow(new Object[]{m.getIdMateria(), m.getNombre()});
                }
            } else {
                for (Cursada c : cursadaData.listarMateriasPorAlumno(alumno.getIdAlumno())) {
                    if ((!todosLosAnios && c.getCursa() != anio) || !coincideFiltro(c.getMateria(), filtro)) {
                        continue;
                    }
                    materiasEnTabla.add(c.getMateria());
                    cursadasEnTabla.add(c);
                    modeloTabla.addRow(new Object[]{
                        c.getMateria().getIdMateria(),
                        c.getMateria().getNombre(),
                        c.getCursa(),
                        textoNota(c.getNota()),
                        textoAsistencia(c.getAsist())
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar las materias: " + ex.getMessage());
        }
    }

    private void actualizarBotones() {
        boolean inscriptas = rbInscriptas.isSelected();
        btnInscribir.setEnabled(!inscriptas);
        btnAnular.setEnabled(inscriptas);
        chkTodosLosAnios.setEnabled(inscriptas);
        spAnio.setEnabled(!(inscriptas && chkTodosLosAnios.isSelected()));
        txtNota.setEnabled(inscriptas);
        txtAsistencia.setEnabled(inscriptas);
        btnGuardar.setEnabled(inscriptas);
    }

    private static boolean coincideFiltro(Materia materia, String filtro) {
        if (filtro.isEmpty()) {
            return true;
        }
        String nombre = materia.getNombre() == null ? "" : materia.getNombre().toLowerCase(Locale.ROOT);
        return nombre.contains(filtro) || String.valueOf(materia.getIdMateria()).contains(filtro);
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

    private static String textoAsistencia(float asist) {
        int clases = Math.round(asist);
        if (clases < 0) {
            clases = 0;
        } else if (clases > CLASES_TOTALES) {
            clases = CLASES_TOTALES;
        }
        int porcentaje = Math.round(clases * 100f / CLASES_TOTALES);
        return porcentaje + "%";
    }

    private void sincronizarDniAlumno() {
        Alumno alumno = (Alumno) cboAlumno.getSelectedItem();
        if (alumno == null) {
            return;
        }
        String dni = String.valueOf(alumno.getDni());
        if (!dni.equals(txtDni.getText().trim())) {
            actualizando = true;
            txtDni.setText(dni);
            actualizando = false;
        }
    }

    private void seleccionarAlumnoPorDni() {
        String texto = txtDni.getText().trim();
        if (texto.isEmpty()) {
            return;
        }
        int dni;
        try {
            dni = Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El DNI debe ser numérico.");
            return;
        }
        for (int i = 0; i < cboAlumno.getItemCount(); i++) {
            if (cboAlumno.getItemAt(i).getDni() == dni) {
                cboAlumno.setSelectedIndex(i);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "No hay un alumno activo con ese DNI.");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoMaterias = new javax.swing.ButtonGroup();
        lblAlumno = new javax.swing.JLabel();
        cboAlumno = new javax.swing.JComboBox<>();
        lblAnio = new javax.swing.JLabel();
        spAnio = new javax.swing.JSpinner();
        rbNoInscriptas = new javax.swing.JRadioButton();
        rbInscriptas = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaMaterias = new javax.swing.JTable();
        btnInscribir = new javax.swing.JButton();
        btnAnular = new javax.swing.JButton();
        lblDni = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        lblFiltro = new javax.swing.JLabel();
        txtFiltroMateria = new javax.swing.JTextField();
        chkTodosLosAnios = new javax.swing.JCheckBox();
        lblNota = new javax.swing.JLabel();
        txtNota = new javax.swing.JTextField();
        lblAsistencia = new javax.swing.JLabel();
        txtAsistencia = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();

        setClosable(true);
        setTitle("Gestión de Inscripciones");

        lblAlumno.setText("Alumno: ");

        cboAlumno.addActionListener(this::cboAlumnoActionPerformed);

        lblAnio.setText("Año: ");

        spAnio.addChangeListener(this::spAnioStateChanged);

        grupoMaterias.add(rbNoInscriptas);
        rbNoInscriptas.setForeground(new java.awt.Color(204, 102, 0));
        rbNoInscriptas.setText("Materias no inscriptas");
        rbNoInscriptas.addActionListener(this::rbNoInscriptasActionPerformed);

        grupoMaterias.add(rbInscriptas);
        rbInscriptas.setForeground(new java.awt.Color(0, 102, 51));
        rbInscriptas.setText("Materias inscriptas");
        rbInscriptas.addActionListener(this::rbInscriptasActionPerformed);

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
        tablaMaterias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMateriasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaMaterias);

        btnInscribir.setBackground(new java.awt.Color(153, 255, 153));
        btnInscribir.setText("Inscribir");
        btnInscribir.addActionListener(this::btnInscribirActionPerformed);

        btnAnular.setBackground(new java.awt.Color(255, 102, 102));
        btnAnular.setText("Anular inscripción");
        btnAnular.addActionListener(this::btnAnularActionPerformed);

        lblDni.setText("Buscar por DNI");

        txtDni.addActionListener(this::txtDniActionPerformed);

        lblFiltro.setText("Nombre de materia:");

        txtFiltroMateria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroMateriaKeyReleased(evt);
            }
        });

        chkTodosLosAnios.setText("Todos los años");
        chkTodosLosAnios.setEnabled(false);
        chkTodosLosAnios.addActionListener(this::chkTodosLosAniosActionPerformed);

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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rbInscriptas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbNoInscriptas))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAlumno)
                                .addGap(18, 18, 18)
                                .addComponent(cboAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(lblDni))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAnio)
                                .addGap(18, 18, 18)
                                .addComponent(spAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(chkTodosLosAnios))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblFiltro)
                                .addGap(18, 18, 18)
                                .addComponent(txtFiltroMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(101, 101, 101)
                            .addComponent(btnAnular))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnInscribir)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblNota)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtNota, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblAsistencia)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(93, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAnular, btnInscribir});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {rbInscriptas, rbNoInscriptas});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAlumno)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDni)
                        .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbInscriptas)
                    .addComponent(rbNoInscriptas))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAnio)
                    .addComponent(spAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkTodosLosAnios))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFiltro)
                    .addComponent(txtFiltroMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnInscribir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNota)
                            .addComponent(txtNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAsistencia)
                            .addComponent(txtAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGuardar))
                        .addGap(48, 48, 48)
                        .addComponent(btnAnular))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInscribirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInscribirActionPerformed
        int fila = tablaMaterias.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una materia de la tabla.");
            return;
        }
        Alumno alumno = (Alumno) cboAlumno.getSelectedItem();
        Materia materia = materiasEnTabla.get(fila);
        int anio = (Integer) spAnio.getValue();
        try {
            cursadaData.inscribir(new Cursada(alumno, materia, 0, 0, anio));
            JOptionPane.showMessageDialog(this, "Inscripción realizada.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo inscribir: " + ex.getMessage());
        }
        cargarTabla();
    }//GEN-LAST:event_btnInscribirActionPerformed

    private void btnAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnularActionPerformed
        int fila = tablaMaterias.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una materia de la tabla.");
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Anular la inscripción? Se pierden la nota y la asistencia cargadas.",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) {
            return;
        }
        Alumno alumno = (Alumno) cboAlumno.getSelectedItem();
        Materia materia = materiasEnTabla.get(fila);
        int anio = cursadasEnTabla.get(fila).getCursa();
        try {
            cursadaData.anularInscripcion(new Cursada(alumno, materia, 0, 0, anio));
            JOptionPane.showMessageDialog(this, "Inscripción anulada.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo anular: " + ex.getMessage());
        }
        cargarTabla();
    }//GEN-LAST:event_btnAnularActionPerformed

    private void cboAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAlumnoActionPerformed
        sincronizarDniAlumno();
        cargarTabla();
    }//GEN-LAST:event_cboAlumnoActionPerformed

    private void txtDniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDniActionPerformed
        if (!actualizando) {
            seleccionarAlumnoPorDni();
        }
    }//GEN-LAST:event_txtDniActionPerformed

    private void txtFiltroMateriaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroMateriaKeyReleased
        cargarTabla();
    }//GEN-LAST:event_txtFiltroMateriaKeyReleased

    private void rbNoInscriptasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNoInscriptasActionPerformed
        actualizando = true;
        chkTodosLosAnios.setSelected(false);
        actualizando = false;
        cargarTabla();
    }//GEN-LAST:event_rbNoInscriptasActionPerformed

    private void rbInscriptasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbInscriptasActionPerformed
        cargarTabla();
    }//GEN-LAST:event_rbInscriptasActionPerformed

    private void spAnioStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spAnioStateChanged
        cargarTabla();
    }//GEN-LAST:event_spAnioStateChanged

    private void chkTodosLosAniosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTodosLosAniosActionPerformed
        if (!actualizando) {
            cargarTabla();
        }
    }//GEN-LAST:event_chkTodosLosAniosActionPerformed

    private void tablaMateriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMateriasMouseClicked
        int fila = tablaMaterias.getSelectedRow();
        if (fila < 0 || fila >= cursadasEnTabla.size()) {
            txtNota.setText("");
            txtAsistencia.setText("");
            return;
        }
        txtNota.setText(textoNota(cursadasEnTabla.get(fila).getNota()));
        txtAsistencia.setText(textoClases(cursadasEnTabla.get(fila).getAsist()));
    }//GEN-LAST:event_tablaMateriasMouseClicked

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        int fila = tablaMaterias.getSelectedRow();
        if (fila < 0 || fila >= cursadasEnTabla.size()) {
            JOptionPane.showMessageDialog(this, "Seleccioná una materia inscripta.");
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
        Alumno alumno = (Alumno) cboAlumno.getSelectedItem();
        if (alumno == null) {
            JOptionPane.showMessageDialog(this, "Seleccioná un alumno.");
            return;
        }
        try {
            cursadaData.actualizarNota(alumno.getIdAlumno(), cursada.getMateria().getIdMateria(), cursada.getCursa(), nota);
            cursadaData.actualizarAsistencia(alumno.getIdAlumno(), cursada.getMateria().getIdMateria(), cursada.getCursa(), asist);
            JOptionPane.showMessageDialog(this, "Nota y asistencia guardadas.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar: " + ex.getMessage());
            return;
        }
        int idCursada = cursada.getIdCursada();
        cargarTabla();
        for (int i = 0; i < cursadasEnTabla.size(); i++) {
            if (cursadasEnTabla.get(i).getIdCursada() == idCursada) {
                tablaMaterias.setRowSelectionInterval(i, i);
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
    private javax.swing.JButton btnAnular;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnInscribir;
    private javax.swing.JComboBox<Alumno> cboAlumno;
    private javax.swing.JCheckBox chkTodosLosAnios;
    private javax.swing.ButtonGroup grupoMaterias;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAsistencia;
    private javax.swing.JLabel lblAlumno;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblDni;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JLabel lblNota;
    private javax.swing.JRadioButton rbInscriptas;
    private javax.swing.JRadioButton rbNoInscriptas;
    private javax.swing.JSpinner spAnio;
    private javax.swing.JTable tablaMaterias;
    private javax.swing.JTextField txtAsistencia;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtFiltroMateria;
    private javax.swing.JTextField txtNota;
    // End of variables declaration//GEN-END:variables
}

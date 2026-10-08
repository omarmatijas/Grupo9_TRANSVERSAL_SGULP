/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.grupo9_transversal_sgulp.modelo;

/**
 *
 * @author Gonza - Jessi
 */
public class Cursada {
    
    private int idCursada;
    private Alumno alumno;
    private Materia materia;
    private float nota;
    private float asist;
    private int cursa;

    public Cursada() {
    }

    public Cursada(Alumno alumno, Materia materia, float nota, float asist, int cursa) {
        this.alumno = alumno;
        this.materia = materia;
        this.nota = nota;
        this.asist = asist;
        this.cursa = cursa;
    }

    public Cursada(int idCursada, Alumno alumno, Materia materia, float nota, float asist, int cursa) {
        this.idCursada = idCursada;
        this.alumno = alumno;
        this.materia = materia;
        this.nota = nota;
        this.asist = asist;
        this.cursa = cursa;
    }

    public int getIdCursada() {
        return idCursada;
    }

    public void setIdCursada(int idCursada) {
        this.idCursada = idCursada;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public float getAsist() {
        return asist;
    }

    public void setAsist(float asist) {
        this.asist = asist;
    }

    public int getCursa() {
        return cursa;
    }

    public void setCursa(int cursa) {
        this.cursa = cursa;
    }

    @Override
    public String toString() {
        return alumno + " - " + materia + " (" + cursa + ")";
    }

}

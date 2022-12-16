package com.stisbolivia.acloapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TareaAclo {
    //Atributos
    private int id;
    private String nombre;
    private String descripcion;
    private String fecha;
    private String estado;

    //Metodos
    public TareaAclo(){
        this.id = 0;
        this.nombre = "";
        this.descripcion = "";
        this.fecha = "";
        this.estado = "";
    }

    public TareaAclo(int id,String nombre,String descripcion,String fecha){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estado = "ACTIVO";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getHora(){
        String[] hora = this.fecha.split(" ")[1].split(":");
        return hora[0]+":"+hora[1];
    }

    public String getFechaDias() {
        String[] fecha = this.fecha.split(" ")[0].split("-");
        return fecha[2]+"/"+fecha[1]+"/"+fecha[0];
    }

    @Override
    public String toString() {
        return "TareaAclo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}

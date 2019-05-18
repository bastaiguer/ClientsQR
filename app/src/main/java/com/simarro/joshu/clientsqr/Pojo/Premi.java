package com.simarro.joshu.clientsqr.Pojo;

public class Premi {

    private int id, puntos;
    private String titulo, imagen, descripcion;

    public Premi() {
    }

    public Premi(int id, int puntos, String titulo, String imagen, String descripcion) {
        this.id = id;
        this.puntos = puntos;
        this.titulo = titulo;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

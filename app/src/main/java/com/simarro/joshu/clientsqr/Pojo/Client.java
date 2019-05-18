package com.simarro.joshu.clientsqr.Pojo;

import java.io.Serializable;
import java.util.Date;

public class Client implements Serializable {

    private int id, punts;
    private String nombre;
    private String mote;
    private String telefono;
    private int tienda;
    private String imagen = "";

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getTienda() {
        return tienda;
    }

    public void setTienda(int tienda) {
        this.tienda = tienda;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    private String pass;
    private Date registro;
    private Double latitud, longitud;

    public Client(){
        super();
    }

    public Client(int id, int punts, String nombre, String mote, String telefono, int tienda, String pass, Date registro, Double latitud, Double longitud) {
        this.id = id;
        this.punts = punts;
        this.nombre = nombre;
        this.mote = mote;
        this.telefono = telefono;
        this.tienda = tienda;
        this.pass = pass;
        this.registro = registro;
        this.latitud = latitud;
        this.longitud = longitud;
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

    public String getMote() {
        return mote;
    }

    public void setMote(String mote) {
        this.mote = mote;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getPunts() {
        return punts;
    }

    public void setPunts(int punts) {
        this.punts = punts;
    }

    public Date getRegistro() {
        return registro;
    }

    public void setRegistro(Date registro) {
        this.registro = registro;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}

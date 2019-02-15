package com.simarro.joshu.clientsqr.Pojo;

import java.io.Serializable;
import java.util.Date;

public class Client implements Serializable {

    private int id, punts;
    private String nombre, mote, telefono;
    private Date registro;

    public Client(){
        super();
    }

    public Client(String nombre, String mote, String telefono,Date registro) {
        this.nombre = nombre;
        this.mote = mote;
        this.telefono = telefono;
        this.punts = 0;
        this.registro = registro;
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
}

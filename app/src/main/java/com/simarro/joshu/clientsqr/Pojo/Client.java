package com.simarro.joshu.clientsqr.Pojo;

public class Client {

    private int id, punts;
    private String nombre, mote, telefono;

    public Client(){

    }

    public Client(String nombre, String mote, String telefono) {
        this.nombre = nombre;
        this.mote = mote;
        this.telefono = telefono;
        this.punts = 0;
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
}

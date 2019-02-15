package com.simarro.joshu.clientsqr.Pojo;

import java.io.Serializable;
import java.util.Date;

public class Punts implements Serializable {

    private int id, idclient, punts;
    private boolean operacio;
    private Date registro;
    private double latitud, longitud;

    public Punts() {
    }

    public Punts(int idclient, int punts, boolean operacio, Date registro, double latitud, double longitud) {
        this.idclient = idclient;
        this.punts = punts;
        this.operacio = operacio;
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

    public int getIdclient() {
        return idclient;
    }

    public void setIdclient(int idclient) {
        this.idclient = idclient;
    }

    public int getPunts() {
        return punts;
    }

    public void setPunts(int punts) {
        this.punts = punts;
    }

    public boolean isOperacio() {
        return operacio;
    }

    public void setOperacio(boolean operacio) {
        this.operacio = operacio;
    }

    public Date getRegistro() {
        return registro;
    }

    public void setRegistro(Date registro) {
        this.registro = registro;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}

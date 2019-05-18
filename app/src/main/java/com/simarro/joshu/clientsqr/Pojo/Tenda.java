package com.simarro.joshu.clientsqr.Pojo;

import java.io.Serializable;

public class Tenda implements Serializable {

    private int id;
    private String nombre, empresa, pass;

    public Tenda() {
    }

    public Tenda(int id, String nombre, String empresa, String pass) {
        this.id = id;
        this.nombre = nombre;
        this.empresa = empresa;
        this.pass = pass;
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

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

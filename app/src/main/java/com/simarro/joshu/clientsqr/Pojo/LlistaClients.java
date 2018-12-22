package com.simarro.joshu.clientsqr.Pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class LlistaClients extends ArrayList<Client> implements Serializable {

    public void addClient(Client cl){
        cl.setId(this.size()+1);
        this.add(cl);
    }

    public void addLlista(ArrayList<Client> llista){
        for(Client cl : llista){
            this.add(cl);
        }
    }

    public void mostrarNoms(){
        for(Client cl:this){
            System.out.println("------------------------------");
            System.out.println(cl.getNombre());
            System.out.println("------------------------------");
        }
    }

    public ArrayList<Client> getClients(){
        return this;
    }

    public Client getClientById(int id) {
        Client c = null;
        for (Client cl : this) {
            if (cl.getId() == id) {
                c = cl;
            }
        }
        return c;
    }

    public ArrayList<Client> getClientsOver(int over){
        ArrayList<Client> c = new ArrayList<>();
        for(Client cl: this){
            if(cl.getPunts() >= over){
                c.add(cl);
            }
        }
        return c;
    }

    public ArrayList<Client> getClientsByName(String name){
        ArrayList<Client> c = new ArrayList<>();
        for (Client cl : this) {
            if (cl.getNombre().equals(name)) {
                c.add(cl);
            }
        }
        return c;
    }

    public Client getClientByTlf(String tlf){
        Client c = null;
        for (Client cl : this) {
            if (cl.getTelefono().equals(tlf)) {
                c = cl;
            }
        }
        return c;
    }

    public void orderByName() {
        char first;
        Client cl;
        for (int j = 0; j < this.size(); j++) {
            for (int i = 1; i < this.size(); i++) {
                first = this.get(i - 1).getNombre().charAt(0);
                if (first > this.get(i).getNombre().charAt(0)) {
                    cl = this.get(i - 1);
                    this.set(i - 1, this.get(i));
                    this.set(i, cl);
                }
            }
        }
    }

}

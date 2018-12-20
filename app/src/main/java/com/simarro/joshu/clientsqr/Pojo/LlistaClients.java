package com.simarro.joshu.clientsqr.Pojo;

import java.util.ArrayList;

public class LlistaClients extends ArrayList<Client> {

    public Client getClient(int id) {
        Client c = null;
        for (Client cl : this) {
            if (cl.getId() == id) {
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

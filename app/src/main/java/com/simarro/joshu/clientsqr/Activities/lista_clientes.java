package com.simarro.joshu.clientsqr.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.LlistaClients;
import com.simarro.joshu.clientsqr.R;

import java.util.ArrayList;

public class lista_clientes extends AppCompatActivity {

    private LlistaClients clients = new LlistaClients();
    private ListView llista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        llista = findViewById(R.id.listView_clients);
        //Accedemos a la BBDD mediante un Thread
        BD bd = new BD(getApplicationContext()){
            public void run() {
                try {
                    synchronized (this) {
                        wait(500);
                        conectarBDMySQL();
                        getClientes();
                        cerrarConexion();
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    Log.e("Error", "Waiting didnt work!!");
                    e.printStackTrace();
                }
            }
        };
        Thread th = new Thread(bd) ;
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Obtenemos la lista de los clientes en la BBDD
        this.clients.addLlista(bd.obClients());
        llista.setAdapter(new adapterListClients(clients.getClients(),this.getApplicationContext()));
    }
}

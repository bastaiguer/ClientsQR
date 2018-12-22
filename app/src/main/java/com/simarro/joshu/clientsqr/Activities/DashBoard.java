package com.simarro.joshu.clientsqr.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.LlistaClients;
import com.simarro.joshu.clientsqr.R;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {

    private LlistaClients clients = new LlistaClients();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
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
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_add_user:
                intent = new Intent();
                break;
            case R.id.btn_clientes:
                intent = new Intent(this,lista_clientes.class);
                intent.putExtra("clients",clients.getClients());
                break;
            case R.id.btn_leer_qr:
                intent = new Intent(this,lector_qr.class);
                break;
            default:
                intent = new Intent();
                break;

        }
        startActivity(intent);
    }
}

package com.simarro.joshu.clientsqr.Activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.Premi;
import com.simarro.joshu.clientsqr.R;
import com.simarro.joshu.clientsqr.Resources.Adapters.adapterListPremis;
import com.simarro.joshu.clientsqr.Resources.Dialogs.DialogoCanjeoCupon;

import java.util.ArrayList;

public class Cupones extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView cupones;
    private adapterListPremis adapter;
    private ArrayList<Premi> premis;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupones);

        this.cupones = findViewById(R.id.lista_cupones);
        client = (Client) getIntent().getExtras().getSerializable("client");
        BD bd = new BD(getApplicationContext()) {
            public void run() {
                try {
                    synchronized (this) {
                        wait(500);
                        conectarBDMySQL();
                        getCupones(client.getId());
                        cerrarConexion();
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    Log.e("Error", "Waiting didnt work!!");
                    e.printStackTrace();
                }
            }
        };
        Thread th = new Thread(bd);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.premis = bd.obPremis();
        this.adapter = new adapterListPremis(premis,this);
        this.cupones.setAdapter(this.adapter);
        this.cupones.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DialogoCanjeoCupon dialeg = DialogoCanjeoCupon.newInstance(this.premis.get(position),this.client.getId());
        dialeg.show(getSupportFragmentManager(),"dialeg");
    }
}

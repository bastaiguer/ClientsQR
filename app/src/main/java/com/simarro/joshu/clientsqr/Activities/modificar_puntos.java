package com.simarro.joshu.clientsqr.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.LlistaClients;
import com.simarro.joshu.clientsqr.R;

public class modificar_puntos extends AppCompatActivity implements View.OnClickListener{

    private LlistaClients clients = new LlistaClients();
    private String qr;
    private TextView numPunts;
    private EditText anyPunts;
    private int mostrarpunts, addPunts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_puntos);
        qr = getIntent().getExtras().getString("qr");
        numPunts = findViewById(R.id.txt_num_punts);
        anyPunts = findViewById(R.id.ed_punts);
        numPunts.setText(qr);
    }

    @Override
    public void onClick(View v) {
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
        mostrarpunts = this.clients.getClientById(Integer.parseInt(qr)).getPunts();
        switch(v.getId()){
            case R.id.btn_sumar_punto:
                bd = new BD(getApplicationContext()){
                    public void run() {
                        try {
                            synchronized (this) {
                                wait(500);
                                conectarBDMySQL();
                                addPunts = mostrarpunts + Integer.parseInt(anyPunts.getText().toString());
                                modPuntos(addPunts,Integer.parseInt(qr));
                                cerrarConexion();
                            }
                        } catch (InterruptedException e) {
                            Log.e("Error", "Waiting didnt work!!");
                            e.printStackTrace();
                        }
                    }
                };
                th = new Thread(bd) ;
                th.start();
                try {
                    th.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this,"El cliente Nº"+qr+" tiene "+addPunts+" puntos!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_canjear_por_cupon:
                if(mostrarpunts >= 100) {
                    bd = new BD(getApplicationContext()) {
                        public void run() {
                            try {
                                synchronized (this) {
                                    wait(500);
                                    conectarBDMySQL();
                                    addPunts = mostrarpunts - 100;
                                    modPuntos(addPunts, Integer.parseInt(qr));
                                    cerrarConexion();
                                }
                            } catch (InterruptedException e) {
                                Log.e("Error", "Waiting didnt work!!");
                                e.printStackTrace();
                            }
                        }
                    };
                    th = new Thread(bd);
                    th.start();
                    try {
                        th.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this, "El cliente Nº" + qr + " tiene " + addPunts + " puntos!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"No tiene suficientes puntos",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this,DashBoard.class);
        startActivity(intent);
    }
}

package com.simarro.joshu.clientsqr.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.LlistaClients;
import com.simarro.joshu.clientsqr.Pojo.Tenda;
import com.simarro.joshu.clientsqr.R;
import com.simarro.joshu.clientsqr.Resources.Dialogs.DialogoLogin;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences preferences;
    private Tenda tenda;
    private Client client;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
    }


    @Override
    public void onClick(View v) {
        if(preferences.getBoolean("login",false)){
            Intent intent;
            int tipo = preferences.getInt("tipo",0);
            num = preferences.getInt("user",0);
            if(tipo == 0){
                intent = new Intent(this,DashBoard.class);
                BD bd = new BD(getApplicationContext()) {
                    public void run() {
                        try {
                            synchronized (this) {
                                wait(500);
                                conectarBDMySQL();
                                getTenda(num);
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
                this.tenda = bd.getTendaOb();
                intent.putExtra("tenda",this.tenda);
            }else{
                intent = new Intent(this,MiPerfil.class);
                BD bd = new BD(getApplicationContext()) {
                    public void run() {
                        try {
                            synchronized (this) {
                                wait(500);
                                conectarBDMySQL();
                                getClient(num);
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
                this.client = bd.getClientOb();
                intent.putExtra("client",this.client);
                intent.putExtra("tipo",false);
            }
            startActivity(intent);
        }else {
            DialogoLogin dialeg = DialogoLogin.newInstance("");
            dialeg.show(getSupportFragmentManager(), "DialegLogin");
        }
    }
}

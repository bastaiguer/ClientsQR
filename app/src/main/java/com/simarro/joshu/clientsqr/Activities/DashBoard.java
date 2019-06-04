package com.simarro.joshu.clientsqr.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Tenda;
import com.simarro.joshu.clientsqr.R;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Tenda tenda;
    private Button btn_cerrar_sesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        // Fons imatge
        findViewById(R.id.btn_add_user).setBackgroundResource(R.drawable.selector_menu);
        btn_cerrar_sesion = findViewById(R.id.btn_cerrar_sesion);
        btn_cerrar_sesion.setOnClickListener(this);
        tenda = (Tenda) getIntent().getExtras().getSerializable("tenda");
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            BD bd = new BD(getApplicationContext()){
                public void run() {
                    try {
                        synchronized (this) {
                            wait(500);
                            conectarBDMySQL();
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        Log.e("Error", "Waiting didnt work!!");
                        e.printStackTrace();
                    }
                }
            };
            final Thread th = new Thread(bd) ;
            th.start();
            Thread thcount = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        if(th.isAlive()){
                            th.stop();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thcount.start();
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(bd.getConexionMySQL()==null){
                Toast.makeText(this,"Base de Datos no disponible",Toast.LENGTH_SHORT).show();
                findViewById(R.id.btn_add_user).setEnabled(false);
                findViewById(R.id.btn_add_user).setAlpha(0.6f);
                findViewById(R.id.btn_clientes).setEnabled(false);
                findViewById(R.id.btn_clientes).setAlpha(0.6f);
                findViewById(R.id.btn_leer_qr).setEnabled(false);
                findViewById(R.id.btn_leer_qr).setAlpha(0.6f);
            }else{
                bd.cerrarConexion();
            }
        }else{
            findViewById(R.id.btn_add_user).setEnabled(false);
            findViewById(R.id.btn_add_user).setAlpha(0.6f);
            findViewById(R.id.btn_clientes).setEnabled(false);
            findViewById(R.id.btn_clientes).setAlpha(0.6f);
            findViewById(R.id.btn_leer_qr).setEnabled(false);
            findViewById(R.id.btn_leer_qr).setAlpha(0.6f);
            Toast.makeText(this,"No hay conexión a internet",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_add_user:
                intent = new Intent(this,lector_qr.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("tenda",tenda);
                intent.putExtra("opcion",1);
                startActivity(intent);
                break;
            case R.id.btn_clientes:
                intent = new Intent(this, ListaClientes.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("tenda",tenda);
                startActivity(intent);
                break;
            case R.id.btn_leer_qr:
                intent = new Intent(this,lector_qr.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("tenda",tenda);
                intent.putExtra("opcion",0);
                startActivity(intent);
                break;
            case R.id.btn_cerrar_sesion:
                intent = new Intent(this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SharedPreferences prefs = getSharedPreferences("preferencias",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("login",false);
                editor.commit();
                startActivity(intent);
                break;

        }
    }

}

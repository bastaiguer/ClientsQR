package com.simarro.joshu.clientsqr.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.LlistaClients;
import com.simarro.joshu.clientsqr.R;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
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
                intent = new Intent(this,lector_qr.class);
                intent.putExtra("opcion",1);
                break;
            case R.id.btn_clientes:
                intent = new Intent(this,lista_clientes.class);
                break;
            case R.id.btn_leer_qr:
                intent = new Intent(this,lector_qr.class);
                intent.putExtra("opcion",0);
                break;
            default:
                intent = new Intent();
                break;

        }
        startActivity(intent);
    }
}

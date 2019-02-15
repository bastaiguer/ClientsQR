package com.simarro.joshu.clientsqr.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.R;

import java.util.Calendar;
import java.util.Date;

public class add_client extends AppCompatActivity implements View.OnClickListener{

    private String txt_nom, txt_mote, txt_tlf;
    private EditText nom, mote, tlf;
    private Button registrar;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        nom = findViewById(R.id.ed_nom_add);
        id = Integer.parseInt(getIntent().getExtras().getString("qr"));
        mote = findViewById(R.id.ed_mote_add);
        tlf = findViewById(R.id.ed_tlf_add);
        registrar = findViewById(R.id.btn_registrar);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == registrar.getId()){
            txt_nom = nom.getText().toString();
            txt_mote = mote.getText().toString();
            txt_tlf = tlf.getText().toString();
            //Accedemos a la BBDD mediante un Thread
            BD bd = new BD(getApplicationContext()){
                public void run() {
                    try {
                        synchronized (this) {
                            wait(500);
                            conectarBDMySQL();
                            addCliente(id,txt_nom,txt_mote,txt_tlf);
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
            if(bd.getResult() == 0){
                Toast.makeText(this, "El cliente " + id + " con nombre " + txt_nom + " ha sido registrado correctamente", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "El cliente " + id + " ya está registrado", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this,DashBoard.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this,DashBoard.class);
        startActivity(intent);
    }
}

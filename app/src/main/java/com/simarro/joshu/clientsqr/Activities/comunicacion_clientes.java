package com.simarro.joshu.clientsqr.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.R;

public class comunicacion_clientes extends AppCompatActivity implements View.OnClickListener{

    private Button btn_whats,btn_llamada;
    private EditText ed_whats_msg;
    private Spinner telefonos;
    private SpinnerAdapter spinnerAdapter;
    private String[] tlfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicacion_clientes);
        btn_whats = findViewById(R.id.btn_whats_msg);
        btn_whats.setOnClickListener(this);
        ed_whats_msg = findViewById(R.id.edit_whats_msg);
        btn_llamada = findViewById(R.id.btn_llamar);
        btn_llamada.setOnClickListener(this);
        telefonos = findViewById(R.id.spinner_tlf);
        tlfs = new String[]{"Selecciona un teléfono","1","2"};
        spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,tlfs);
        telefonos.setAdapter(spinnerAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_whats_msg){
                if(ed_whats_msg.getText().toString().equals("")) {
                    Toast.makeText(this, "Escribe algún mensaje", Toast.LENGTH_SHORT).show();
                }else {
                    //Enviar missatge de difusió whatsapp
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, ed_whats_msg.getText().toString());
                    try {
                        startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "Whatsapp no está instalado...", Toast.LENGTH_SHORT).show();
                    }
                }
        }else{

        }
    }
}

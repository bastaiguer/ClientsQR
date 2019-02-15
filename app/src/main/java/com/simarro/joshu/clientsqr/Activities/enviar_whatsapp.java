package com.simarro.joshu.clientsqr.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.R;

public class enviar_whatsapp extends AppCompatActivity implements View.OnClickListener{

    private Button btn_whats;
    private EditText ed_whats_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_whatsapp);
        btn_whats = findViewById(R.id.btn_whats_msg);
        btn_whats.setOnClickListener(this);
        ed_whats_msg = findViewById(R.id.edit_whats_msg);
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

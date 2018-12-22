package com.simarro.joshu.clientsqr.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.LlistaClients;
import com.simarro.joshu.clientsqr.R;

import java.util.ArrayList;

public class lista_clientes extends AppCompatActivity {

    private LlistaClients llistaClients = new LlistaClients();
    private ListView llista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        llista = findViewById(R.id.listView_clients);
        llistaClients.addLlista((ArrayList<Client>) getIntent().getExtras().getSerializable("clients"));
        llistaClients.mostrarNoms();
        llista.setAdapter(new adapterListClients(llistaClients.getClients(),this.getApplicationContext()));
    }
}

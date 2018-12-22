package com.simarro.joshu.clientsqr.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
        clients.addLlista((ArrayList<Client>) getIntent().getExtras().getSerializable("clients"));
        this.clients.mostrarNoms();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_add_user:
                intent = new Intent();
                break;
            case R.id.btn_clientes:
                intent = new Intent();
                break;
            case R.id.btn_leer_qr:
                intent = new Intent();
                break;
            default:
                intent = new Intent();
                break;

        }
        startActivity(intent);
    }
}

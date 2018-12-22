package com.simarro.joshu.clientsqr.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.LlistaClients;
import com.simarro.joshu.clientsqr.R;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity {

    private LlistaClients clients = new LlistaClients();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        clients.addLlista((ArrayList<Client>) getIntent().getExtras().getSerializable("clients"));
    }
}

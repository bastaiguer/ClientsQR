package com.simarro.joshu.clientsqr.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.Premi;
import com.simarro.joshu.clientsqr.Pojo.Tenda;
import com.simarro.joshu.clientsqr.R;
import com.simarro.joshu.clientsqr.Resources.Adapters.adapterListPremis;
import com.simarro.joshu.clientsqr.Resources.Dialogs.DialogoInfo;

import java.util.ArrayList;

public class Premios extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listaPremios;
    private adapterListPremis adapter;
    private ArrayList<Premi> premis;
    private Button btnAddPremi;
    private BD bd;
    private int numTenda;
    private Tenda tenda;
    private Client client;
    private Premi premi;
    private int addPunts;
    private LocationManager locationManager;
    private static Location location;
    private LocationListener locationListener;
    private double latitud, longitud;
    private boolean changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premios);
        this.listaPremios = findViewById(R.id.lista_premios);
        this.btnAddPremi = findViewById(R.id.btn_add_premio);
        if(getIntent().getExtras().getBoolean("tipo",true)){
            this.tenda = (Tenda) getIntent().getExtras().getSerializable("tenda");
            this.numTenda = this.tenda.getId();
            this.btnAddPremi.setOnClickListener(this);
        }else{
            this.client = (Client) getIntent().getExtras().getSerializable("client");
            this.numTenda = this.client.getTienda();
            this.btnAddPremi.setVisibility(View.INVISIBLE);
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            123);
                } else {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            longitud = location.getLongitude();
                            latitud = location.getLatitude();
                            if(!changed) {
                                listaPremios.setOnItemClickListener(Premios.this);
                                changed = true;
                            }
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            longitud = location.getLongitude();
                            latitud = location.getLatitude();
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            longitud = location.getLongitude();
                            latitud = location.getLatitude();
                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    };
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, locationListener);
                }

        }


        bd = new BD(getApplicationContext()) {
            public void run() {
                try {
                    synchronized (this) {
                        wait(500);
                        conectarBDMySQL();
                        getPremios(numTenda);
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
        this.premis = bd.obPremis();
        this.adapter = new adapterListPremis(premis,this);
        this.listaPremios.setAdapter(this.adapter);
        this.listaPremios.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.premi = this.premis.get(position);
        if(!getIntent().getExtras().getBoolean("tipo",true)) {
            if (longitud == 0 && latitud == 0) {
                Toast.makeText(this, "Obteniendo geolocalización", Toast.LENGTH_SHORT).show();
            } else {
                if (this.client.getPunts() >= premi.getPuntos()) {
                    this.addPunts = this.client.getPunts() - premi.getPuntos();
                    bd = new BD(getApplicationContext()) {
                        public void run() {
                            try {
                                synchronized (this) {
                                    wait(500);
                                    conectarBDMySQL();
                                    addRegistrePunts(client.getId(), false, premi.getPuntos(), longitud, latitud);
                                    canjearPremio(premi.getId(), client.getId());
                                    modPuntos(addPunts, client.getId());
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
                    DialogoInfo dialogo = DialogoInfo.newInstance("Premio canjeado! Tienes " + this.addPunts + " puntos");
                    dialogo.show(getSupportFragmentManager(), "DialegInfo");
                } else {
                    DialogoInfo dialogo2 = DialogoInfo.newInstance("No tienes suficientes puntos...");
                    dialogo2.show(getSupportFragmentManager(), "DialegInfo");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.btn_add_premio:
                intent = new Intent(this, AddPremio.class);
                intent.putExtra("tenda",this.tenda);
                startActivity(intent);
                break;
        }
    }
}

package com.simarro.joshu.clientsqr.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
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
import java.util.List;

public class add_client extends AppCompatActivity implements View.OnClickListener {

    private String txt_nom, txt_mote, txt_tlf;
    private EditText nom, mote, tlf;
    private Button registrar;
    private int id;
    private LocationManager locationManager;
    private static Location location;
    private LocationListener locationListener;
    private double longitud, latitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        nom = findViewById(R.id.ed_nom_add);
        id = Integer.parseInt(getIntent().getExtras().getString("qr"));
        mote = findViewById(R.id.ed_mote_add);
        tlf = findViewById(R.id.ed_tlf_add);
        registrar = findViewById(R.id.btn_registrar);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION },
                        123);

                Toast.makeText(this,"Acceso a la ubicación no permitido...",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,latitud+" - "+longitud,Toast.LENGTH_SHORT).show();
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        longitud = location.getLongitude();
                        latitud = location.getLatitude();
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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            }
        }else{
            Toast.makeText(this,"Deshabilitado Network Ubication Service",Toast.LENGTH_SHORT).show();

        }
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
                            addCliente(id,txt_nom,txt_mote,txt_tlf, latitud,longitud);
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

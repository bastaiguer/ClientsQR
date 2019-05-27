package com.simarro.joshu.clientsqr.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.Tenda;
import com.simarro.joshu.clientsqr.R;
import com.simarro.joshu.clientsqr.Resources.Dialogs.DialogoLogin;

import java.io.IOException;

public class lector_qr extends AppCompatActivity {

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private String token = "-1";
    private String tokenanterior = "";
    private int op = 3;
    private boolean comp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_qr);
        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector).setRequestedPreviewSize(800, 600).setAutoFocusEnabled(true).build();
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        op = getIntent().getExtras().getInt("opcion");
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(
                                    Manifest.permission.CAMERA)) ;
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                            return;
                        }
                        return;
                    } else {
                        cameraSource.start(holder);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                lector_qr.this.mostrarQR();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    token = barcodes.valueAt(0).displayValue.toString();
                    // verificamos que el token anterior no se igual al actual
                    // esto es util para evitar multiples llamadas empleando el mismo token
                    if (!token.equals(tokenanterior)) {
                        // guardamos el ultimo token procesado
                        tokenanterior = token;
                        Log.i("token", token);
                        /*if (URLUtil.isValidUrl(token)) {
                            // si es una URL valida abre el navegador
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(token));
                            startActivity(browserIntent);
                        } else {
                            // comparte en otras apps
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, token);
                            shareIntent.setType("text/plain");
                            startActivity(shareIntent);
                        }*/
                        Intent intent = new Intent();
                        Toast.makeText(getApplicationContext(),token,Toast.LENGTH_SHORT).show();
                        Tenda tenda2;
                        if (op == 0) {
                            intent.setClass(lector_qr.this.getApplicationContext(), modificar_puntos.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("qr", token);
                            tenda2 = (Tenda) getIntent().getExtras().getSerializable("tenda");
                            intent.putExtra("tenda",tenda2);
                            startActivity(intent);
                        } else if (op == 1) {
                            intent.setClass(lector_qr.this.getApplicationContext(), add_client.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("qr", token);
                            tenda2 = (Tenda) getIntent().getExtras().getSerializable("tenda");
                            intent.putExtra("tenda",tenda2);
                            startActivity(intent);
                        } else {
                            if(comprobarTipo(token)){
                                token = getCodigoTienda(token);
                                BD bd = new BD(getApplicationContext()) {
                                    public void run() {
                                        try {
                                            synchronized (this) {
                                                wait(500);
                                                conectarBDMySQL();
                                                getTenda(Integer.parseInt(token));
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
                                Tenda tenda = bd.getTendaOb();
                                if (!tenda.getNombre().equals("..truco..")) {
                                    DialogoLogin dialeg = DialogoLogin.newInstance(token, tenda,true);
                                    dialeg.show(getSupportFragmentManager(), "dialeg");
                                } else {
                                    Toast.makeText(getApplicationContext(), "Tienda no registrada", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                BD bd = new BD(getApplicationContext()) {
                                    public void run() {
                                        try {
                                            synchronized (this) {
                                                wait(500);
                                                conectarBDMySQL();
                                                getClient(Integer.parseInt(token));
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
                                Client client = bd.getClientOb();
                                if (!client.getNombre().equals("..truco..")) {
                                    if (client.getPass().equals("")) {
                                        Toast.makeText(getApplicationContext(), "Introduce la contraseña que utilizarás", Toast.LENGTH_SHORT).show();
                                    }
                                    DialogoLogin dialeg = DialogoLogin.newInstance(token, client,false);
                                    dialeg.show(getSupportFragmentManager(), "dialeg");
                                } else {
                                    Toast.makeText(getApplicationContext(), "Usuario no registrado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (this) {
                                        wait(5000);
                                        // limpiamos el token
                                        tokenanterior = "";
                                    }
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Waiting didnt work!!");
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                }
            }
        });
    }

    public boolean comprobarTipo(String qr){
        if(qr.substring(0,3).equals("TND")){
            return true;
        }else {
            return false;
        }
    }

    public String getCodigoTienda(String qr){
        String res = "";
        for(int i = 3; i < qr.length(); i++){
            res += qr.charAt(i);
        }
        return res;
    }

    public void mostrarQR() {
        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
    }
}

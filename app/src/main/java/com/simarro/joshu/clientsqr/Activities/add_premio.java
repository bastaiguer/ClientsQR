package com.simarro.joshu.clientsqr.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Premi;
import com.simarro.joshu.clientsqr.Pojo.Tenda;
import com.simarro.joshu.clientsqr.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class add_premio extends AppCompatActivity implements View.OnClickListener {

    private EditText titulo, descripcion, puntos;
    private Button btnAdd;
    private ImageView btnImg;
    private BD bd;
    private int punts;
    private String tit, desc, img;
    private Tenda tenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_premio);
        this.titulo = findViewById(R.id.ed_premio_titulo);
        this.descripcion = findViewById(R.id.ed_premio_desc);
        this.puntos = findViewById(R.id.ed_premio_puntos);
        this.btnAdd = findViewById(R.id.btn_premio_add);
        this.btnImg = findViewById(R.id.btn_premio_imagen);
        tenda = (Tenda) getIntent().getExtras().getSerializable("tenda");
        this.btnAdd.setOnClickListener(this);
        this.btnImg.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        final Premi premi = new Premi();
        switch (v.getId()) {
            case R.id.btn_premio_add:
                intent = new Intent(this, lista_clientes.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (this.titulo.getText().toString().equals("") || this.descripcion.getText().toString().equals("") || this.puntos.getText().toString().equals("")) {
                    Toast.makeText(this, "Hay campos vacíos", Toast.LENGTH_SHORT).show();
                } else {
                    this.punts = Integer.parseInt(this.puntos.getText().toString());
                    this.tit = this.titulo.getText().toString();
                    this.desc = this.descripcion.getText().toString();
                    premi.setTitulo(this.tit);
                    premi.setDescripcion(this.desc);
                    premi.setPuntos(this.punts);
                    premi.setImagen(this.img);

                    BD bd = new BD(getApplicationContext()) {
                        public void run() {
                            try {
                                synchronized (this) {
                                    wait(500);
                                    conectarBDMySQL();
                                    addPremio(premi,tenda.getId());
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
                }
                intent.putExtra("tenda",this.tenda);
                startActivity(intent);
                break;
            case R.id.btn_premio_imagen:
                this.abrirGaleria();
                break;
        }
    }

    public void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Selecciona una imagen"),
                1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImage;
        int primer, segon;
        boolean pos;
        double proporcion;
        if (resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            String selectedPath = selectedImage.getPath();
            if (requestCode == 1) {
                if (selectedPath != null) {
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(
                                selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                    this.btnImg.setImageBitmap(bmp);
                    this.btnImg.setBackground(null);
                    if (bmp.getWidth() > bmp.getHeight()) {
                        primer = bmp.getWidth();
                        segon = bmp.getHeight();
                        pos = true;
                    } else {
                        primer = bmp.getHeight();
                        segon = bmp.getWidth();
                        pos = false;
                    }
                    proporcion = (double) (primer / segon);
                    if (pos) {
                        bmp = Bitmap.createScaledBitmap(bmp,400,(int)(400*proporcion),false);
                    } else {
                        bmp = Bitmap.createScaledBitmap(bmp,(int)(400*proporcion),400,false);
                    }
                    this.img = bitmapToBase64(bmp);
                }
            }
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}

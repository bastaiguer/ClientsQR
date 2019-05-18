package com.simarro.joshu.clientsqr.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.Punts;
import com.simarro.joshu.clientsqr.R;
import com.simarro.joshu.clientsqr.Resources.Dialogs.DialogoCambioPass;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MiPerfil extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private CircularImageView circularImageView;
    private Client client = new Client();
    private String img;
    private boolean mostrarImg = true;
    private ImageView imatgePerfil;
    private Button btnCerrarSesion, btnPass, btnPremios, btnCupones;
    private TextView txtPuntos, txtCanjeos, txtNombre;
    private EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);
        this.btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion_perfil);
        this.btnCerrarSesion.setOnClickListener(this);
        this.btnCupones = findViewById(R.id.btn_cupones_perfil);
        this.btnCupones.setOnClickListener(this);
        this.btnPass = findViewById(R.id.btn_cambiar_pass);
        this.btnPass.setOnClickListener(this);
        this.btnPremios = findViewById(R.id.btn_premios_perfil);
        this.btnPremios.setOnClickListener(this);
        this.txtCanjeos = findViewById(R.id.txt_num_canjeados);
        this.txtPuntos = findViewById(R.id.txt_puntos_perfil);
        this.txtNombre = findViewById(R.id.txt_nom_perfil);
        this.circularImageView = findViewById(R.id.imagen_perfil);
        this.imatgePerfil = findViewById(R.id.imagen_perfil_cuadrada);
        this.imatgePerfil.setVisibility(View.INVISIBLE);
        client = (Client) getIntent().getExtras().getSerializable("client");
        this.circularImageView.setOnClickListener(this);
        this.circularImageView.setOnLongClickListener(this);
        this.imatgePerfil.setOnClickListener(this);
        this.imatgePerfil.setOnLongClickListener(this);
        if (client.getImagen().equals("")) {
            this.circularImageView.setImageResource(R.drawable.user);
            this.imatgePerfil.setImageResource(R.drawable.user);
        } else {
            this.circularImageView.setImageBitmap(base64ToBitmap(client.getImagen()));
            this.imatgePerfil.setImageBitmap(base64ToBitmap(client.getImagen()));
        }
        this.txtNombre.setText(this.client.getNombre());
        String punts = this.client.getPunts()+" Puntos";
        this.txtPuntos.setText(punts);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imagen_perfil || v.getId() == R.id.imagen_perfil_cuadrada) {
            if (this.mostrarImg) {
                this.imatgePerfil.setVisibility(View.VISIBLE);
                this.circularImageView.setVisibility(View.INVISIBLE);
                this.mostrarImg = false;
            } else {
                this.imatgePerfil.setVisibility(View.INVISIBLE);
                this.circularImageView.setVisibility(View.VISIBLE);
                this.mostrarImg = true;
            }
        }else{
            Intent intent;
            switch(v.getId()){
                case R.id.btn_cerrar_sesion_perfil:
                    intent = new Intent(this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SharedPreferences prefs = getSharedPreferences("preferencias",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("login",false);
                    editor.commit();
                    startActivity(intent);
                    break;
                case R.id.btn_cambiar_pass: //Mostrar dialogo cambio contraseña
                    DialogoCambioPass dialeg = DialogoCambioPass.newInstance(this.client);
                    dialeg.show(getSupportFragmentManager(),"DialogoCambioPass");
                    break;
                case R.id.btn_premios_perfil:
                    intent = new Intent(this,premios.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("client",this.client);
                    intent.putExtra("tipo",false);
                    startActivity(intent);
                    break;
                case R.id.btn_cupones_perfil: //Intent to CuponesCliente

                    break;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.imagen_perfil || v.getId() == R.id.imagen_perfil_cuadrada) {
            this.abrirGaleria();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

                    this.circularImageView.setImageBitmap(bmp);
                    this.imatgePerfil.setImageBitmap(bmp);
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
                        bmp = Bitmap.createScaledBitmap(bmp, 400, (int) (400 * proporcion), false);
                    } else {
                        bmp = Bitmap.createScaledBitmap(bmp, (int) (400 * proporcion), 400, false);
                    }
                    this.img = bitmapToBase64(bmp);
                    BD bd = new BD(getApplicationContext()) {
                        public void run() {
                            try {
                                synchronized (this) {
                                    wait(500);
                                    conectarBDMySQL();
                                    modImagenClient(client.getId(), img);
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
            }
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

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.bus.unregister(this);
    }

    @Subscribe
    public void ejecutarLlamada(String pass){
        this.client.setPass(pass);
    }
}

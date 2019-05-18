package com.simarro.joshu.clientsqr.Resources.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.Activities.DashBoard;
import com.simarro.joshu.clientsqr.Activities.MiPerfil;
import com.simarro.joshu.clientsqr.Activities.lector_qr;
import com.simarro.joshu.clientsqr.Activities.premios;
import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.Premi;
import com.simarro.joshu.clientsqr.Pojo.Tenda;
import com.simarro.joshu.clientsqr.R;

public class DialogoLogin extends DialogFragment implements View.OnClickListener {

    private EditText user, pass;
    private Button login, mostrar;
    private boolean mostrando = false;
    private String numUser = "";
    private String txtPass;
    private Drawable drawable;
    private Client c;
    private Tenda t;
    private SharedPreferences preferences;

    public DialogoLogin() {

    }

    public static DialogoLogin newInstance(String user, Client c, boolean tipo) {
        DialogoLogin dialogo = new DialogoLogin();
        Bundle args = new Bundle();
        args.putString("user", user);
        args.putSerializable("usuari", c);
        args.putBoolean("tipo", tipo);
        dialogo.setArguments(args);
        return dialogo;
    }

    public static DialogoLogin newInstance(String user) {
        DialogoLogin dialogo = new DialogoLogin();
        Bundle args = new Bundle();
        args.putString("user", user);
        dialogo.setArguments(args);
        return dialogo;
    }

    public static DialogoLogin newInstance(String user, Tenda t, boolean tipo) {
        DialogoLogin dialogo = new DialogoLogin();
        Bundle args = new Bundle();
        args.putString("user", user);
        args.putSerializable("usuari", t);
        args.putBoolean("tipo", tipo);
        dialogo.setArguments(args);
        return dialogo;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_login, null);
        builder.setView(view);
        this.numUser = getArguments().getString("user", "");
        this.user = view.findViewById(R.id.ed_user_login);
        this.pass = view.findViewById(R.id.ed_pass_login);
        this.login = view.findViewById(R.id.btn_login);
        this.login.setOnClickListener(this);
        this.mostrar = view.findViewById(R.id.btn_cambiar_tipo);
        this.mostrar.setOnClickListener(this);
        if (!numUser.equals("")) {
            if (!getArguments().getBoolean("tipo", false)) {
                this.c = (Client) getArguments().getSerializable("usuari");
            } else {
                this.t = (Tenda) getArguments().getSerializable("usuari");
            }
            this.user.setText(numUser);
            drawable = getResources().getDrawable(R.drawable.ed_login);
            this.user.setBackground(drawable);
            this.user.setEnabled(false);
        } else {
            this.user.setOnClickListener(this);
        }
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Drawable verContra;
        switch (v.getId()) {
            case R.id.ed_user_login:
                if (this.numUser.equals("")) {
                    intent = new Intent(this.getActivity(), lector_qr.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("opcion", 2);
                    startActivity(intent);
                }
                break;
            case R.id.btn_cambiar_tipo:
                if (mostrando) {
                    this.pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    verContra = getResources().getDrawable(R.drawable.ver_pass2);
                    this.mostrando = false;
                } else {
                    this.pass.setInputType(InputType.TYPE_CLASS_TEXT);
                    verContra = getResources().getDrawable(R.drawable.ver_pass);
                    this.mostrando = true;
                }
                this.mostrar.setBackground(verContra);
                break;
            case R.id.btn_login:
                txtPass = pass.getText().toString();
                if (!txtPass.equals("") && !numUser.equals("")) {
                    if (getArguments().getBoolean("tipo", false)) { //Tienda
                        if (txtPass.equals(this.t.getPass()) && numUser.equals("" + this.t.getId())) {
                            intent = new Intent(this.getActivity(), DashBoard.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("tenda",this.t);
                            intent.putExtra("tipo",true);
                            preferences = getActivity().getSharedPreferences("preferencias",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("login",true);
                            editor.putInt("user",this.t.getId());
                            editor.putInt("tipo",0);
                            editor.commit();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    } else {//Cliente
                        if (this.c.getPass().equals("")) {
                            BD bd = new BD(getContext()) {
                                public void run() {
                                    try {
                                        synchronized (this) {
                                            wait(500);
                                            conectarBDMySQL();
                                            setPasswordClient(Integer.parseInt(numUser), txtPass);
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
                            Toast.makeText(getContext(), "Contraseña guardada", Toast.LENGTH_SHORT).show();
                            intent = new Intent(this.getActivity(), MiPerfil.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("client",this.c);
                            preferences = getActivity().getSharedPreferences("preferencias",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("login",true);
                            editor.putInt("user",this.c.getId());
                            editor.putInt("tipo",1);
                            editor.commit();
                            this.onDestroy();
                        }else if (txtPass.equals(this.c.getPass()) && numUser.equals("" + this.c.getId())) {
                            intent = new Intent(this.getActivity(), MiPerfil.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("client",this.c);
                            preferences = getActivity().getSharedPreferences("preferencias",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("login",true);
                            editor.putInt("user",this.c.getId());
                            editor.putInt("tipo",1);
                            editor.commit();
                            startActivity(intent);
                            this.onDestroy();
                        } else {
                            Toast.makeText(getContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Hay campos vacíos", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

package com.simarro.joshu.clientsqr.Resources.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.R;

import org.greenrobot.eventbus.EventBus;

public class DialogoCambioPass extends DialogFragment implements View.OnClickListener {


    private Client client;
    private Button btnCambio;
    private EditText edActual, edNueva;
    private String actual = "", nueva = "";
    private EventBus bus = EventBus.getDefault();

    public DialogoCambioPass(){

    }

    public static DialogoCambioPass newInstance(Client client){
        DialogoCambioPass dialogo = new DialogoCambioPass();
        Bundle args = new Bundle();
        args.putSerializable("client",client);
        dialogo.setArguments(args);
        return dialogo;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_cambio_pass,null);
            builder.setView(view);
            edActual = view.findViewById(R.id.ed_pass_actual);
            edNueva = view.findViewById(R.id.ed_pass_nueva);
            btnCambio = view.findViewById(R.id.btn_aceptar_cambio_pass);
            btnCambio.setOnClickListener(this);


            this.client = (Client) getArguments().getSerializable("client");

            return builder.create();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_aceptar_cambio_pass){
            this.actual = edActual.getText().toString();
            this.nueva = edNueva.getText().toString();
            if(!this.actual.equals("") && !this.nueva.equals("")){
                if(this.actual.equals(this.client.getPass())){
                    BD bd = new BD(getContext()) {
                        public void run() {
                            try {
                                synchronized (this) {
                                    wait(500);
                                    conectarBDMySQL();
                                    setPasswordClient(client.getId(),nueva);
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
                        this.client.setPass(nueva);
                        Toast.makeText(getContext(),"Contraseña cambiada correctamente",Toast.LENGTH_SHORT).show();
                        bus.post(this.nueva);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getContext(),"Contraseña incorrecta",Toast.LENGTH_SHORT).show();
                    this.edActual.setText("");
                }
            }else{
                Toast.makeText(getContext(),"Hay campos vacíos",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

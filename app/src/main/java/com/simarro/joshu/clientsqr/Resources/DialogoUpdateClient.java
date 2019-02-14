package com.simarro.joshu.clientsqr.Resources;

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

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.R;

public class DialogoUpdateClient extends DialogFragment implements View.OnClickListener {

    private Button ok;
    private EditText nom, tlf, punts;
    private Client c;

    public DialogoUpdateClient(){

    }

    public static DialogoUpdateClient newInstance(Client c){
        DialogoUpdateClient dialogo = new DialogoUpdateClient();
        Bundle args = new Bundle();
        args.putSerializable("client",c);
        dialogo.setArguments(args);
        return dialogo;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_update_client,null);
        c = (Client) getArguments().getSerializable("client");
        builder.setView(view);
        //getFragmentManager().findFragmentById().onDestroy();
        nom = view.findViewById(R.id.ed_update_nom);
        tlf = view.findViewById(R.id.ed_update_tlf);
        punts = view.findViewById(R.id.ed_update_punts);
        nom.setText(c.getNombre());
        tlf.setText(c.getTelefono());
        punts.setText(""+c.getPunts());
        ok = view.findViewById(R.id.btn_ok);
        ok.setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_ok){
            c.setNombre(nom.getText().toString());
            c.setTelefono(tlf.getText().toString());
            c.setPunts(Integer.parseInt(punts.getText().toString()));
            BD bd = new BD(getContext()) {
                public void run() {
                    try {
                        synchronized (this) {
                            wait(500);
                            conectarBDMySQL();
                            modCliente(c.getId(),c.getNombre(),c.getMote(),c.getTelefono(),c.getPunts());
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

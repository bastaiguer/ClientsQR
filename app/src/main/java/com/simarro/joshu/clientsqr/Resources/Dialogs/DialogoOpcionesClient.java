package com.simarro.joshu.clientsqr.Resources.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.simarro.joshu.clientsqr.Activities.panel_control;
import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.R;

public class DialogoOpcionesClient extends DialogFragment implements View.OnClickListener {

    private Button eliminar, modificar, pan_cont;
    private Client c;

    public DialogoOpcionesClient(){

    }

    public static DialogoOpcionesClient newInstance(Client c){
        DialogoOpcionesClient dialogo = new DialogoOpcionesClient();
        Bundle args = new Bundle();
        args.putSerializable("client",c);
        dialogo.setArguments(args);
        return dialogo;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_opciones_client,null);
        c = (Client) getArguments().getSerializable("client");
        builder.setView(view);

        eliminar = view.findViewById(R.id.btn_eliminar);
        modificar = view.findViewById(R.id.btn_modificar);
        pan_cont = view.findViewById(R.id.btn_panel_control);
        eliminar.setOnClickListener(this);
        modificar.setOnClickListener(this);
        pan_cont.setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_eliminar){
            BD bd = new BD(getContext()) {
                public void run() {
                    try {
                        synchronized (this) {
                            wait(500);
                            conectarBDMySQL();
                            delCliente(c.getId());
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
            DialogoDeleteClient dialegDel = DialogoDeleteClient.newInstance(c);
            dialegDel.show(getFragmentManager(),"dialegDelete");
        }else if(v.getId() == R.id.btn_modificar){
            DialogoUpdateClient dialegUpd = DialogoUpdateClient.newInstance(c);
            dialegUpd.show(getFragmentManager(), "dialegUpdate");
        }else if(v.getId() == R.id.btn_panel_control){
            Intent intent = new Intent(getContext(), panel_control.class);
            intent.putExtra("client",c);
            startActivity(intent);
        }
    }
}

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
import android.widget.TextView;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Premi;
import com.simarro.joshu.clientsqr.R;

public class DialogoCanjeoCupon extends DialogFragment implements View.OnClickListener{

    private Button aceptar, cancelar;
    private int id;
    private Premi cupon;

    public DialogoCanjeoCupon(){

    }

    public static DialogoCanjeoCupon newInstance(Premi cupon,int id){
        DialogoCanjeoCupon dialogo = new DialogoCanjeoCupon();
        Bundle args = new Bundle();
        args.putSerializable("cupon",cupon);
        args.putInt("id",id);
        dialogo.setArguments(args);
        return dialogo;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_canjeo_cupones,null);
            this.cupon = (Premi) getArguments().getSerializable("cupon");
            builder.setView(view);
            this.id = getArguments().getInt("id");

            aceptar = view.findViewById(R.id.btn_aceptar_canjeo);
            cancelar = view.findViewById(R.id.btn_cancelar_canjeo);
            aceptar.setOnClickListener(this);
            cancelar.setOnClickListener(this);


            return builder.create();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_aceptar_canjeo){
            BD bd = new BD(getContext()) {
                public void run() {
                    try {
                        synchronized (this) {
                            wait(500);
                            conectarBDMySQL();
                            modCanjeado(id,cupon.getId());
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
        this.dismiss();
    }
}

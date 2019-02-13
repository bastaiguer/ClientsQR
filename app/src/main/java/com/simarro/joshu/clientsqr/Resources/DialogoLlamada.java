package com.simarro.joshu.clientsqr.Resources;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.R;

public class DialogoLlamada extends DialogFragment implements View.OnClickListener {

    private int MY_PERMISSIONS_REQUEST_CALL_PHONE;
    private TextView nom, tlf;
    private Button llamada;

    public DialogoLlamada(){

    }

    public static DialogoLlamada newInstance(Client c){
        DialogoLlamada dialogo = new DialogoLlamada();
        Bundle args = new Bundle();
        args.putSerializable("client",c);
        dialogo.setArguments(args);
        return dialogo;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_llamada,null);
            Client c = (Client) getArguments().getSerializable("client");
            builder.setView(view);

            nom = view.findViewById(R.id.dialeg_nom);
            tlf = view.findViewById(R.id.dialeg_tlf);
            llamada = view.findViewById(R.id.btn_llamada);
            llamada.setOnClickListener(this);
            nom.setText(c.getNombre());
            tlf.setText(c.getTelefono());

            return builder.create();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        String tlf_txt;
        if(v.getId() == R.id.btn_llamada){
            tlf_txt = tlf.getText().toString();
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+tlf_txt));
            // Comprobem si tenim els permisos denegats
            if (ContextCompat.checkSelfPermission(this.getContext(),
                    Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
            //Demanem els permisos
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {
                //Si tenim permisos iniciem la cridada
                try {
                    startActivity(intent);
                } catch(SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

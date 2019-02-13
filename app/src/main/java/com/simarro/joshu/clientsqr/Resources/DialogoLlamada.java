package com.simarro.joshu.clientsqr.Resources;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.R;

public class DialogoLlamada extends DialogFragment{

    private TextView nom, tlf;

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

            nom.setText(c.getNombre());
            tlf.setText(c.getTelefono());

            return builder.create();
    }
}

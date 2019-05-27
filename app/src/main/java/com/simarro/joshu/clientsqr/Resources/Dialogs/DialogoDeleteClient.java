package com.simarro.joshu.clientsqr.Resources.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.R;

public class DialogoDeleteClient extends DialogFragment{

    private TextView txt_client;
    private Client c;

    public DialogoDeleteClient(){

    }

    public static DialogoDeleteClient newInstance(Client c){
        DialogoDeleteClient dialogo = new DialogoDeleteClient();
        Bundle args = new Bundle();
        args.putSerializable("client",c);
        dialogo.setArguments(args);
        return dialogo;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_delete_client, null);
        c = (Client) getArguments().getSerializable("client");
        builder.setView(view);
        txt_client = view.findViewById(R.id.txt_nom_client_del);
        txt_client.setText("" + c.getId() + " - " + c.getNombre());
        return builder.create();
    }
}

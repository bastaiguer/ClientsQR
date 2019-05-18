package com.simarro.joshu.clientsqr.Resources.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.simarro.joshu.clientsqr.R;

public class DialogoPremioVistaPrevia extends DialogFragment {

    private TextView info;

    public DialogoPremioVistaPrevia(){

    }

    public static DialogoPremioVistaPrevia newInstance(String txt){
        DialogoPremioVistaPrevia dialogo = new DialogoPremioVistaPrevia();
        Bundle args = new Bundle();
        args.putString("info",txt);
        dialogo.setArguments(args);
        return dialogo;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_info,null);
            String txt = getArguments().getString("info");
            builder.setView(view);

            info = view.findViewById(R.id.dialeg_info);
            info.setText(txt);

            return builder.create();
    }

}

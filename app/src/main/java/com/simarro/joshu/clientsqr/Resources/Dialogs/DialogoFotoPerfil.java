package com.simarro.joshu.clientsqr.Resources.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simarro.joshu.clientsqr.R;

public class DialogoFotoPerfil extends DialogFragment {

    private ImageView imatge;

    public DialogoFotoPerfil(){

    }

    public static DialogoFotoPerfil newInstance(String img){
        DialogoFotoPerfil dialogo = new DialogoFotoPerfil();
        Bundle args = new Bundle();
        args.putString("foto",img);
        dialogo.setArguments(args);
        return dialogo;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_foto_perfil,null);
            String img = getArguments().getString("foto");
            builder.setView(view);

            this.imatge = view.findViewById(R.id.foto_perfil_dialogo);
            this.imatge.setImageBitmap(base64ToBitmap(img));

            return builder.create();
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

}

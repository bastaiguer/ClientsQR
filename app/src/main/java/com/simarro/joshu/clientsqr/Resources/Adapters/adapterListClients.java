package com.simarro.joshu.clientsqr.Resources.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.R;

import java.util.ArrayList;

public class adapterListClients extends ArrayAdapter<Client> {
    private static class ViewHolder{
        TextView id, nom, tlf, punts;
        ImageView perfil;
    }

    public adapterListClients(ArrayList<Client> data, Context context){
        super(context, R.layout.element_llista_clients, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Client cl = (Client) getItem(position);
        ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.element_llista_clients,parent,false);
            viewHolder.nom = convertView.findViewById(R.id.txt_nom);
            viewHolder.tlf = convertView.findViewById(R.id.txt_telefono);
            viewHolder.punts = convertView.findViewById(R.id.txt_punts);
            viewHolder.perfil = convertView.findViewById(R.id.foto_cliente);
            convertView.setTag(viewHolder);
            result = convertView;
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.nom.setText(cl.getNombre());
        viewHolder.tlf.setText(cl.getTelefono());
        viewHolder.punts.setText(""+cl.getPunts());
        if(cl.getPunts() >= 200){
            viewHolder.punts.setTextColor(result.getResources().getColor(R.color.moltbo));
        }else if(cl.getPunts() >= 100){
            viewHolder.punts.setTextColor(result.getResources().getColor(R.color.bon));
        }else if(cl.getPunts() >= 75){
            viewHolder.punts.setTextColor(result.getResources().getColor(R.color.habitual));
        }else if(cl.getPunts() >= 30){
            viewHolder.punts.setTextColor(result.getResources().getColor(R.color.espontani));
        }else{
            viewHolder.punts.setTextColor(result.getResources().getColor(R.color.normal));
        }

        if(!cl.getImagen().equals("")){
            viewHolder.perfil.setImageBitmap(base64ToBitmap(cl.getImagen()));
        }else{
            viewHolder.perfil.setImageResource(R.drawable.user);
        }

        return result;
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

}

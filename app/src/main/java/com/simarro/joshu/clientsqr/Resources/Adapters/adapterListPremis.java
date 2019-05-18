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
import com.simarro.joshu.clientsqr.Pojo.Premi;
import com.simarro.joshu.clientsqr.R;

import java.util.ArrayList;

public class adapterListPremis extends ArrayAdapter<Premi> {
    private static class ViewHolder{
        TextView titulo, descripcion, puntos;
        ImageView imagen;
    }

    public adapterListPremis(ArrayList<Premi> data, Context context){
        super(context, R.layout.element_llista_premis, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Premi premi = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.element_llista_premis,parent,false);
            viewHolder.titulo = convertView.findViewById(R.id.txt_premio_titulo);
            viewHolder.descripcion = convertView.findViewById(R.id.txt_premio_descripcion);
            viewHolder.imagen = convertView.findViewById(R.id.imagen_premio);
            viewHolder.puntos = convertView.findViewById(R.id.txt_premio_puntos);
            convertView.setTag(viewHolder);
            result = convertView;
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.titulo.setText(premi.getTitulo());
        viewHolder.descripcion.setText(premi.getDescripcion());
        viewHolder.puntos.setText(""+premi.getPuntos());

        viewHolder.imagen.setImageBitmap(base64ToBitmap(premi.getImagen()));

        return result;
    }

    private Bitmap base64ToBitmap(String b64){
        byte[] imageAsBytes = Base64.decode(b64.getBytes(),Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}

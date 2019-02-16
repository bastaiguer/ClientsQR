package com.simarro.joshu.clientsqr.Resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.Punts;
import com.simarro.joshu.clientsqr.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class adapterListPunts extends ArrayAdapter<Punts> {
    private static class ViewHolder{
        TextView id, punts, fecha, hora;
    }

    public adapterListPunts(ArrayList<Punts> data, Context context){
        super(context, R.layout.element_llista_punts, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Punts p = (Punts) getItem(position);
        ViewHolder viewHolder;
        SimpleDateFormat sdf, sdf2;
        String op;

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf2 = new SimpleDateFormat("HH:mm");

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.element_llista_punts,parent,false);
            viewHolder.id = convertView.findViewById(R.id.txt_id_punto_mostrar);
            viewHolder.punts = convertView.findViewById(R.id.txt_puntos_punto_mostrar);
            viewHolder.fecha = convertView.findViewById(R.id.txt_fecha_punto_mostrar);
            viewHolder.hora = convertView.findViewById(R.id.txt_hora_punto_mostrar);
            convertView.setTag(viewHolder);
            result = convertView;
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        if(p.isOperacio()){
            op = "+";
            viewHolder.punts.setTextColor(result.getResources().getColor(R.color.mas));
        }else{
            op = "-";
            viewHolder.punts.setTextColor(result.getResources().getColor(R.color.menos));
        }
        viewHolder.id.setText(""+p.getId());
        viewHolder.punts.setText(op+p.getPunts());
        viewHolder.fecha.setText(sdf.format(p.getRegistro()));
        viewHolder.hora.setText(sdf2.format(p.getRegistro()));

        return result;
    }
}

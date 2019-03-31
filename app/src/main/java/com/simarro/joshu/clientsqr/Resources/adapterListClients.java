package com.simarro.joshu.clientsqr.Resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.R;

import java.util.ArrayList;

public class adapterListClients extends ArrayAdapter<Client> {
    private static class ViewHolder{
        TextView id, nom, tlf, punts;
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

        return result;
    }
}

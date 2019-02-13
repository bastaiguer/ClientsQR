package com.simarro.joshu.clientsqr.Resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.R;

import java.util.ArrayList;

public class adapterListClients extends ArrayAdapter<Client> {
    private static class ViewHolder{
        TextView id, nom, mote, tlf, punts;
    }

    public adapterListClients(ArrayList<Client> data, Context context){
        super(context, R.layout.element_llista, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Client cl = (Client) getItem(position);
        ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.element_llista,parent,false);
            viewHolder.id = convertView.findViewById(R.id.txt_id);
            viewHolder.nom = convertView.findViewById(R.id.txt_nom);
            viewHolder.mote = convertView.findViewById(R.id.txt_mote);
            viewHolder.tlf = convertView.findViewById(R.id.txt_telefono);
            viewHolder.punts = convertView.findViewById(R.id.txt_punts);
            convertView.setTag(viewHolder);
            result = convertView;
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.id.setText(""+cl.getId());
        viewHolder.nom.setText(cl.getNombre());
        viewHolder.mote.setText(cl.getMote());
        viewHolder.tlf.setText(cl.getTelefono());
        viewHolder.punts.setText(""+cl.getPunts());

        return result;
    }
}

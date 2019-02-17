package com.simarro.joshu.clientsqr.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.Punts;
import com.simarro.joshu.clientsqr.R;
import com.simarro.joshu.clientsqr.Resources.adapterListPunts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Tabla_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tabla_Fragment extends Fragment implements View.OnClickListener {

    private Client client;
    private ArrayList<Punts> punts = new ArrayList<>(), edited = new ArrayList<>();
    private Button hoy, semana, mes, todo;
    private TextView id, nombre, registro;
    private Date ahora = new Date();
    private SimpleDateFormat sdf, sdf2;
    private adapterListPunts adapter;
    private ListView listaPuntos;
    private TextView num_punts;
    private int suma, resta;

    public Tabla_Fragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Tabla_Fragment.
     */
    public static Tabla_Fragment newInstance(Client c) {
        Tabla_Fragment fragment = new Tabla_Fragment();
        Bundle args = new Bundle();
        args.putSerializable("client", c);
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = (Client) getArguments().getSerializable("client");
        BD bd = new BD(getContext()) {
            public void run() {
                try {
                    synchronized (this) {
                        wait(500);
                        conectarBDMySQL();
                        getRegistrePuntsByIdClient(client.getId());
                        cerrarConexion();
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    Log.e("Error", "Waiting didnt work!!");
                    e.printStackTrace();
                }
            }
        };
        Thread th = new Thread(bd);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.punts = bd.obPunts();
        adapter = new adapterListPunts(this.punts, getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tabla, container, false);
        id = rootView.findViewById(R.id.txt_mostrar_id);
        nombre = rootView.findViewById(R.id.txt_mostrar_nombre);
        registro = rootView.findViewById(R.id.txt_mostrar_registro);
        hoy = rootView.findViewById(R.id.btn_hoy);
        semana = rootView.findViewById(R.id.btn_semana);
        mes = rootView.findViewById(R.id.btn_mes);
        todo = rootView.findViewById(R.id.btn_todo);
        num_punts = rootView.findViewById(R.id.txt_num_punts);
        hoy.setOnClickListener(this);
        semana.setOnClickListener(this);
        mes.setOnClickListener(this);
        todo.setOnClickListener(this);
        id.setText("" + client.getId());
        nombre.setText(client.getNombre());
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        registro.setText(sdf.format(client.getRegistro()));
        listaPuntos = rootView.findViewById(R.id.listView_punts);
        listaPuntos.setAdapter(adapter);
        edited = this.punts;
        if(this.edited.size() > 0){
            num_punts.setText("Total +"+sumaPunts()+"  -"+restaPunts());
        }
        return rootView;
    }

    public int sumaPunts(){
        int numPunts = 0;
        for(Punts p: this.edited){
            if(p.isOperacio()){
                numPunts += p.getPunts();
            }
        }
        return numPunts;
    }

    public int restaPunts(){
        int numPunts = 0;
        for(Punts p: this.edited){
            if(!p.isOperacio()){
                numPunts += p.getPunts();
            }
        }
        return numPunts;
    }

    @Override
    public void onClick(View v) {
        edited = new ArrayList<>();
        switch (v.getId()) {
            case R.id.btn_hoy:
                Calendar c1,c2;
                for (Punts p : punts) {
                    sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    sdf2.format(ahora);
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.format(p.getRegistro());
                    c1 = sdf2.getCalendar();
                    c2 = sdf.getCalendar();
                    if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
                        edited.add(p);
                    }
                }
                adapter = new adapterListPunts(this.edited, getContext());
                listaPuntos.setAdapter(adapter);
                if(this.edited.size() > 0){
                    num_punts.setText("Total +"+sumaPunts()+"  -"+restaPunts());
                }else{
                    num_punts.setText("No se registraron puntos");
                }
                break;
            case R.id.btn_semana:
                for (Punts p : punts) {
                    sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    sdf2.format(ahora);
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.format(p.getRegistro());
                    c1 = sdf2.getCalendar();
                    c2 = sdf.getCalendar();
                    if (c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
                        edited.add(p);
                    }
                }
                adapter = new adapterListPunts(this.edited, getContext());
                listaPuntos.setAdapter(adapter);
                if(this.edited.size() > 0){
                    num_punts.setText("Total +"+sumaPunts()+"  -"+restaPunts());
                }else{
                    num_punts.setText("No se registraron puntos");
                }
                break;
            case R.id.btn_mes:
                for (Punts p : punts) {
                    if (p.getRegistro().getMonth() == ahora.getMonth()) {
                        edited.add(p);
                    }
                }
                adapter = new adapterListPunts(this.edited, getContext());
                listaPuntos.setAdapter(adapter);
                if(this.edited.size() > 0){
                    num_punts.setText("Total +"+sumaPunts()+"  -"+restaPunts());
                }else{
                    num_punts.setText("No se registraron puntos");
                }
                break;
            case R.id.btn_todo:
                edited = punts;
                adapter = new adapterListPunts(this.edited, getContext());
                listaPuntos.setAdapter(adapter);
                if(this.edited.size() > 0){
                    num_punts.setText("Total +"+sumaPunts()+"  -"+restaPunts());
                }else{
                    num_punts.setText("No se registraron puntos");
                }
                break;
        }
    }
}

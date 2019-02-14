package com.simarro.joshu.clientsqr.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.LlistaClients;
import com.simarro.joshu.clientsqr.R;
import com.simarro.joshu.clientsqr.Resources.adapterListClients;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AdministrarClientes_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdministrarClientes_Fragment extends Fragment implements AdapterView.OnItemClickListener {



    public AdministrarClientes_Fragment() {
        // Required empty public constructor
    }


    public static AdministrarClientes_Fragment newInstance() {
        AdministrarClientes_Fragment fragment = new AdministrarClientes_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_administrar_clientes_,container,false);
        rootView.setBackgroundColor(getResources().getColor(R.color.bar));
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

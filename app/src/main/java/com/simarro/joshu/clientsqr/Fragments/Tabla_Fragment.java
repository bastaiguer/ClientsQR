package com.simarro.joshu.clientsqr.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simarro.joshu.clientsqr.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Tabla_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tabla_Fragment extends Fragment {

    public Tabla_Fragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment Tabla_Fragment.
     */
    public static Tabla_Fragment newInstance() {
        Tabla_Fragment fragment = new Tabla_Fragment();
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tabla,container,false);
        rootView.setBackgroundColor(getResources().getColor(R.color.bar));
        return rootView;
    }
}

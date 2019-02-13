package com.simarro.joshu.clientsqr.Fragments;

import android.content.Context;
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
 * Use the {@link Mapa#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mapa extends Fragment {

    public Mapa() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static Mapa newInstance() {
        Mapa fragment = new Mapa();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

package com.simarro.joshu.clientsqr.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.simarro.joshu.clientsqr.BBDD.BD;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.simarro.joshu.clientsqr.Pojo.LlistaClients;
import com.simarro.joshu.clientsqr.R;
import com.simarro.joshu.clientsqr.Resources.DialogoLlamada;
import com.simarro.joshu.clientsqr.Resources.DialogoOpcionesClient;
import com.simarro.joshu.clientsqr.Resources.adapterListClients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class lista_clientes extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private LlistaClients clients = new LlistaClients();
    private ListView llista;
    private Toolbar toolbar;
    private adapterListClients adapter;
    private DialogoLlamada dialeg;
    private DialogoOpcionesClient dialeg2 = new DialogoOpcionesClient();
    private BD bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        llista = findViewById(R.id.listView_clients);
        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        //Accedemos a la BBDD mediante un Thread
        bd = new BD(getApplicationContext()) {
            public void run() {
                try {
                    synchronized (this) {
                        wait(500);
                        conectarBDMySQL();
                        getClientes();
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
        //Obtenemos la lista de los clientes en la BBDD
        this.clients.addLlista(bd.obClients());
        if (this.clients.size() > 0) {
            adapter = new adapterListClients(clients.getClients(), this.getApplicationContext());
            llista.setAdapter(adapter);
            llista.setOnItemClickListener(this);
        } else {
            findViewById(R.id.clients_not_size).setVisibility(View.VISIBLE);
        }
        getSupportActionBar().setTitle(clients.size() + " Clients");
        llista.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.whatsapp:
                intent = new Intent(this, comunicacion_clientes.class);
                startActivity(intent);
                break;
            case R.id.ordenar_por_puntos:
                ordenarPorPuntos();
                break;
            case R.id.graficos:
                intent = new Intent(this, panel_control.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void ordenarPorPuntos() {
        LlistaClients c = new LlistaClients(), c2 = new LlistaClients();
        ArrayList<Integer> punts = new ArrayList<Integer>();
        c.addLlista(bd.obClients());
        for (Client cl : c) {
            punts.add(cl.getPunts());
        }
        Comparator<Integer> comparador = Collections.reverseOrder();
        Collections.sort(punts, comparador);
        for (Integer i : punts) {
            for (int j = 0; j < c.size(); j++) {
                if (c.get(j) != null) {
                    if (c.get(j).getPunts() == i) {
                        c2.add(c.get(j));
                        c.set(j, null);
                    }
                }
            }
        }
        adapter = new adapterListClients(c2, getApplicationContext());
        llista.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_cliente, menu);
        MenuItem search = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LlistaClients llistaBusqueda = new LlistaClients();
                for (Client c : clients) {
                    if (c.getNombre().contains(query) || c.getTelefono().contains(query)) {
                        llistaBusqueda.add(c);
                    }
                }
                adapter = new adapterListClients(llistaBusqueda, getApplicationContext());
                llista.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LlistaClients llistaBusqueda = new LlistaClients();
                for (Client c : clients) {
                    if (c.getNombre().contains(newText) || c.getTelefono().contains(newText)) {
                        llistaBusqueda.add(c);
                    }
                }
                adapter = new adapterListClients(llistaBusqueda, getApplicationContext());
                llista.setAdapter(adapter);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!dialeg2.isAdded()) {
            dialeg = DialogoLlamada.newInstance(clients.get(position));
            dialeg.show(getSupportFragmentManager(), "dialegCridada");
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        dialeg2 = DialogoOpcionesClient.newInstance(clients.get(position));
        dialeg2.show(getSupportFragmentManager(), "dialegModDel");
        return false;
    }
}

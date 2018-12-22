package com.simarro.joshu.clientsqr.BBDD;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.Activities.MainActivity;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.mysql.jdbc.*;
import com.mysql.jdbc.Driver;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BD {

    private String user, password, ip;
    private Connection conexionMySQL;

    public BD(){

    }

    public BD(String u, String pass, String i){
        this.user = u;
        this.password = pass;
        this.ip = i;
    }

    public Connection getConexion() {
        return conexionMySQL;
    }

    public void setConexion(Connection conexionMySQL) {
        this.conexionMySQL = conexionMySQL;
    }

    public void conectarBDMySQL(Context c){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String urlOdbc="jdbc:mysql://localhost:3306/clientesqr";
            conexionMySQL=(java.sql.DriverManager.getConnection(urlOdbc+"?user=picanya&password=picanya"));
            if(!conexionMySQL.isClosed()){
                Toast.makeText(c,"Conexión establecida",Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            Toast.makeText(c,"No se ha podido establecer la conexión",Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Client> getClientes(){
        ArrayList<Client> clientes = new ArrayList<>();
        try {
            this.conexionMySQL.prepareStatement("SELECT * FROM clients");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
}

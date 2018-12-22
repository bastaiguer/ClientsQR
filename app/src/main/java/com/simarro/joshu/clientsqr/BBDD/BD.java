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

public class BD implements Runnable {

    private String user, password, ip;
    private Connection conexionMySQL;
    private ArrayList<Client> clientes;
    private boolean conectado = false;
    private Context context;

    public BD() {

    }

    public BD(Context con) {
        this.context = con;
    }

    public ArrayList<Client> obClients() {
        return this.clientes;
    }

    public Connection getConexion() {
        return conexionMySQL;
    }

    public void setConexion(Connection conexionMySQL) {
        this.conexionMySQL = conexionMySQL;
    }

    public void conectarBDMySQL() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String urlOdbc = "jdbc:mysql://192.168.0.17:3306/clientsqr";
            conexionMySQL = (DriverManager.getConnection(urlOdbc, "picanya", "picanya"));
            if (!conexionMySQL.isClosed()) {
                System.out.println("Conexión establecida");
                conectado = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cerrarConexion() {
        try {
            this.conexionMySQL.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getClientes() {
        this.clientes = new ArrayList<>();
        java.sql.PreparedStatement stmt = null;
        Client client;
        ResultSet rs = null;
        try {
            stmt = this.conexionMySQL.prepareStatement("SELECT * FROM clients");
            rs = stmt.executeQuery();
            while (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setNombre(rs.getString("nombre"));
                client.setMote(rs.getString("mote"));
                client.setTelefono(rs.getString("telefono"));
                client.setPunts(rs.getInt("punts"));
                this.clientes.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar la conexión...");
            }
        }

    }

    @Override
    public void run() {

    }
}

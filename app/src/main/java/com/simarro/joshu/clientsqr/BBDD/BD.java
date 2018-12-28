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

    private int result;
    private Connection conexionMySQL;
    private ArrayList<Client> clientes;
    private boolean conectado = false;
    private int mostrarpunts, addPunts;
    private Context context;

    public BD() {

    }

    public BD(Context con) {
        this.context = con;
    }

    public int getResult(){
        return this.result;
    }

    public int getMostrarpunts(){
        return this.mostrarpunts;
    }

    public int getAddPunts(){
        return addPunts;
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

    public void modPuntos(int x, int id) {
        java.sql.PreparedStatement stmt = null;
        try {
            stmt = conexionMySQL.prepareCall("UPDATE clients SET punts=? WHERE id=?");
            stmt.setInt(1, x);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar la conexión...");
            }
        }
    }

    public void addCliente(int id, String nom, String mote, String tlf){
        java.sql.PreparedStatement stmt = null;
        java.sql.PreparedStatement stmt2 = null;
        this.result = 1;
        ResultSet rs = null;
        Client c = null;
        try {
            stmt = conexionMySQL.prepareCall("INSERT INTO clients (id, nombre, mote, telefono, punts) VALUES (?,?,?,?,0)");
            stmt2 = conexionMySQL.prepareCall("SELECT * FROM clients WHERE id=?");
            stmt2.setInt(1, id);
            rs = stmt2.executeQuery();
            while (rs.next()) {
                c = new Client();
            }
            if(c==null) {
                this.result = 0;
                stmt.setInt(1, id);
                stmt.setString(2, nom);
                stmt.setString(3, mote);
                stmt.setString(4, tlf);
                stmt.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar la conexión...");
            }
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

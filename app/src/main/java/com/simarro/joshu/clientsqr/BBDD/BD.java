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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    public ArrayList<Client> obClients() {
        return this.clientes;
    }


    protected void conectarBDMySQL() {
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

    protected void modPuntos(int x, int id) {
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

    protected void addCliente(int id, String nom, String mote, String tlf, double longitud, double latitud){
        java.sql.PreparedStatement stmt = null;
        java.sql.PreparedStatement stmt2 = null;
        this.result = 1;
        ResultSet rs = null;
        Client c = null;
        try {
            stmt = conexionMySQL.prepareCall("INSERT INTO clients (id, nombre, mote, telefono, punts, registro, latitud, longitud) VALUES (?,?,?,?,0,?,?,?)");
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
                stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
                stmt.setDouble(6,latitud);
                stmt.setDouble(7,longitud);
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

    protected void modCliente(int id, String nom, String mote, String tlf, int pts){
        java.sql.PreparedStatement stmt = null;
        java.sql.PreparedStatement stmt2;
        this.result = 1;
        ResultSet rs;
        Client c = null;
        try {
            stmt = conexionMySQL.prepareCall("UPDATE clients SET nombre=?, mote=?, telefono=?, punts=? WHERE id=?");
            stmt2 = conexionMySQL.prepareCall("SELECT * FROM clients WHERE id=?");
            stmt2.setInt(1, id);
            rs = stmt2.executeQuery();
            while (rs.next()) {
                c = new Client();
            }
            if(c!=null) {
                this.result = 0;
                stmt.setString(1, nom);
                stmt.setString(2, mote);
                stmt.setString(3, tlf);
                stmt.setInt(4, pts);
                stmt.setInt(5, id);
                stmt.executeUpdate();
            }else{
                System.out.println("No se ha encontrado el cliente...");
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

    public Connection getConexionMySQL() {
        return conexionMySQL;
    }

    protected void delCliente(int id){
        java.sql.PreparedStatement stmt = null;
        java.sql.PreparedStatement stmt2;
        this.result = 1;
        ResultSet rs;
        Client c = null;
        try {
            stmt = conexionMySQL.prepareCall("DELETE FROM clients WHERE id=?");
            stmt2 = conexionMySQL.prepareCall("SELECT * FROM clients WHERE id=?");
            stmt2.setInt(1, id);
            rs = stmt2.executeQuery();
            while (rs.next()) {
                c = new Client();
            }
            if(c!=null) {
                this.result = 0;
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }else{
                System.out.println("No se ha encontrado el cliente...");
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

    protected void getClientes() {
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
                    client.setRegistro(rs.getTimestamp("registro"));
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

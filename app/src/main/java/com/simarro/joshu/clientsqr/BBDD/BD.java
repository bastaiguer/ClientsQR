package com.simarro.joshu.clientsqr.BBDD;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.simarro.joshu.clientsqr.Activities.MainActivity;
import com.simarro.joshu.clientsqr.Pojo.Client;
import com.mysql.jdbc.*;
import com.mysql.jdbc.Driver;
import com.simarro.joshu.clientsqr.Pojo.Premi;
import com.simarro.joshu.clientsqr.Pojo.Punts;
import com.simarro.joshu.clientsqr.Pojo.Tenda;

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
    private ArrayList<Punts> punts;
    private ArrayList<Premi> premis;
    private boolean conectado = false;
    private int mostrarpunts, addPunts;
    private Context context;
    private Client clientOb = new Client();
    private Tenda tendaOb = new Tenda();

    public BD() {

    }

    public BD(Context con) {
        this.context = con;
    }

    public int getResult() {
        return this.result;
    }

    public Client getClientOb(){
        return this.clientOb;
    }

    public Tenda getTendaOb(){
        return this.tendaOb;
    }

    public ArrayList<Client> obClients() {
        return this.clientes;
    }

    public ArrayList<Punts> obPunts() {
        return this.punts;
    }

    public ArrayList<Premi> obPremis(){
        return this.premis;
    }

    protected void conectarBDMySQL() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String urlOdbc = "jdbc:mysql://84.127.223.3:6767/clientsqr";
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

    protected void modCanjeado(int id){
        java.sql.PreparedStatement stmt = null;
        try {
            stmt = conexionMySQL.prepareCall("UPDATE registre_premis SET canjeado=? WHERE id=?");
            stmt.setInt(1, 1);
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

    protected void listarMisPremios(int idCliente){
        this.premis = new ArrayList<>();
        java.sql.PreparedStatement stmt = null;
        Premi premi;
        ResultSet rs = null;
        try {
            stmt = this.conexionMySQL.prepareStatement("SELECT * FROM registre_premis WHERE client = ? AND canjeado = 0");
            stmt.setInt(1,idCliente);
            rs = stmt.executeQuery();
            while (rs.next()) {
                premi = new Premi();
                premi.setId(rs.getInt("id"));
                premi.setTitulo(rs.getString("titulo"));
                premi.setDescripcion(rs.getString("descripcion"));
                premi.setImagen(rs.getString("imagen"));
                premi.setPuntos(rs.getInt("puntos"));
                this.premis.add(premi);
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

    protected void canjearPremio(int idPremio, int idCliente){
        java.sql.PreparedStatement stmt = null, stmt2;
        ResultSet rs;
        int id = 0;
        try {
            stmt = conexionMySQL.prepareCall("INSERT INTO registre_premis (id, premi, client, registre) VALUES (?,?,?,?)");
            stmt2 = conexionMySQL.prepareCall("SELECT * FROM registre_premis");
            rs = stmt2.executeQuery();
            while (rs.next()) {
                id++;
            }
            stmt.setInt(1, id);
            stmt.setInt(2, idPremio);
            stmt.setInt(3, idCliente);
            stmt.setTimestamp(4,new Timestamp(new Date().getTime()));
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

    protected void addCliente(int id, String nom, String mote, String tlf, double longitud, double latitud, int idTienda) {
        java.sql.PreparedStatement stmt = null;
        java.sql.PreparedStatement stmt2 = null;
        this.result = 1;
        ResultSet rs = null;
        Client c = null;
        try {
            stmt = conexionMySQL.prepareCall("INSERT INTO clients (id, nombre, mote, telefono, punts, registro, latitud, longitud, tienda, imagen) VALUES (?,?,?,?,0,?,?,?,?,?)");
            stmt2 = conexionMySQL.prepareCall("SELECT * FROM clients WHERE id=?");
            stmt2.setInt(1, id);
            rs = stmt2.executeQuery();
            while (rs.next()) {
                c = new Client();
            }
            if (c == null) {
                this.result = 0;
                stmt.setInt(1, id);
                stmt.setString(2, nom);
                stmt.setString(3, mote);
                stmt.setString(4, tlf);
                stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
                stmt.setDouble(6, latitud);
                stmt.setDouble(7, longitud);
                stmt.setInt(8, idTienda);
                stmt.setString(9, "");
                stmt.executeUpdate();
            }
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

    protected void addRegistrePunts(int idclient, boolean operacio, int punts, double longitud, double latitud) {
        java.sql.PreparedStatement stmt = null;
        java.sql.PreparedStatement stmt3;
        this.result = 1;
        int id = 0;
        ResultSet rs;
        Client c = null;
        try {
            stmt = conexionMySQL.prepareCall("INSERT INTO registre_punts (id, idclient, operacio, punts, registro, latitud, longitud) VALUES (?,?,?,?,?,?,?)");
            stmt3 = conexionMySQL.prepareCall("SELECT *  FROM registre_punts");
            rs = stmt3.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    id = rs.getInt("id") + 1;
                }
            }
            this.result = 0;
            stmt.setInt(1, id);
            stmt.setInt(2, idclient);
            stmt.setBoolean(3, operacio);
            stmt.setInt(4, punts);
            stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
            stmt.setDouble(6, latitud);
            stmt.setDouble(7, longitud);
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

    protected void getRegistrePuntsByIdClient(int id) {
        this.punts = new ArrayList<>();
        java.sql.PreparedStatement stmt = null;
        Punts punts;
        ResultSet rs = null;
        try {
            stmt = this.conexionMySQL.prepareStatement("SELECT * FROM registre_punts WHERE idclient = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                punts = new Punts();
                punts.setId(rs.getInt("id"));
                punts.setIdclient(rs.getInt("idclient"));
                punts.setOperacio(rs.getBoolean("operacio"));
                punts.setPunts(rs.getInt("punts"));
                punts.setRegistro(rs.getTimestamp("registro"));
                punts.setLongitud(rs.getDouble("longitud"));
                punts.setLatitud(rs.getDouble("latitud"));
                this.punts.add(punts);
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

    protected void modCliente(int id, String nom, String mote, String tlf, int pts) {
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
            if (c != null) {
                this.result = 0;
                stmt.setString(1, nom);
                stmt.setString(2, mote);
                stmt.setString(3, tlf);
                stmt.setInt(4, pts);
                stmt.setInt(5, id);
                stmt.executeUpdate();
            } else {
                System.out.println("No se ha encontrado el cliente...");
            }
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

    protected void modImagenClient(int id, String imagen) {
        java.sql.PreparedStatement stmt = null;
        java.sql.PreparedStatement stmt2;
        this.result = 1;
        ResultSet rs;
        Client c = null;
        try {
            stmt = conexionMySQL.prepareCall("UPDATE clients SET imagen=? WHERE id=?");
            stmt2 = conexionMySQL.prepareCall("SELECT * FROM clients WHERE id=?");
            stmt2.setInt(1, id);
            rs = stmt2.executeQuery();
            while (rs.next()) {
                c = new Client();
            }
            if (c != null) {
                this.result = 0;
                stmt.setString(1, imagen);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            } else {
                System.out.println("No se ha encontrado el cliente...");
            }
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

    public Connection getConexionMySQL() {
        return conexionMySQL;
    }

    protected void delCliente(int id) {
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
            if (c != null) {
                this.result = 0;
                stmt.setInt(1, id);
                stmt.executeUpdate();
            } else {
                System.out.println("No se ha encontrado el cliente...");
            }
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

    protected void getClientes(int tenda) {
        this.clientes = new ArrayList<>();
        java.sql.PreparedStatement stmt = null;
        Client client;
        ResultSet rs = null;
        try {
            stmt = this.conexionMySQL.prepareStatement("SELECT * FROM clients WHERE tienda = ?");
            stmt.setInt(1,tenda);
            rs = stmt.executeQuery();
            while (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setNombre(rs.getString("nombre"));
                client.setMote(rs.getString("mote"));
                client.setTelefono(rs.getString("telefono"));
                client.setPunts(rs.getInt("punts"));
                client.setRegistro(rs.getTimestamp("registro"));
                client.setLongitud(rs.getDouble("longitud"));
                client.setLatitud(rs.getDouble("latitud"));
                client.setTienda(tenda);
                client.setImagen(rs.getString("imagen"));
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

    public void getClient(int qr){
        java.sql.PreparedStatement stmt = null;
        Client client;
        ResultSet rs = null;
        this.clientOb.setNombre("..truco..");
        try {
            stmt = this.conexionMySQL.prepareStatement("SELECT * FROM clients WHERE id = ?");
            stmt.setInt(1, qr);
            rs = stmt.executeQuery();
            while (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setNombre(rs.getString("nombre"));
                client.setMote(rs.getString("mote"));
                client.setTelefono(rs.getString("telefono"));
                client.setPunts(rs.getInt("punts"));
                client.setRegistro(rs.getTimestamp("registro"));
                client.setLongitud(rs.getDouble("longitud"));
                client.setLatitud(rs.getDouble("latitud"));
                client.setPass(rs.getString("pass"));
                client.setImagen(rs.getString("imagen"));
                client.setTienda(Integer.parseInt(rs.getString("tienda")));
                this.clientOb = client;
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

    public void getTenda(int qr){
        java.sql.PreparedStatement stmt = null;
        Tenda tenda;
        ResultSet rs = null;
        this.tendaOb.setNombre("..truco..");
        try {
            stmt = this.conexionMySQL.prepareStatement("SELECT * FROM tienda WHERE id = ?");
            stmt.setInt(1, qr);
            rs = stmt.executeQuery();
            while (rs.next()) {
                tenda = new Tenda();
                tenda.setId(rs.getInt("id"));
                tenda.setNombre(rs.getString("nombre"));
                tenda.setEmpresa(rs.getString("empresa"));
                tenda.setPass(rs.getString("pass"));
                this.tendaOb = tenda;
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

    public void setPasswordClient(int id, String pass){
        java.sql.PreparedStatement stmt = null;
        try {
            stmt = conexionMySQL.prepareCall("UPDATE clients SET pass=? WHERE id=?");
            stmt.setString(1, pass);
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

    public void addPremio(Premi premi, int idt){
        java.sql.PreparedStatement stmt = null, stmt2;
        ResultSet rs;
        int id = 0;
        try {
            stmt = conexionMySQL.prepareCall("INSERT INTO premis (id, titulo, descripcion, imagen, puntos, tienda) VALUES (?,?,?,?,?,?)");
            stmt2 = conexionMySQL.prepareCall("SELECT * FROM premis");
            rs = stmt2.executeQuery();
            while (rs.next()) {
                id++;
            }
                stmt.setInt(1, id);
                stmt.setString(2, premi.getTitulo());
                stmt.setString(3, premi.getDescripcion());
                stmt.setString(4, premi.getImagen());
                stmt.setInt(5, premi.getPuntos());
                stmt.setInt(6, idt);
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

    protected void getPremios(int idt) {
        this.premis = new ArrayList<>();
        java.sql.PreparedStatement stmt = null;
        Premi premi;
        ResultSet rs = null;
        try {
            stmt = this.conexionMySQL.prepareStatement("SELECT * FROM premis WHERE tienda = ?");
            stmt.setInt(1,idt);
            rs = stmt.executeQuery();
            while (rs.next()) {
                premi = new Premi();
                premi.setId(rs.getInt("id"));
                premi.setTitulo(rs.getString("titulo"));
                premi.setDescripcion(rs.getString("descripcion"));
                premi.setImagen(rs.getString("imagen"));
                premi.setPuntos(rs.getInt("puntos"));
                this.premis.add(premi);
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

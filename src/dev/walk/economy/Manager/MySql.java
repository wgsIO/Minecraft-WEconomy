package dev.walk.economy.Manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.plugin.Plugin;

public class MySql {

    private String host,db,user,pass;
    private int port;
    private Connection c;

    public MySql(String host, int port, String database,String user, String password) {
        this.host = host;
        this.port = port;
        this.db = database;
        this.user = user;
        this.pass = password;

    }
    public void close(PreparedStatement s) {
        try {
            s.close();
        } catch (SQLException e) {}
    }
    public boolean openConnection() {
        try {
            if (c != null && !c.isClosed()) {
                return true;
            }
        }catch (Exception e) {}
        String connectionURL = "jdbc:mysql://"
                + this.host + ":" + this.port;
        if (db != null) {
            connectionURL = connectionURL + "/" + this.db;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection(connectionURL, this.user, this.pass);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public ResultSet query(PreparedStatement statement) {
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            return null;
        }
    }
    public void queryUpdate(PreparedStatement statement) {
        try {
            statement.executeUpdate();
        } catch (SQLException e) {}
    }
    public PreparedStatement newPreparedStatement(String query) {
        try {
            return c.prepareStatement(query);
        } catch (SQLException e) {
            return null;
        }
    }

}

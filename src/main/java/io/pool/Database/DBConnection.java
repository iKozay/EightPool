package io.pool.Database;

import io.pool.controller.BallController;

import java.sql.*;

public class DBConnection {

    protected static final String connectionString = "jdbc:sqlite:src/main/resources/DBFiles/EightBallDatabase.db";
    protected static Connection connection = null;

    //Connection to database
    protected static void connect() {
        try {
            if ((connection == null)||(connection.isClosed())) {
                connection = DriverManager.getConnection(connectionString);
                System.out.println("Connection successful");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {return connection;}

}

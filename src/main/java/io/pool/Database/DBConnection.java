package io.pool.Database;

import io.pool.controller.BallController;
import io.pool.view.BallView;

import java.sql.*;

public class DBConnection {

    public static final String connectionString = "";
    public static Connection connection = null;

    //Connection to database
    public static void connect() {
        try {
            if ((connection == null)||(connection.isClosed())) {
                connection = DriverManager.getConnection(connectionString);
                System.out.println("Connection successful");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }



    //Player table
    public static void createNewPlayerDB(){
        connect();
        PreparedStatement ps;

        String sql = "INSERT INTO PlayerTable (name) VALUES (?)";
        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1, "player name test");
            //TODO add other data to player table
            ps.execute();
            System.out.println("Player created!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removePlayerDB(int playerNB){
        connect();
        PreparedStatement ps;
        String sql = "delete from Player WHERE playerNumber = ? ";

        try{
            ps = connection.prepareStatement(sql);
            ps.setInt(1, playerNB);
            ps.execute();
            System.out.println("Player removed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void readPlayerDB(){
        //TODO
    }

    public static void updatePlayerTableDB(){
        //TODO
    }


    static BallController bc = new BallController();
    //Ball position table
    //double x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, xw;
    //double y1, y2, y3, y4, y5, y6, y7, y8, y9, y10, y11, y12, y13, y14, y15, yw;
    public static void setBallPositions(){
        bc.addBallsPosition();
        connect();
        PreparedStatement ps;

        for(int i=0; i<bc.xballPos.size(); i++){
            String sql = "INSERT INTO BallConfiguration(x" + i+1 + ") VALUES (?)";
            try{
                ps = connection.prepareStatement(sql);
                ps.setFloat(1, Float.valueOf(bc.xballPos.get(i).toString()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for(int i=0; i<bc.yballPos.size(); i++){
            String sql = "INSERT INTO BallConfiguration(y" + i+1 + ") VALUES (?)";
            try{
                ps = connection.prepareStatement(sql);
                ps.setFloat(1, Float.valueOf(bc.yballPos.get(i).toString()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public static void createNewSavedPosition(){
        connect();
        PreparedStatement ps;

        String sql = "INSERT INTO BallConfiguration (layoutName) VALUES (?)";
        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1, "layout name test");
            //TODO add ball positions
            ps.execute();
            System.out.println("Layout saved!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setBallPositions();
    }

    public static void removeSavedPosition(int layoutNB){connect();
        PreparedStatement ps;
        String sql = "delete from BallConfiguration WHERE layoutNumber = ? ";

        try{
            ps = connection.prepareStatement(sql);
            ps.setInt(1, layoutNB);
            ps.execute();
            System.out.println("Player removed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }}

    public static void readSavedPosition(){
        //TODO
    }

    public static void updateSavedPosition(){
        //TODO
    }



//Getters and setters
    public static Connection getConnection() {return connection;}

}

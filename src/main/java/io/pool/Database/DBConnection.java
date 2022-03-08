package io.pool.Database;

import io.pool.controller.BallController;
import io.pool.model.BallModel;
import io.pool.view.BallView;

import java.sql.*;

public class DBConnection {

    public static final String connectionString = "jdbc:sqlite:src/main/resources/DBFiles/EightBallDatabase.db";
    public static Connection connection = null;
    static BallController bc = new BallController();

    //Connection to database
    private static void connect() {
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

    public static boolean hasBeenCalled = false;
    public static void createNewSavedPosition(){
        connect();
        PreparedStatement ps;

        String sqlName = "INSERT INTO BallConfiguration (layoutName) VALUES (?)";
        try{
            ps = connection.prepareStatement(sqlName);
            ps.setString(1, "layout name test");
            ps.execute();
            System.out.println("Layout saved!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            for(BallModel e: bc.ballModelArrayList()){
            String sqlPos = "UPDATE BallConfiguration set (x"+e.getNumber()+")=?, (y"+e.getNumber()+")=? WHERE layoutName = ?";
            ps = connection.prepareStatement(sqlPos);
            ps.setFloat(1, e.getPositionX().floatValue());
            ps.setFloat(2, e.getPositionY().floatValue());
            ps.setString(3, "layout name test");
            ps.execute();
            System.out.println("New layout saved!");
        }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void removeSavedPosition(int layoutNB){
        connect();
        PreparedStatement ps;
        String sql = "delete from BallConfiguration WHERE layoutNumber = ? ";

        try{
            ps = connection.prepareStatement(sql);
            ps.setInt(1, layoutNB);
            ps.execute();
            System.out.println("Player removed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void readSavedPosition(){
        //TODO
    }

    public static void updateSavedPosition(){
        //TODO
    }

    /**Deletes all data from BallConfiguration Table*/
    public static void deleteAllData(){
        connect();
        PreparedStatement ps;
        String sql = "DELETE FROM BallConfiguration";
        try {
            ps = connection.prepareStatement(sql);
            ps.execute();
            System.out.println("All saved layouts deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//Getters and setters
    public static Connection getConnection() {return connection;}

}

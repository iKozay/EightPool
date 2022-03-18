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
    public static void createNewPlayerDB(String name){
        connect();
        PreparedStatement ps;

        String sql = "INSERT INTO PlayerTable (name) VALUES (?)";
        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            //TODO add other data to player table
            ps.execute();
            System.out.println("Player created!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removePlayerDB(String name){
        connect();
        PreparedStatement ps;
        String sql = "delete from Player WHERE name = ? ";

        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1, name);
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
        connect();
        PreparedStatement ps;
        String sql;
    }

    /**
     This is to set up the first line of the database where the most recent game position will be saved.
     */
    public static void instantiateLastLayoutDB(int gameType){
        connect();
        PreparedStatement ps;

        String sqlName = "INSERT INTO BallConfiguration (layoutNumber, layoutName) VALUES (?, ?)";
        try{
            ps = connection.prepareStatement(sqlName);
            ps.setInt(1, gameType);
            ps.setString(2, "lastPosition");
            ps.execute();
            System.out.println("last layout instantiated in database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** boolean is to check if the updateLastPosition method has been called after all balls have stopped
        we only want this method called once every time the balls have stopped
        updateLastPosition only updates the first line of the database.
        The first line of the database is exclusive to the most recent position of the balls.
     */
    public static boolean hasBeenCalled = false;
    /**
     Updates the first row of the database which is the most recent table state.
     This is used to fetch the game if the application closes or if you go to settings and return back to the game.
     */
    public static void updateLastPosition(){
        connect();
        PreparedStatement ps;
        try{
            for(BallModel e: bc.ballModelArrayList()){
            String sqlPos = "UPDATE BallConfiguration set (x"+e.getNumber()+")=?, (y"+e.getNumber()+")=? WHERE layoutName = ?";
            ps = connection.prepareStatement(sqlPos);
            ps.setFloat(1, e.getPositionX().floatValue());
            ps.setFloat(2, e.getPositionY().floatValue());
            ps.setString(3, "lastPosition");
            ps.execute();
            System.out.println("New layout saved!");
        }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     Saves custom positions in database
     */
    public static void createNewSavedPosition(int gameType, String layoutName){
        connect();
        PreparedStatement ps;

        String sql = "INSERT INTO BallConfiguration (layoutNumber, layoutName) VALUES (?,?)";
        try{
            ps = connection.prepareStatement(sql);
            ps.setInt(1, gameType);
            ps.setString(2, layoutName);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            for(BallModel e: bc.ballModelArrayList()){
                String sqlPos = "UPDATE BallConfiguration set (x"+e.getNumber()+")=?, (y"+e.getNumber()+")=? WHERE layoutName = ?";
                ps = connection.prepareStatement(sqlPos);
                ps.setFloat(1, e.getPositionX().floatValue());
                ps.setFloat(2, e.getPositionY().floatValue());
                ps.setString(3, layoutName);
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

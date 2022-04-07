package io.pool.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerTableDB extends DBConnection{

    /**used for readData method*/
    public String[] PlayerTableDBReadOptions = {"playerNumber", "name"};

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
        String sql = "delete from PlayerTable WHERE name = ? ";
        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.execute();
            System.out.println("Player removed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static String readPlayerTableDB(String playerName, String readOption){
        connect();
        PreparedStatement ps;
        ResultSet rs;
        try{
            String sql = "SELECT " + readOption + " from PlayerTable WHERE name = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, playerName);
            rs = ps.executeQuery();

            return rs.getString(1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void updatePlayerTableDB(int numberOfWins, int numberOfLoss, int averageShotsPerGame, String playerName){
        connect();
        PreparedStatement ps;
        try{
            String sql = "UPDATE PlayerTable set (numberOfWins)=?, (numberOfLoss)=?, (averageShotsPerGame)=? WHERE name = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, numberOfWins);
            ps.setInt(2, numberOfLoss);
            ps.setInt(3, averageShotsPerGame);
            ps.setString(4, playerName);
            ps.execute();
            System.out.println("PlayerTable updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

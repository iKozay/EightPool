package io.pool.Database;

import io.pool.model.PlayerModel;
import javafx.scene.paint.Color;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerTableDB extends DBConnection{

    /**used for readData method*/
    public String[] PlayerTableDBReadOptions = {"ID", "Username", "SelectedPoolCue", "Win", "Loss", "AverageShots"};

    public static void createNewPlayerDB(PlayerModel player){
        connect();
        PreparedStatement ps;

        String sql = "INSERT INTO PlayerTable (Username,SelectedPoolCue, Win, Loss, AverageShots) VALUES (?,1,0,0,0)";
        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1, player.getUsername());
            ps.execute();
            System.out.println("Player Added to DB!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removePlayerDB(PlayerModel player){
        connect();
        PreparedStatement ps;
        String sql = "delete from PlayerTable WHERE Username = ? ";
        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1, player.getUsername());
            ps.execute();
            System.out.println("Player removed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static String readPlayerTableDB(PlayerModel player, String property){
        connect();
        PreparedStatement ps;
        ResultSet rs;
        try{
            String sql = "SELECT " + property + " from PlayerTable WHERE Username = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, player.getUsername());
            rs = ps.executeQuery();

            return rs.getString(1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void renamePlayerTableDB(PlayerModel player, String newUsername){
        connect();
        PreparedStatement ps;
        try{
            String sql = "UPDATE PlayerTable set (Username)=? WHERE Username = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, newUsername);
            ps.setString(2, player.getUsername());
            ps.execute();
            System.out.println("PlayerTable updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePlayerTableDB(PlayerModel player){
        connect();
        PreparedStatement ps;
        try{
            String sql = "UPDATE PlayerTable set (Win)=?, (Loss)=?, (AverageShots)=? WHERE Username = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, player.getNumberOfWins());
            ps.setInt(2, player.getNumberOfLoss());
            ps.setInt(3, player.getAverageNumberOfShotsPerGame());
            ps.setString(4, player.getUsername());
            ps.execute();
            System.out.println("PlayerTable updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void fetchAllPlayers(){
        connect();
        PreparedStatement ps;
        ResultSet rs;
        String sql = "SELECT * from PlayerTable";

        try{
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()){
                PlayerModel.playersList.add(new PlayerModel(rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),rs.getInt(6)));
            }
            if(PlayerModel.playersList.size()==0) {
                createNewPlayerDB(new PlayerModel("Player1",1,0,0,0));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}

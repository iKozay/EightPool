package io.pool.Database;

import io.pool.controller.BallController;
import io.pool.model.BallModel;
import io.pool.model.PlayerModel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BallConfigurationDB extends DBConnection{

    /**used for readData method*/
    public final String[] BallConfigurationDBReadOptions = {"layoutName", "GameType", "x1", "y1", "x2", "y2", "x3", "y3", "x4", "y4", "x5", "y5", "x6", "y6", "x7", "y7", "x8", "y8", "x9", "y9", "x10", "y10", "x11", "y11", "x12", "y12", "x13", "y13", "x14", "y14", "x15", "y15", "x16", "y16", "Player1Name", "Player2Name", "Player1BallType", "Player2BallType", "Player1Score", "Player2Score", "PlayerTurnName"};


    /**
     This is to set up the first line of the database where the most recent game position will be saved.
     */
    public static void instantiateLastLayoutDB(int gameType, PlayerModel player1, PlayerModel player2, String playerTurn){
        connect();
        PreparedStatement ps;

        String sqlName = "UPDATE BallConfiguration SET Player1Name=?, Player2Name=?, Player1BallType=-1, Player2BallType=-1, Player1Score=0, Player2Score=0, PlayerTurnName=? WHERE layoutName='lastPosition' AND GameType = ?";
        try{
            ps = connection.prepareStatement(sqlName);
            ps.setString(1, player1.getUsername());
            ps.setString(2, player2.getUsername());
            ps.setString(3, playerTurn);
            ps.setInt(4, gameType);
            ps.execute();
            System.out.println("last layout instantiated in database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void assignBallType(int gameType,int player1BallType, int player2BallType){
        connect();
        PreparedStatement ps;
        try{
            String sqlPos = "UPDATE BallConfiguration set (Player1BallType)=?, (Player2BallType)=? WHERE layoutName = 'lastPosition' AND GameType = ?";
            ps = connection.prepareStatement(sqlPos);
            ps.setInt(1, player1BallType);
            ps.setInt(2, player2BallType);
            ps.setInt(3, gameType);
            ps.execute();
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
    public static void updateLastPosition(int gameType, String playerTurn){
        connect();
        PreparedStatement ps;
        try{
            for(BallModel e: BallController.ballModelArrayList()){
                String sqlPos = "UPDATE BallConfiguration set (x"+e.getNumber()+")=?, (y"+e.getNumber()+")=? WHERE layoutName = 'lastPosition' AND GameType=?";
                ps = connection.prepareStatement(sqlPos);
                ps.setFloat(1, e.getPositionX().floatValue());
                ps.setFloat(2, e.getPositionY().floatValue());
                ps.setInt(3, gameType);
                ps.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            String playerSql = "UPDATE BallConfiguration set (PlayerTurnName)=? WHERE layoutName = 'lastPosition' AND GameType=?";
            ps = connection.prepareStatement(playerSql);
            ps.setString(1,playerTurn);
            ps.setInt(2,gameType);
            ps.execute();
            System.out.println("New layout saved!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     Saves custom positions in database
     */

    public static void createNewSavedPosition(int gameType, String layoutName, PlayerModel player1, PlayerModel player2, int player1BallType, int player2BallType, String playerTurn){
        connect();
        PreparedStatement ps;

        String sql = "INSERT INTO BallConfiguration (GameType, layoutName, Player1Name, Player2Name, Player1BallType, Player2BallType, Player1Score, Player2Score, PlayerTurnName) VALUES (?,'lastPosition', ?, ?, -1, -1, 0, 0, ?);";
        try{
            ps = connection.prepareStatement(sql);
            ps.setInt(1, gameType);
            ps.setString(2, player1.getUsername());
            ps.setString(3, player2.getUsername());
            ps.setString(4, playerTurn);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            for(BallModel e: BallController.ballModelArrayList()){
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
        String sql = "delete from BallConfiguration WHERE GameType = ? ";

        try{
            ps = connection.prepareStatement(sql);
            ps.setInt(1, layoutNB);
            ps.execute();
            System.out.println("Player removed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**if we want the last saved game position, the layoutName should be lastPosition */
    public static String readBallConfiguration(String readOption, String layoutName){
        connect();
        PreparedStatement ps;
        ResultSet rs;

        try{
            String sql = "Select " + readOption + " from BallConfiguration WHERE layoutName = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, layoutName);
            rs = ps.executeQuery();

            return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadLastPosition(int gameType){
        connect();
        PreparedStatement ps;
        ResultSet rs;

        try{
            for(BallModel e: BallController.ballModelArrayList()){
                String sqlPos = "SELECT (x"+e.getNumber()+") from BallConfiguration WHERE layoutName = 'lastPosition' AND GameType=?";
                ps = connection.prepareStatement(sqlPos);
                ps.setInt(1, gameType);
                rs = ps.executeQuery();
                e.setPositionX(new BigDecimal(rs.getFloat(0)));

                sqlPos = "SELECT (y"+e.getNumber()+") from BallConfiguration WHERE layoutName = 'lastPosition' AND GameType=?";
                ps = connection.prepareStatement(sqlPos);
                ps.setInt(1, gameType);
                rs = ps.executeQuery();
                e.setPositionY(new BigDecimal(rs.getFloat(0)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

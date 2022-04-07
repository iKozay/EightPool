package io.pool.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsDB extends DBConnection{

    /**used for readData method*/
    public String[] SettingsDBReadOptions = {"ControlOption", "PoolCueHelper", "FrictionPercentage", "CurrentTableTheme", "CurrentCueTheme", "PlayerName"};

    public static void createNewSavedSettings(String playerName){
        connect();
        PreparedStatement ps;

        String sql = "INSERT INTO Settings (PlayerName) VALUES (?)";
        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1, playerName);
            //TODO add other data to method
            ps.execute();
            System.out.println("Setting preferences saved!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeSettingsDB(String playerName){
        connect();
        PreparedStatement ps;
        String sql = "delete from Settings WHERE PlayerName = ? ";
        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1, playerName);
            ps.execute();
            System.out.println("Setting preferences removed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String readSettingPreferencesDB(String playerName, String readOption){
        connect();
        PreparedStatement ps;
        ResultSet rs;
        try{
            String sql = "SELECT " + readOption + " from Settings WHERE playerName = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, playerName);
            rs = ps.executeQuery();

            return rs.getString(1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**updates all settings in settings DB table*/
    public static void updateSettingsDB(int controlOption, int poolCueHelper, double frictionPercent, int currentTableTheme, int currentCueTheme, String playerName){
        connect();
        PreparedStatement ps;
        try{
            String sql = "UPDATE Settings set (ControlOption)=?, (PoolCueHelper)=?, (FrictionPercentage)=?, (CurrentTableTheme)=?, (CurrentCueTheme)=? WHERE PlayerName = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, controlOption);
            ps.setInt(2, poolCueHelper);
            ps.setDouble(3, frictionPercent);
            ps.setInt(4, currentTableTheme);
            ps.setInt(5, currentCueTheme);
            ps.setString(6, playerName);
            ps.execute();
            System.out.println("In game settings updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**updates the settings that can be modified in game*/
    public static void updateInGameSettingsDB(int currentTableTheme, int currentCueTheme, String playerName){
        connect();
        PreparedStatement ps;
        
        try{
            String sql = "UPDATE Settings set (CurrentTableTheme)=?, (CurrentCueTheme)=? WHERE playerName = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, currentTableTheme);
            ps.setInt(2, currentCueTheme);
            ps.setString(3, playerName);
            ps.execute();
            System.out.println("In game settings updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}

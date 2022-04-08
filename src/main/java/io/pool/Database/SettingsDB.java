package io.pool.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsDB extends DBConnection{

    /**used for readData method*/
    public static String[] SettingsDBReadOptions = {"ControlOption", "PoolCueHelper", "FrictionPercentage", "CurrentTableTheme"};

//    public static void createNewSavedSettings(String playerName){
//        connect();
//        PreparedStatement ps;
//
//        String sql = "INSERT INTO Settings (PlayerName) VALUES (?)";
//        try{
//            ps = connection.prepareStatement(sql);
//            ps.setString(1, playerName);
//
//            ps.execute();
//            System.out.println("Setting preferences saved!");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void removeSettingsDB(String playerName){
//        connect();
//        PreparedStatement ps;
//        String sql = "delete from Settings WHERE PlayerName = ? ";
//        try{
//            ps = connection.prepareStatement(sql);
//            ps.setString(1, playerName);
//            ps.execute();
//            System.out.println("Setting preferences removed!");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public static double readSettingPreferencesDB(String setting){
        connect();
        PreparedStatement ps;
        ResultSet rs;
        try{
            String sql = "SELECT " + setting + " from Settings";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            return rs.getDouble(1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**updates all settings in settings DB table*/
    public static void updateSettingsDB(String setting, double settingValue, boolean isInteger){
        connect();
        PreparedStatement ps;
        try{
            String sql = "UPDATE Settings set ("+ setting +")=?";
            ps = connection.prepareStatement(sql);
            if(isInteger){
                ps.setInt(1, (int)settingValue);
            }else{
                ps.setDouble(1, settingValue);
            }
            ps.execute();
            System.out.println("In game settings updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//    /**updates the settings that can be modified in game*/
//    public static void updateInGameSettingsDB(int currentTableTheme, int currentCueTheme, String playerName){
//        connect();
//        PreparedStatement ps;
//
//        try{
//            String sql = "UPDATE Settings set (CurrentTableTheme)=?, (CurrentCueTheme)=? WHERE playerName = ?";
//            ps = connection.prepareStatement(sql);
//            ps.setInt(1, currentTableTheme);
//            ps.setInt(2, currentCueTheme);
//            ps.setString(3, playerName);
//            ps.execute();
//            System.out.println("In game settings updated!");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
}

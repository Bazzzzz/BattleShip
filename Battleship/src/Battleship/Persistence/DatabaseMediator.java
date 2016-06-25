/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Persistence;

import Battleship.Domain.Account;
import Battleship.Interfaces.IDatabaseMediator;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sebas
 */
public class DatabaseMediator implements IDatabaseMediator {

    private static Connection con = null;
    private static CallableStatement callStatement = null;
    private static ResultSet rs = null;
    private static String query = "";

    private static String url;
    private static String user;
    private static String password;

    public DatabaseMediator(String ipAddress) {
        url = "jdbc:mysql://" + ipAddress + ":3306/battleship";
        user = "root";
        password = "";
    }

    /**
     * Opens the connection to the database.
     *
     * @return True if opened.
     */
    private static boolean openConnection() {
        try {
            con = DriverManager.getConnection(url, user, password);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Closes the connection to the database.
     */
    private static void closeConnection() {
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean addNewPlayer(Account account) {
        if(account == null) {
            return false;
        }
        
        try {
            if(openConnection()) {
                callStatement = con.prepareCall("call addPlayer(?,?)");
                callStatement.setString(1, account.getLoginName());
                callStatement.setString(2, account.getPassword());
                
                callStatement.execute();
                
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return false;
    }

    @Override
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        try {
            if(openConnection()) {
                callStatement = con.prepareCall("call login(?,?)");
                callStatement.setString(1, username);
                callStatement.setString(2, password);
                
                callStatement.execute();
                
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return false;
    }

    @Override
    public List<Account> getHighschore() {
        List<Account> highscores = new ArrayList<>();
        try {
            if(openConnection()) {
                callStatement = con.prepareCall("call highscore()");
                
                callStatement.execute();
                
                ResultSet result = callStatement.getResultSet();
                
                while(result.next()) {
                    String username = result.getString("username");
                    int score = result.getInt("score");
                    
                    highscores.add(new Account(username, score));
                }
                return highscores;
            }       
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return new ArrayList<>();
        
    }
    
    @Override
    public boolean logout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addScore(String username, int score) {
        if(!username.isEmpty() && username != null && score > 0) {
            try {
                if(openConnection()) {
                    callStatement = con.prepareCall("call changescore(?,?)");
                    callStatement.setString(1, username);
                    callStatement.setInt(2, score);
                    
                    callStatement.execute();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

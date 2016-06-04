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

    /**
     * Add a new player account to the database.
     *
     * @param account not null
     * @return True if added to database.
     */
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

    /**
     * Retrieve a player from the database and make him log in.
     *
     * @param username not null
     * @param password not null
     * @return True if logged in.
     */
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

    /**
     * Retrieve the top 10 players from the database.
     * @return List holding the top 10 players
     */
    @Override
    public List<Account> getHighschore() {
        List<Account> highscores = new ArrayList<>();
        try {
            if(openConnection()) {
                callStatement = con.prepareCall("(call highschore()");
                
                callStatement.execute();
                
                ResultSet result = callStatement.getResultSet();
                
                while(result.next()) {
                    String username = result.getString("name");
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
    
    
    /**
     * Log out a player.
     *
     * @return True if logged out.
     */
    @Override
    public boolean logout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

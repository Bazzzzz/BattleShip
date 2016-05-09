/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import Battleship.Domain.Account;
import Battleship.Interfaces.IDatabaseMediator;
import Battleship.Persistence.DatabaseMediator;

/**
 *
 * @author sebas
 */
public class ApplicationHandler {  
    private IDatabaseMediator dbm;
    public static Account loggedInPlayer; 
    
    public ApplicationHandler() {
        dbm = new DatabaseMediator();
        loggedInPlayer = null;
    }

    public Account getLoggedInPlayer() {
        return loggedInPlayer;
    }
    
    public boolean newPlayerToDB(String username, String password) {
        Account newAccount = new Account(username, password);
        if (newAccount != null) {
            return dbm.addNewPlayer(newAccount);
        }
        return false;
    }
    
    public void loginPlayer(String username, String password) {
        if(dbm.login(username, password)) {
            loggedInPlayer = new Account(username, password);
        }
    }
    public boolean logoutPlayer() {
        loggedInPlayer = null;
        return dbm.logout();
    }
    
}

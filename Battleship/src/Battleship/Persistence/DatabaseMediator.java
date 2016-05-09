/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Persistence;

import Battleship.Domain.Account;
import Battleship.Interfaces.IDatabaseMediator;

/**
 *
 * @author sebas
 */
public class DatabaseMediator implements IDatabaseMediator {

    /**
     * Add a new player account to the database.
     * @param account
     * @return 
     */
    @Override
    public boolean addNewPlayer(Account account) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * Retrieve a player from the database and make him log in.
     * @return 
     */
    @Override
    public boolean login(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Log out a player.
     * @return 
     */
    @Override
    public boolean logout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

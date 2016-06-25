/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Interfaces;

import Battleship.Domain.Account;
import java.util.List;

/**
 *
 * @author sebas
 */
public interface IDatabaseMediator {

    /**
     * Add a new player account to the database.
     *
     * @param account not null
     * @return True if added to database.
     */
    public boolean addNewPlayer(Account account);

    /**
     * Retrieve a player from the database and make him log in.
     *
     * @param username not null
     * @param password not null
     * @return True if logged in.
     */
    public boolean login(String username, String password);

    /**
     * Log out a player.
     *
     * @return True if logged out.
     */
    public boolean logout();

    /**
     * Retrieve the top 10 players from the database.
     *
     * @return List holding the top 10 players
     */
    public List<Account> getHighschore();

    /**
     * Change the score of a player in the database.
     * @param username not null or empty
     * @param score Greater than 0
     */
    public void addScore(String username, int score);
}

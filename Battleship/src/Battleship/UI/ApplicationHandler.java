/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import Battleship.Domain.Account;
import Battleship.Interfaces.IDatabaseMediator;
import Battleship.Interfaces.ILobby;
import Battleship.Persistence.DatabaseMediator;
import Battleship.RMI.RMIClient;

/**
 *
 * @author sebas
 */
public class ApplicationHandler {

    private IDatabaseMediator dbm;
    public static Account loggedInPlayer;
    private static RMIClient rmiClient;
    private static String joinedLobbyName;
    
    public ApplicationHandler(String ipAddress) {
        dbm = new DatabaseMediator(ipAddress);
        loggedInPlayer = null;
        rmiClient = null;
        joinedLobbyName = null;
    }

    public Account getLoggedInPlayer() {
        return loggedInPlayer;
    }
    /**
     * Set the RMIClient object and enable usage over the application. Object is remote.
     * @param rmiClient 
     */
    public void setRMIClient(RMIClient rmiClient) {
        ApplicationHandler.rmiClient = rmiClient;
    }
    /**
     * Get the RMIClient object. This is the remote object.
     * @return Remote object RMIClient.
     */
    public RMIClient getRMIClient() {
        return ApplicationHandler.rmiClient;
    }
    /**
     * Set the joinedLobby object and enable usage over the application.
     * @param lobby 
     */
    public void setJoinedLobby(String lobbyName) {
        ApplicationHandler.joinedLobbyName = lobbyName;
    }
    /**
     * Get the lobby a user clicked in the lobby list controller.
     * @return Selected lobby.
     */
    public String getJoinedLobbyName() {
        return ApplicationHandler.joinedLobbyName;
    }
    /**
     * Add a new player to the database.
     * @param username
     * @param password
     * @return True if added, False if not.
     */
    public boolean newPlayerToDB(String username, String password) {
        Account newAccount = new Account(username, password);

        return dbm.addNewPlayer(newAccount);
    }
    /**
     * Log a player into the application.
     * @param username
     * @param password 
     */
    public void loginPlayer(String username, String password) {
        if (dbm.login(username, password)) {
            loggedInPlayer = new Account(username, password);
        }
    }

    public boolean logoutPlayer() {
        loggedInPlayer = null;
        return dbm.logout();
    }

}

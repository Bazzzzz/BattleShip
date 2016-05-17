/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author sebas
 */
public interface ILobby {
    /**
     * Create a game manager object.
     * @return The created game manager object.
     */
    public IGameManager createGameManager();
    /**
     * Add a player to the game manager object.
     * @param player not null.
     */
    public void addPlayer(IPlayer player);
    /**
     * Remove a player from the game manager object
     * @param player not null
     */
    public void removePlayerFromLobby(IPlayer player);
    /**
     * Get the game manager object.
     * @return The game manager object.
     */
    public IGameManager getGameManager();
    /**
     * Get the list of players in the lobby.
     * @return List of players.
     */
    public List<IPlayer> getPlayers();
    
    /**
     * Get the name of the lobby
     * @return The name of the lobby.
     */
    public String getName();
}

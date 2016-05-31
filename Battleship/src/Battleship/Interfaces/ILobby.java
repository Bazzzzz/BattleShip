/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Interfaces;

import fontys.observer.RemotePropertyListener;
import fontys.observer.RemotePublisher;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author sebas
 */
public interface ILobby extends RemotePublisher {
    /**
     * Create a game manager object.
     * @return The created game manager object.
     */
    public IGameManager createGameManager() throws RemoteException;
    /**
     * Add a player to the game manager object.
     * @param player not null.
     */
    public void addPlayer(IPlayer player) throws RemoteException;
    /**
     * Remove a player from the game manager object
     * @param player not null
     */
    public void removePlayerFromLobby(String playerName) throws RemoteException;
    /**
     * Get the game manager object.
     * @return The game manager object.
     */
    public IGameManager getGameManager() throws RemoteException;
    /**
     * Get the list of players in the lobby.
     * @return List of players.
     */
    public List<IPlayer> getPlayers() throws RemoteException;
    
    /**
     * Get the name of the lobby
     * @return The name of the lobby.
     */
    public String getName() throws RemoteException;
    
    public void updateLobby(IPlayer player, boolean joined) throws RemoteException;
    
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException;
    
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException;
    
}

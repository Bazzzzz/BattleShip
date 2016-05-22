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
public interface IClientManager extends Remote {

    /**
     * Get the list of all lobbies currently active.
     *
     * @return List of lobbies.
     * @throws RemoteException
     */
    public List<ILobby> getLobbies() throws RemoteException;

    /**
     * Add a lobby to the list of lobbies.
     *
     * @param lobby
     * @return True if added, False if not.
     * @throws java.rmi.RemoteException
     */
    public boolean addLobby(ILobby lobby) throws RemoteException;

    /**
     * Remove a lobby from the list.
     *
     * @param lobby
     * @return True if removed, False if not.
     * @throws java.rmi.RemoteException
     */
    public boolean removeLobby(ILobby lobby) throws RemoteException;

    /**
     * Return the lobby which holds a certain player.
     *
     * @param playerName Not null or empty
     * @return Lobby that holds the player or null.
     * @throws RemoteException
     */
    public ILobby findLobbyByPlayer(String playerName) throws RemoteException;

    /**
     * Find a lobby by it's name.
     *
     * @param name Not null or empty
     * @return Lobby that has the name.
     * @throws java.rmi.RemoteException
     */
    public ILobby findLobbyByName(String name) throws RemoteException;

    /**
     * Remove all lobbies from the list.
     *
     * @throws java.rmi.RemoteException
     */
    public void removeAllLobbies() throws RemoteException;

    /**
     * Update a lobby when a player joins or leaves.
     *
     * @param lobby Not null.
     * @return True if updated. False if not.
     * @throws RemoteException
     */
    public boolean updateLobby(ILobby lobby) throws RemoteException;
}

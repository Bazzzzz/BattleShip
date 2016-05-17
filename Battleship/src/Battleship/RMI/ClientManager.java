/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.RMI;

import Battleship.Interfaces.IClientManager;
import Battleship.Interfaces.ILobby;
import Battleship.Interfaces.IPlayer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author sebas
 */
public class ClientManager extends UnicastRemoteObject implements IClientManager {

    private List<ILobby> lobbies;

    public ClientManager() throws RemoteException {
        lobbies = new ArrayList<>();
    }

    @Override
    public List<ILobby> getLobbies() throws RemoteException {
        return lobbies;
    }

    /**
     * Add a lobby to the list of lobbies.
     *
     * @param lobby
     * @return True if added, False if not.
     * @throws java.rmi.RemoteException
     */
    @Override
    public boolean addLobby(ILobby lobby) throws RemoteException {
        if (lobbies.isEmpty()) {
            lobbies.add(lobby);
            return true;
        } else {
            boolean isListed = false;
            Iterator<ILobby> itrLobby = lobbies.iterator();
            while (itrLobby.hasNext()) {
                ILobby tempLobby = itrLobby.next();
                if (tempLobby.equals(lobby)) {
                    isListed = true;
                }
            }
            if (!isListed) {
                lobbies.add(lobby);
                return true;
            }
        }
        return false;
    }

    /**
     * Remove a lobby from the list.
     *
     * @param lobby
     * @return True if removed, False if not.
     * @throws java.rmi.RemoteException
     */
    @Override
    public boolean removeLobby(ILobby lobby) throws RemoteException {
        if (!lobbies.isEmpty()) {
            for (ILobby tempLobby : this.lobbies) {
                if (tempLobby.equals(lobby)) {
                    this.lobbies.remove(lobby);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return the lobby which holds a certain player.
     *
     * @param playerName Not null or empty
     * @return Lobby that holds the player or null.
     * @throws RemoteException
     */
    @Override
    public ILobby findLobbyByPlayer(String playerName) throws RemoteException {
        if (playerName != null && !playerName.equals("")) {
            for (ILobby tempLobby : this.lobbies) {
                for(IPlayer tempPlayer : tempLobby.getPlayers()) {
                    if(tempPlayer.getName().equals(playerName)) {
                        return tempLobby;
                    }
                }

            }
        }
        return null;
    }

    /**
     * Find a lobby by it's name.
     *
     * @param name Not null or empty
     * @return Lobby that has the name.
     * @throws java.rmi.RemoteException
     */
    @Override
    public ILobby findLobbyByName(String name) throws RemoteException {
        if (name != null && !name.equals("")) {
            for (ILobby tempLobby : this.lobbies) {
                if (tempLobby.getName().equals(name)) {
                    return tempLobby;
                }
            }
        }
        return null;
    }

    /**
     * Remove all lobbies from the list.
     *
     * @throws java.rmi.RemoteException
     */
    @Override
    public void removeAllLobbies() throws RemoteException {
        if (!this.lobbies.isEmpty()) {
            this.lobbies.clear();
        }
    }
    /**
     * Update a lobby when a player joins or leaves.
     * @param lobby Not null.
     * @return True if updated. False if not.
     * @throws RemoteException 
     */
    @Override
    public boolean updateLobby(ILobby lobby) throws RemoteException {
        if (lobby != null) {
            for(ILobby tempLobby : this.lobbies) {
                if(tempLobby.getName().equals(lobby.getName())) {
                    this.lobbies.remove(tempLobby);
                    this.lobbies.add(lobby);
                    return true;
                }
            }
        }
        return false;
    }
    
}
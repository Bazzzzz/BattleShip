/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Domain;

import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.ILobby;
import Battleship.Interfaces.IPlayer;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author sebas
 */
public class Lobby implements ILobby, Serializable {
    private String name;
    private List<IPlayer> players;
    private IGameManager gameManager;
    
    public Lobby(String name) throws RemoteException {
        this.players = new ArrayList<>();
        this.gameManager = null;
        this.name = name;
        
    }
    @Override
    public String getName() {
        return name;
    } 
    @Override
    public IGameManager getGameManager() {
        return this.gameManager;
    }
    @Override
    public List<IPlayer> getPlayers() {
        return this.players;
    }

    @Override
    public IGameManager createGameManager() {
        if(this.players.size() == 2) {
            gameManager = new GameManager();
            gameManager.addPlayer(this.players.get(0));
            gameManager.addPlayer(this.players.get(1));
            
            return gameManager;
        }
        return null;
    }
    @Override
    public void addPlayer(IPlayer player) {
        if(player != null) {
            this.players.add(player);
        }
    }
    @Override
    public void removePlayerFromLobby(IPlayer player) {
        if(player != null) {
            Iterator<IPlayer> itrPlayer = this.players.iterator();
            IPlayer tempPlayer = null;
            while(itrPlayer.hasNext()) {
                IPlayer playerFound = itrPlayer.next();
                if (playerFound.equals(player)) {
                    tempPlayer = playerFound;
                    break;
                }
            }
            if(tempPlayer != null) {
                this.players.remove(tempPlayer);
                if (this.gameManager != null) {
                    this.removePlayerFromGM(player);
                }
            }
        }
    }
    
    private void removePlayerFromGM(IPlayer player) {
        this.gameManager.removePlayer(player);
    }
    
    public void changeName(String newName) {
        if(newName != null) {
            this.name = newName;
        }
    }
    
}

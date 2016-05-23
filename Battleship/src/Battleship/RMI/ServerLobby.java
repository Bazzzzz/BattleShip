/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.RMI;

import Battleship.Domain.GameManager;
import Battleship.Domain.Lobby;
import Battleship.Exceptions.BattleshipExceptions;
import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.ILobby;
import Battleship.Interfaces.IPlayer;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import fontys.observer.RemotePublisher;
import java.beans.PropertyChangeEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sebas
 */
public class ServerLobby extends UnicastRemoteObject implements ILobby, RemotePublisher {

    private String name;
    private List<IPlayer> players;
    private IGameManager gameManager;

    private BasicPublisher basicPublisher;

    public ServerLobby(String name) throws RemoteException {
        String[] properties = {"lobbies"};
        basicPublisher = new BasicPublisher(properties);

        basicPublisher.inform(this, "lobbies", null, this);
        
        this.players = new ArrayList<>();
        this.gameManager = null;
        this.name = name;
    }

 @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public IGameManager getGameManager() throws RemoteException {
        return this.gameManager;
    }

    @Override
    public List<IPlayer> getPlayers() throws RemoteException {
        return this.players;
    }

    @Override
    public IGameManager createGameManager() throws RemoteException {
        if (this.players.size() == 2) {
            try {
                gameManager = new GameManager();
                gameManager.addPlayer(this.players.get(0));
                gameManager.addPlayer(this.players.get(1));
            } catch (RemoteException ex) {
                Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
            }

            return gameManager;
        }
        return null;
    }

    @Override
    public void addPlayer(IPlayer player) throws RemoteException {
        try {
            if (player != null && this.players.size() < 2) {
                this.players.add(player);
            } else {
                throw new BattleshipExceptions("Lobby is full.");
            }
        } catch (BattleshipExceptions ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void removePlayerFromLobby(IPlayer player) throws RemoteException {
        if (player != null) {
            Iterator<IPlayer> itrPlayer = this.players.iterator();
            IPlayer tempPlayer = null;
            while (itrPlayer.hasNext()) {
                IPlayer playerFound = itrPlayer.next();
                if (playerFound.equals(player)) {
                    tempPlayer = playerFound;
                    break;
                }
            }
            if (tempPlayer != null) {
                this.players.remove(tempPlayer);
                if (this.gameManager != null) {
                    this.removePlayerFromGM(player);
                }
            }
        }
    }

    private void removePlayerFromGM(IPlayer player) {
        try {
            this.gameManager.removePlayer(player);
        } catch (RemoteException ex) {
            Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changeName(String newName) throws RemoteException {
        if (newName != null) {
            this.name = newName;
        }
    }
    @Override
    public String toString() {
        return String.format("%s' lobby: ", this.name); // TODO: Find relation between account and player, add account score to the string format.
    }
    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {
        basicPublisher.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
        basicPublisher.removeListener(listener, property);
    }

    @Override
    public void updateLobby(IPlayer player, boolean joined) throws RemoteException {
        if (joined) {
            this.players.add(player);
        } else {
            this.players.remove(player);
        }
    }
}

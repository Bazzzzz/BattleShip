/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Domain;

import Battleship.Exceptions.BattleshipExceptions;
import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.ILobby;
import Battleship.Interfaces.IPlayer;
import Battleship.RMI.RMIClient;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import fontys.observer.RemotePublisher;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author sebas
 */
public class Lobby implements ILobby, Serializable, RemotePropertyListener {

    private String name;
    private List<IPlayer> players;
    private IGameManager gameManager;
    

    public Lobby(String name) throws RemoteException {
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

    public void changeName(String newName) {
        if (newName != null) {
            this.name = newName;
        }
    }

    @Override
    public String toString() {
        return String.format("%s' lobby: ", this.name); // TODO: Find relation between account and player, add account score to the string format.
    }

    @Override
    public void updateLobby(IPlayer player, boolean joined) throws RemoteException {
        if (joined) {
            this.players.add(player);
        } else {
            this.players.remove(player);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        ILobby lobby = (ILobby) evt.getNewValue();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(lobby.getName());
                } catch (RemoteException ex) {
                    Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

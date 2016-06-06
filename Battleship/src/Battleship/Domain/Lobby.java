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
public class Lobby extends UnicastRemoteObject implements ILobby {

    private String name;
    private List<IPlayer> players;
    private IGameManager gameManager;

    public Lobby(String name) throws RemoteException {
        this.players = new ArrayList<>();
        this.gameManager = null;
        this.name = name + "' lobby";
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
                String gameName = this.name.split("'")[0];
                gameManager = new GameManager(gameName+"' game");
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
                System.out.println("PLayer added to lobby: " + player.toString());
            } else {
                throw new BattleshipExceptions("Lobby is full.");
            }
        } catch (BattleshipExceptions ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void removePlayerFromLobby(String playerName) throws RemoteException {
        if (playerName != null) {
            Iterator<IPlayer> itrPlayer = this.players.iterator();
            IPlayer tempPlayer = null;
            while (itrPlayer.hasNext()) {
                IPlayer playerFound = itrPlayer.next();
                if (playerFound.getName().equals(playerName)) {
                    tempPlayer = playerFound;
                    break;
                }
            }
            if (tempPlayer != null) {
                this.players.remove(tempPlayer);
                if (this.gameManager != null) {
                    this.removePlayerFromGM(tempPlayer);
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
        return String.format("%s", this.name); // TODO: Find relation between account and player, add account score to the string format
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
    public boolean playersReady() throws RemoteException {
        if (this.players.size() == 2) {
            if(this.players.get(0).isPlayerReady() && this.players.get(1).isPlayerReady()) {
                return true;
            }
        }
        return false;
    }
    
/*
    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        ILobby lobby = (ILobby) evt.getNewValue();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("PropertyChange: " + lobby.getName());
                    
                } catch (RemoteException ex) {
                    Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }*/

    /**
     * Lobbies are equal if the name and amount of players list is equal.
     *
     * @param o
     * @return True if equal.
     */
    @Override
    public boolean equals(Object o) {
        if (o != null) {
            try {
                ILobby lobby = (ILobby) o;

                if (lobby.getName().equals(this.getName())) {
                    return true;
                }
                return false;
            } catch (RemoteException ex) {
                Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
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

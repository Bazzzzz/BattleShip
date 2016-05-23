/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.RMI;

import Battleship.Exceptions.BattleshipExceptions;
import Battleship.Interfaces.IClientManager;
import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.ILobby;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import java.beans.PropertyChangeEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import java.lang.Object;

/**
 *
 * @author sebas
 */
public class RMIClient {

    private BasicPublisher basicPublisher;

    // Binding name for a game.
    private static final String bindingName = "BattleshipInfo";

    // References to registry and GameManager
    private Registry registry = null;
    private IClientManager clientManager = null;
    private IGameManager gameManager = null;
    private ILobby lobby = null;

    // Port number and ip address.
    private final int portNumber = 9999;
    private final String ipAddress;

    private final String clientMessage = "[CLIENT MESSAGE]";

    public RMIClient(String ipAddress) throws BattleshipExceptions {
        if (!ipAddress.equals("")) {
            this.ipAddress = ipAddress;
        } else {
            throw new BattleshipExceptions("No server IP filled in.");
        }

    }

    /**
     * Connect a client to the server.
     *
     * @return True if connected.
     */
    public boolean connectToServer(String search) {
        boolean result = false;
        if (search.equals("cm")) {
            result = this.connectRMIClientManager();
        } else {
            result = connectRMI(search);
        }

        return result;
    }

    /**
     * Connect to the registry where the binding name is equal to the name of
     * the lobby.
     *
     * @return True if game manager was found.
     */
    private boolean connectRMI(String search) {

        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (registry != null) {
            if (search.equals("games")) {
                try {
                    gameManager = (IGameManager) registry.lookup("games");
                    //gameManager.addListener(this, "games");
                    System.out.println(clientMessage + " Registry lookup to: " + "games" + " succesful. \n Item found: " + gameManager);

                } catch (RemoteException ex) {
                    System.out.println(clientMessage + " Error remote lookup.");
                    Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotBoundException ex) {
                    System.out.println(clientMessage + " Error bind name.");
                    Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (gameManager != null) {
                return true;
            }

            if (search.equals("lobbies")) {
                try {
                    lobby = (ILobby) registry.lookup("lobbies");
                    //lobby.addListener(this, "lobbies");
                    System.out.println(clientMessage + " Registry lookup to: " + "lobbies" + " succesful. \n Item found: " + lobby);

                } catch (RemoteException ex) {
                    System.out.println(clientMessage + " Error remote lookup.");
                    Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotBoundException ex) {
                    System.out.println(clientMessage + " Error bind name.");
                    Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (lobby != null) {
                return true;
            }

        }

        return false;
    }

    private boolean connectRMIClientManager() {
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
            System.out.println(clientMessage + " Connected to registry: " + registry);
        } catch (RemoteException ex) {
            System.out.println(clientMessage + "Error cannot lcoate registry.");
            ex.printStackTrace();
        }
        if (registry != null) {
            try {
                clientManager = (IClientManager) registry.lookup(bindingName);
                System.out.println(clientMessage + " Registry lookup to: " + bindingName + " succesful. \n Item found: " + clientManager);
            } catch (RemoteException ex) {
                System.out.println(clientMessage + " Error remote lookup.");
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                System.out.println(clientMessage + " Error bind name.");
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (clientManager != null) {
            return true;
        }

        return false;
    }

    public IClientManager getClientManager() {
        return this.clientManager;
    }

    public IGameManager getGameManager() {
        return this.gameManager;
    }

    public ILobby getLobby() {
        return this.lobby;
    }
    /**
     * Allows client to bind 2 types to the server registry.
     * 
     * @param type Type of what the client wants to bind to the server. Type 1: "Lobby" | Type 2: "Game"
     * @param object Object that the client wants to bind to the server.
     */
    public void bindToServer(String type, Object object) {
        switch(type) {
            case "Lobby": bindToLobby(object);
                break;
            case "Game": bindToGame(object);
                break;
            default:   return;     
        }
    }
    
    private void bindToLobby(Object object) {
        try {
            lobby = (ILobby) registry.lookup("lobbies");
            RemotePropertyListener rpl = (RemotePropertyListener) object;
            lobby.addListener(rpl, "lobbies");
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void bindToGame(Object object) {
        try {
            gameManager = (IGameManager) registry.lookup("games");
            RemotePropertyListener rpl = (RemotePropertyListener) object;
            gameManager.addListener(rpl, "games");
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

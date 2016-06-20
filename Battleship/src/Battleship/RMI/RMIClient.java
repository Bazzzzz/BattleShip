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
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.Collection;

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

    private Collection<ILobby> lobbyList;
    private Collection<IGameManager> gamesList;

    // Port number and ip address.
    private final int portNumber = 9999;
    private final String ipAddress;

    private final String clientMessage = "[CLIENT MESSAGE]";

    public RMIClient(String ipAddress) throws BattleshipExceptions {

        if (!ipAddress.equals("")) {
            this.ipAddress = ipAddress;
            this.lobbyList = new ArrayList<>();
            this.gamesList = new ArrayList<>();
        } else {
            throw new BattleshipExceptions("No server IP filled in.");
        }

    }

    /**
     * Connect a client to the server.
     *
     * @param search Type of what object we are looking for in the registry |
     * "cm", "lobbyList", "gamesList", "game".
     * @param gameName Name of a specific game you are trying to look for. Null
     * if @param search is not "game".
     * @return True if connected.
     */
    public boolean connectToServer(String search, String gameName) {
        boolean result = false;
        if (search.equals("cm")) {
            result = this.connectRMIClientManager();
        } else if (search.equals("lobbyList")) {
            result = this.connectRMIList(search);
            if (this.lobbyList.isEmpty()) {
                return false;
            }
        } else if (search.equals("gamesList")) {
            result = this.connectRMIList(search);
            if (this.gamesList.isEmpty()) {
                return false;
            }
        } else if (search.equals("game")) {
            result = this.getGameManagerRMI(gameName);
            if (this.gameManager == null) {
                return false;
            }
        } else {
            result = connectRMI(search);
        }

        return result;
    }

    /**
     * Return the lobby which is searched for through RMI.
     *
     * @param selectedLobbyName lobby name that was selected in the UI, not null
     * or empty.
     * @return Found lobby or null.
     */
    public ILobby getSelectedLobbyRMI(String selectedLobbyName) {
        if (selectedLobbyName != null && !selectedLobbyName.isEmpty()) {
            /*try {
             ILobby foundLobby = (ILobby) registry.lookup(selectedLobbyName);
             }*/

            try {
                String[] registryList = registry.list();
                for (String nameLoop : registryList) {
                    if (nameLoop.endsWith("lobby")) {
                        ILobby foundLobby = (ILobby) registry.lookup(nameLoop);
                        if (foundLobby != null && foundLobby.getName().equals(selectedLobbyName)) {
                            return foundLobby;
                        }
                    }
                }
            } catch (RemoteException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * Return the game which is searched for through RMI.
     *
     * @param selectedGameName game name that was selected in the UI, not null
     * or empty.
     * @return Found game or null.
     */
    public IGameManager getSelectedGameRMI(String selectedGameName) {
        if (selectedGameName != null && !selectedGameName.isEmpty()) {
            try {
                IGameManager foundGame = (IGameManager) registry.lookup(selectedGameName);
                if (foundGame != null && foundGame.getName().equals(selectedGameName)) {
                    return foundGame;
                }
            } catch (RemoteException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        return null;
    }

    /**
     * [DEPRICATED] Connect to the registry where the binding name is equal to
     * the name of the lobby. Method produces 1 bound lobby or game manager.
     *
     * @return True if game manager or lobby was found.
     */
    @Deprecated
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

    /**
     * Connect to the registry where the binding name is in the binding list.
     * Method produces a list of lobbies and game managers.
     *
     * @param search Type of what object we are looking for in the registry |
     * "lobbies" or "games"
     * @return True if
     */
    private boolean connectRMIList(String search) {

        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (registry != null) {
            switch (search) {
                case "gamesList":
                    return this.getGamesListRMI();

                case "lobbyList":
                    return this.getLobbyListRMI();
            }
        }

        return false;
    }

    /**
     * Retrieve the game manager from the registry with @param name.
     *
     * @param name Name of the game manager we are looking for in the registry.
     * @return True if game manager was found.
     */
    private boolean getGameManagerRMI(String name) {
        try {
            if (!name.isEmpty()) {
                if (registry != null) {
                    this.gameManager = (IGameManager) registry.lookup(name);
                    System.out.println("RMIClient verbinding met game manager: " + this.gameManager.getName());
                    return true;
                }
            }
            return false;
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (NotBoundException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        // TODO: Use lookup.
        /*try {
         if (!name.isEmpty()) {
         String[] registryList = registry.list();
         if (registryList.length > 0) {
         for (String nameLoop : registryList) {
         if (nameLoop.equals(name)) {
         this.gameManager = (IGameManager) registry.lookup(name);
         return true;
         }
         }
         }
         }
         return false;
         } catch (RemoteException ex) {
         Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
         return false;
         } catch (NotBoundException ex) {
         Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
         return false;
         }*/
    }

    /**
     * Retrieve a list of lobbies from the registry.
     *
     * @return True if list is filled.
     */
    private boolean getLobbyListRMI() {
        try {
            String[] registryList = registry.list();
            if (registryList.length > 0) {
                this.lobbyList.clear();
                for (String nameLoop : registryList) {
                    if (nameLoop.endsWith("lobby")) {
                        ILobby tempLobby = (ILobby) registry.lookup(nameLoop);
                        if (tempLobby != null) {
                            this.lobbyList.add(tempLobby);
                        }
                    }
                }
                if (this.lobbyList.size() > 0) {
                    return true;
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (NotBoundException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    /**
     * Retrieve a list of game managers from the registry
     *
     * @return True if list is filled.
     */
    private boolean getGamesListRMI() {
        try {
            String[] registryList = registry.list();
            if (registryList.length > 0) {
                this.gamesList.clear();
                String nameEnding = "game";
                for (String nameLoop : registryList) {
                    if (nameLoop.endsWith(nameEnding)) {
                        IGameManager tempGameManager = (IGameManager) registry.lookup(nameEnding);
                        if (tempGameManager != null) {
                            this.gamesList.add(tempGameManager);
                        }
                    }
                }
                if (this.gamesList.size() > 0) {
                    return true;
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (NotBoundException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    /**
     * [DEPRICATED] Connects to the ClientManager.
     *
     * @return True if connected. False if not.
     */
    @Deprecated
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
                Logger
                        .getLogger(RMIClient.class
                                .getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                System.out.println(clientMessage + " Error bind name.");
                Logger
                        .getLogger(RMIClient.class
                                .getName()).log(Level.SEVERE, null, ex);
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

    public Collection<ILobby> getLobbyList() {
        return this.lobbyList;
    }

    public Collection<IGameManager> getGamesList() {
        return this.gamesList;
    }

    /**
     * Allows client to bind 2 types to the server registry.
     *
     * @param type Type of what the client wants to bind to the server. Type 1:
     * "Lobby" | Type 2: "Game" | Type 3: "LobbyUpdate" | Type 4: "GameUpdate"
     * @param object Object that the client wants to bind to the server.
     */
    public void bindToServer(String type, Object object) {
        switch (type) {
            case "Lobby":
                bindLobby(object);
                break;
            case "Game":
                bindGame(object);
                break;
            case "LobbyUpdate":
                bindUpdatedLobby(object);
                break;
            case "GameUpdate":
                bindUpdatedGame(object);
                break;
            default:
                return;
        }
    }

    /**
     * Allows client to unbind 2 types from the server registry.
     *
     * @param type Type of what the client wants to unbind from the server. Type
     * 1: "Lobby" | Type 2: "Game"
     * @param object Object that the client wants to unbind from the server.
     */
    public void unbindFromServer(String type, Object object) {
        switch (type) {
            case "Lobby":
                unbindLobby(object);
                break;
            case "Game":
                unbindGame(object);
                break;
            default:
                return;
        }
    }

    @Deprecated
    public ILobby getLobby(ILobby lobby) {

        return null;
    }

    /**
     * Bind a lobby to the registry.
     *
     * @param object Must be an ILobby object.
     */
    private void bindLobby(Object object) {
        ILobby tempLobby = (ILobby) object;
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (registry != null) {
            try {
                registry.rebind(tempLobby.getName(), tempLobby);
                System.out.println("[SERVER MESSAGE] Lobby bound:" + tempLobby);
            } catch (RemoteException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Bind a game manager to the registry.
     *
     * @param object Must be an IGameManager object.
     */
    private void bindGame(Object object) {
        IGameManager tempGame = (IGameManager) object;
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (registry != null) {
            try {
                registry.rebind(tempGame.getName(), tempGame);
                System.out.println("[SERVER MESSAGE] Game bound:" + tempGame);
            } catch (RemoteException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Bind a lobby to the registry.
     *
     * @param object Muse be an ILobby object.
     */
    private void bindUpdatedLobby(Object object) {
        ILobby lobby = (ILobby) object;
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (registry != null) {
            try {
                registry.rebind(lobby.getName(), lobby);
                System.out.println("[SERVER MESSAGE] Lobby rebound after update:" + lobby);
            } catch (RemoteException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void bindUpdatedGame(Object object) {
        IGameManager game = (IGameManager) object;
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (registry != null) {
            try {
                registry.rebind(game.getName(), game);
                System.out.println("[SERVER MESSAGE] Game rebound after update:" + game);
            } catch (RemoteException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Unbind a lobby from the registry.
     *
     * @param object Must be an ILobby object
     */
    private void unbindLobby(Object object) {
        ILobby lobby = (ILobby) object;
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (registry != null) {
            try {
                registry.unbind(lobby.getName());
            } catch (RemoteException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Unbind a game from the registry.
     *
     * @param object Must be an IGameManager object
     */
    private void unbindGame(Object object) {
        IGameManager game = (IGameManager) object;
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (registry != null) {
            try {
                registry.unbind(game.getName());
            } catch (RemoteException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * [DEPRICATED] Try to bind the lobby using the RemotePropertyListener.
     *
     * @param object
     */
    @Deprecated
    private void bindToLobby(Object object) {
        try {
            lobby = (ILobby) registry.lookup("lobbies");
            RemotePropertyListener rpl = (RemotePropertyListener) object;
            lobby.addListener(rpl, "lobbies");

        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(RMIClient.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * [DEPRICATED] Try to bind game manager using the RemotePropertyListener.
     *
     * @param object
     */
    @Deprecated
    private void bindToGame(Object object) {
        try {
            gameManager = (IGameManager) registry.lookup("games");
            RemotePropertyListener rpl = (RemotePropertyListener) object;
            gameManager.addListener(rpl, "games");

        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(RMIClient.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * [DEPRICATED] Retrieve a lobby from the registry.
     *
     * @param selectedLobby lobby that was selected in the UI, not null.
     * @return THe lobby that was found or null.
     */
    @Deprecated
    public ILobby getSelectedLobby(ILobby selectedLobby) {
        if (selectedLobby != null) {
            for (ILobby tempLobby : this.lobbyList) {
                if (tempLobby.equals(selectedLobby)) {
                    return tempLobby;
                }
            }
        }
        return null;
    }

}

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
import fontys.observer.RemotePropertyListener;
import java.beans.PropertyChangeEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author sebas
 */
public class RMIClient {

    // Binding name for a game.
    private static final String bindingName = "BattleshipInfo";

    // References to registry and GameManager
    private Registry registry = null;
    private IClientManager clientManager = null;
    
    
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
    public boolean connectToServer() {
        boolean result = connectRMI();

        return result;
    }

    private boolean connectRMI() {
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
}

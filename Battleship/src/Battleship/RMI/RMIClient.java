/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.RMI;

import Battleship.Exceptions.BattleshipExceptions;
import Battleship.Interfaces.IGameManager;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author sebas
 */
public class RMIClient {
    // Binding name for a game.
    private static final String bindingName = "BattleshipInfo";
    
    // References to registry and GameManager
    private Registry registry = null;
    private IGameManager gameManager = null;
    
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
        }
        catch (RemoteException ex) {
            System.out.println(clientMessage + "Error cannot lcoate registry.");
        }
        if (registry != null) {
            try {
                gameManager = (IGameManager) registry.lookup(bindingName);
                System.out.println(clientMessage + " Registry lookup to: " + bindingName + " succesful. \n Item found: " + gameManager);
            } catch (RemoteException ex) {
                System.out.println(clientMessage + " Error remote lookup.");
            } catch (NotBoundException ex) {
                System.out.println(clientMessage + " Error bind name.");
            }
        }
        if (gameManager != null) {
            return true;
        }
        return false;
    }
    /**
     * Get the GameManager that was found.
     * @return 
     */
    public IGameManager getGameManager() {
        return this.gameManager;
    }
}

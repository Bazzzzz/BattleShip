/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Server;

import Battleship.Domain.GameManager;
import Battleship.Domain.Lobby;
import Battleship.Interfaces.IClientManager;
import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.ILobby;
import Battleship.RMI.ClientManagerOld;
import Battleship.RMI.ServerLobby;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sebas
 */
public class RMIServer {

    // Set port number
    private static final int portNumber = 9999;

    // Set binding name for battleship administration
    private static final String bindingName = "BattleshipInfo";

    // References to registry, game manager and lobby
    private Registry registry = null;
    private IClientManager clientManager = null;
    private IGameManager gameManager = null;

    private ILobby lobby = null;

    private final String serverMessage = "[SERVER MESSAGE]";

    public RMIServer() {
        // Create Client Manager
        /*try {
         clientManager = new ClientManagerOld();
         System.out.println(serverMessage + " CM created");
         } catch (RemoteException ex) {
         System.out.println(serverMessage + " Error creating CM.");
         Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
         }
         */
        // Create Game Manager
        try {
            gameManager = new GameManager();
            System.out.println(serverMessage + " GM created.");
        } catch (RemoteException ex) {
            System.out.println(serverMessage + " Error creating GM.");
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            lobby = new Lobby("Test lobby");
        } catch (RemoteException ex) {
            System.out.println(serverMessage + " Error creating lobby.");
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Create registry
        try {
            registry = LocateRegistry.createRegistry(portNumber);
            System.out.println(serverMessage + " Server created on port: " + portNumber);
        } catch (RemoteException ex) {
            System.out.println(serverMessage + " Error locating registry.");
            ex.printStackTrace();
        }
        // Bind single CM.
        /*
         try {
         registry.rebind(bindingName, clientManager);
         System.out.println(serverMessage + " Server bound to: " + bindingName + ", registry: " + registry + "\n Object in registry: " + clientManager.toString());
         } catch (RemoteException ex) {
         System.out.println(serverMessage + " Error binding to registry.");
         ex.printStackTrace();
         }
         */
        // Bind GM.
        try {
            registry.rebind("game", gameManager);
            System.out.println(serverMessage + " Server bound to: " + bindingName + ", registry: " + registry + "\n Object in registry: " + gameManager.toString());
        } catch (RemoteException ex) {
            System.out.println(serverMessage + " Error binding to registry.");
            ex.printStackTrace();
        }
        // Bind lobby
        try {
            registry.rebind("lobby", lobby);
            System.out.println(serverMessage + " Server bound to: " + bindingName + ", registry: " + registry + "\n Object in registry: " + lobby.toString());
        } catch (RemoteException ex) {
            System.out.println(serverMessage + " Error binding to registry.");
            ex.printStackTrace();
        }
        System.out.println(serverMessage + " Server IP Address: ");
        printIPAddresses();
        System.out.println(serverMessage + " Server set, waiting for clients.");

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    for(String stringLoop : registry.list()) {
                        System.out.println(stringLoop.toString());
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        Timer serverTimer = new Timer();
        serverTimer.scheduleAtFixedRate(timerTask, 0, 30000);
    }

    // Print IP addresses and network interfaces
    private void printIPAddresses() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("Server: IP Address: " + localhost.getHostAddress());
            // Just in case this host has multiple IP addresses....
            InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
            if (allMyIps != null && allMyIps.length > 1) {
                System.out.println("Server: Full list of IP addresses:");
                for (InetAddress allMyIp : allMyIps) {
                    System.out.println("    " + allMyIp);
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server: Cannot get IP address of local host");
            System.out.println("Server: UnknownHostException: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        RMIServer server = new RMIServer();
    }

}

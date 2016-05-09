/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Server;

import Battleship.Domain.GameManager;
import Battleship.Interfaces.IGameManager;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author sebas
 */
public class RMIServer {
    // Set port number
    private static final int portNumber = 9999;

    // Set binding name for student administration
    private static final String bindingName = "BattleshipInfo";

    // References to registry and student administration
    private Registry registry = null;
    private IGameManager gameManager = null;
    
    private final String serverMessage = "[SERVER MESSAGE]";
    
    public RMIServer() {
        // Create GameManager
        try {
            gameManager = new GameManager();
            System.out.println(serverMessage + " GameManager created");
        } catch(RemoteException ex) {
            System.out.println(serverMessage + " Error creating gamemanager.");
            ex.printStackTrace();
        }
        // Create registry
        try {
            registry = LocateRegistry.createRegistry(portNumber);
            System.out.println(serverMessage + " Server created on port: " + portNumber);
        } catch(RemoteException ex) {
            System.out.println(serverMessage + " Error locating registry.");
            ex.printStackTrace();
        }
        // Bind gamemanager to registry with bindingName
        try {
            registry.rebind(bindingName, gameManager);
            System.out.println(serverMessage + " Server bound to: " + bindingName + ", registry: " + registry + "\n Object in registry: " + gameManager);
            System.out.println(serverMessage + " Server IP Address: ");
            printIPAddresses();
            System.out.println(serverMessage + "Server set, waiting for clients.");
        } catch (RemoteException ex) {
            System.out.println(serverMessage + " Error binding to registry.");
            ex.printStackTrace();
        }
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

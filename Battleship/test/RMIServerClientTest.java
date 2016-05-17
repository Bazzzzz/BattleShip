/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Battleship.Domain.Lobby;
import Battleship.Domain.Player;
import Battleship.Exceptions.BattleshipExceptions;
import Battleship.Interfaces.IClientManager;
import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.ILobby;
import Battleship.Interfaces.IPlayer;
import Battleship.RMI.RMIClient;
import Battleship.Server.RMIServer;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sebas
 */
public class RMIServerClientTest {

    private RMIClient client;
    private IPlayer playerBas;
    private IPlayer playerSukh;
    private IPlayer playerRandom;

    public RMIServerClientTest() {
        client = null;
        playerBas = new Player("Bas");
        playerSukh = new Player("Sukh");
        playerRandom = new Player("Random");
    }

    @Test
    public void TestConnection() throws BattleshipExceptions {
        client = new RMIClient("localhost");
        assertTrue("Test connection of client to server", client.connectToServer());
    }

    @Test
    public void TestGetClientManager() throws BattleshipExceptions, RemoteException {
        client = new RMIClient("localhost");
        client.connectToServer();
        String message = "[TestGetClientManager] ";
        assertNotNull(message + "Client retrieved the client manager object", client.getClientManager());
    }

    @Test
    public void TestAddLobbyToCM() throws BattleshipExceptions, RemoteException {
        client = new RMIClient("localhost");
        client.connectToServer();
        String message = "[TestAddLobbyToCM] ";

        IClientManager cm = client.getClientManager();
        cm.removeAllLobbies();
        int lobbyCount = cm.getLobbies().size();

        assertTrue(message + "Initial Lobby count is 0.", lobbyCount == 0);

        ILobby firstLobby = new Lobby("FirstLobby");
        assertTrue(message + "First Lobby added", cm.addLobby(firstLobby));

        lobbyCount = cm.getLobbies().size();

        assertEquals(message + "After adding a first lobby count is 1.", 1, lobbyCount);

        ILobby secondLobby = new Lobby("SecondLobby");
        assertTrue(message + "Second Lobby added", cm.addLobby(secondLobby));

        lobbyCount = cm.getLobbies().size();

        assertEquals(message + "After adding a second lobby count is 2.", 2, lobbyCount);
    }

    @Test
    public void TestAddPlayerToLobbyFromCM() throws BattleshipExceptions, RemoteException {
        client = new RMIClient("localhost");
        client.connectToServer();
        String message = "[TestAddPlayerToLobbyFromCM] ";
        IClientManager cm = client.getClientManager();
        cm.removeAllLobbies();

        // Add player to first lobby.
        ILobby firstLobby = new Lobby("FirstLobby");
        firstLobby.addPlayer(playerBas);

        cm.addLobby(firstLobby);
        int playersCount = firstLobby.getPlayers().size();

        assertEquals(message + "Players in first lobby after adding 1 should be 1.", 1, playersCount);
        playersCount = 0; // Reset player count.

        // Add player to second lobby.
        ILobby secondLobby = new Lobby("SecondLobby");
        secondLobby.addPlayer(playerRandom);

        cm.addLobby(secondLobby);
        playersCount = cm.findLobbyByPlayer(playerRandom.getName()).getPlayers().size();
        assertEquals(message + "Players in the first lobby should still be 1, after adding a player to lobby 2.", 1, playersCount);

        playersCount = secondLobby.getPlayers().size();
        assertEquals(message + "Players in the second lobby after adding 1 should be 1.", 1, playersCount);

        firstLobby.addPlayer(playerSukh);
        cm.updateLobby(firstLobby);
        playersCount = cm.findLobbyByPlayer(playerSukh.getName()).getPlayers().size();
        assertEquals(message + "Player added to first lobby", 2, playersCount);
    }

    @Test
    public void TestRemovePlayerFromLobbyInCM() throws BattleshipExceptions, RemoteException {
        client = new RMIClient("localhost");
        client.connectToServer();
        String message = "[TestRemovePlayerFromLobbyInCM] ";
        IClientManager cm = client.getClientManager();
        cm.removeAllLobbies();

        // Add player to first lobby.
        ILobby firstLobby = new Lobby("FirstLobby");
        firstLobby.addPlayer(playerBas);

        cm.addLobby(firstLobby);  
        
        // Add player to second lobby.
        ILobby secondLobby = new Lobby("SecondLobby");
        secondLobby.addPlayer(playerRandom);

        cm.addLobby(secondLobby);
        
        // Remove player from first lobby.
        firstLobby.removePlayerFromLobby(playerBas);
        int playersCount = firstLobby.getPlayers().size();
        assertEquals(message + "Player removed from the first lobby.", 0, playersCount);
        
    }

    @Test
    public void TestCreateGameManagerFromLobby() throws BattleshipExceptions, RemoteException {
        client = new RMIClient("localhost");
        client.connectToServer();
        String message = "[TestCreateGameManagerFromLobby] ";
        
        IClientManager cm = client.getClientManager();
        cm.removeAllLobbies();

        // Add player to first lobby.
        ILobby firstLobby = new Lobby("FirstLobby");
        firstLobby.addPlayer(playerBas);
        cm.addLobby(firstLobby);  
        
        firstLobby.addPlayer(playerRandom);
        cm.updateLobby(firstLobby);
        
        ILobby lobbyTest = cm.findLobbyByName(firstLobby.getName());
        IGameManager gm = lobbyTest.createGameManager();
        
        int playersAmount = gm.getPlayers().size();
        
        assertEquals(message + "Players in the game manager 2.", 2, playersAmount);
        
        // Add player to second lobby and test createGameManager on 1 player.
        ILobby secondLobby = new Lobby("SecondLobby");
        secondLobby.addPlayer(playerSukh);
        
        cm.addLobby(secondLobby);
        
        gm = secondLobby.createGameManager();
        assertNull(message + "Unable to make GM with only 1 player in lobby.", gm);
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Battleship.Domain.Lobby;
import Battleship.Domain.Overview;
import Battleship.Domain.Player;
import Battleship.Domain.Ship;
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
        //assertTrue("Test connection of client to server", client.connectToServer("games", null));
        //assertTrue("Test connection of client to server", client.connectToServer("lobbies", null));
    }

    @Test
    public void TestAddLobbyToServer() throws BattleshipExceptions, RemoteException {
        client = new RMIClient("localhost");
        ILobby lobby = new Lobby("TestLobby");
        lobby.addPlayer(playerBas);
        client.bindToServer("Lobby", lobby);

        boolean result = client.getSelectedLobbyRMI(lobby.getName()).getName().equals(lobby.getName());
        String message = "[TestAddLobbyToServer] ";
        assertTrue(message + "Client added lobby to server.", result);
    }

    @Test
    public void TestCreateGameMangerFromLobby() throws BattleshipExceptions, RemoteException {
        client = new RMIClient("localhost");
        ILobby lobby = new Lobby("TestLobby");
        lobby.addPlayer(playerBas);
        client.bindToServer("Lobby", lobby);

        lobby = client.getSelectedLobbyRMI(lobby.getName());
        lobby.addPlayer(playerSukh);

        IGameManager game = lobby.createGameManager();
        String message = "[TestCreateGameMangerFromLobby] ";
        assertNotNull(message + "Client lobby created game manager on the server.", game);

        assertEquals(message + "Client lobby has 2 players in it.", 2, game.getPlayers().size());
        
        client.bindToServer("LobbyUpdate", lobby);
        
        client.connectToServer("game", game.getName());
        
        assertNotNull(message + "Client can retrieve game after it has been bound", client.getGameManager());
        
    }
    
    @Test
    public void TestUseGameManagerFromServer() throws BattleshipExceptions, RemoteException {
        client = new RMIClient("localhost");
        ILobby lobby = new Lobby("TestLobby");
        lobby.addPlayer(playerBas);
        client.bindToServer("Lobby", lobby);

        lobby = client.getSelectedLobbyRMI(lobby.getName());
        lobby.addPlayer(playerSukh);

        IGameManager game = lobby.createGameManager();
        client.bindToServer("Game", game);
        
        client.connectToServer("Game", game.getName());
        
        game.buildOverviewsForPlayers();
        Overview overview = game.getOverviews().get(0);
        
        Overview overviewPlayer1 = game.getPlayers().get(0).getPlayer();
        Overview overviewPlayer2 = game.getPlayers().get(1).getPlayer();
        Overview overviewPlayer1Opp = game.getPlayers().get(0).getOpponent();
        Overview overviewPlayer2Opp = game.getPlayers().get(1).getOpponent();
        int[] location = new int[2];
        location[0] = 5;
        location[1] = 4;
        game.placeShip(playerBas, location, 3, 0);
        game.placeShip(playerSukh, location, 4, 1);
        
        int size = game.getPlayers().size();
        Ship ship = game.getPlayers().get(0).getPlayer().getShipOnLocation(location);
        assertNotNull("Ship is found where expected after placement.", ship);
        ship = game.getPlayers().get(1).getPlayer().getShipOnLocation(location);
        
    }
    /*
     @Test
     public void TestGetClientManager() throws BattleshipExceptions, RemoteException {
     client = new RMIClient("localhost");
     client.connectToServer("cm", null);
     String message = "[TestGetClientManager] ";
     assertNotNull(message + "Client retrieved the client manager object", client.getClientManager());
     }

     @Test
     public void TestAddLobbyToCM() throws BattleshipExceptions, RemoteException {
     client = new RMIClient("localhost");
     client.connectToServer("cm", null);
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
     client.connectToServer("cm", null);
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
     client.connectToServer("cm", null);
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
     firstLobby.removePlayerFromLobby(playerBas.getName());
     int playersCount = firstLobby.getPlayers().size();
     assertEquals(message + "Player removed from the first lobby.", 0, playersCount);

     }

     @Test
     public void TestCreateGameManagerFromLobby() throws BattleshipExceptions, RemoteException {
     client = new RMIClient("localhost");
     client.connectToServer("cm", null);
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

     @Test
     public void TestGetLobby() throws BattleshipExceptions {
     client = new RMIClient("localhost");
     client.connectToServer("lobbies", null);
     String message = "[TestGetLobby] ";

     assertNotNull(message + "Client returned a remote lobby.", client.getLobby());
     }

     @Test
     public void TestAddPlayerToLobby() throws BattleshipExceptions, RemoteException {
     client = new RMIClient("localhost");
     client.connectToServer("lobbies", null);
     String message = "[TestAddPlayerToLobby] ";

     client.getLobby().addPlayer(playerBas);
     assertEquals("Players in lobby is 1.", 1, client.getLobby().getPlayers().size());

     client.getLobby().addPlayer(playerSukh);
     assertEquals("Players in lobby is 2.", 2, client.getLobby().getPlayers().size());
     }
    
     @Test
     public void TestCreateGameManagerFromLobbyObject() throws BattleshipExceptions, RemoteException {
     client = new RMIClient("localhost");
     client.connectToServer("lobbies", null);
     String message = "[TestCreateGameManagerFromLobbyObject] ";

     client.getLobby().addPlayer(playerBas);
     client.getLobby().addPlayer(playerSukh);
        
     assertNotNull(message + "Create game manager.",client.getLobby().createGameManager());
        
     assertEquals(message + "PlayerBas is player 1. ", playerBas, client.getLobby().getPlayers().get(0));
     assertEquals(message + "PlayerSukh is player 2. ", playerSukh, client.getLobby().getPlayers().get(1));
     }
    
     @Test
     public void TestUseGameManagerObjectFromLobbyObject() throws BattleshipExceptions, RemoteException {
     client = new RMIClient("localhost");
     client.connectToServer("lobbies", null);
     String message = "[TestUseGameManagerObjectFromLobbyObject] ";

     client.getLobby().addPlayer(playerBas);
     client.getLobby().addPlayer(playerSukh);
        
     client.getLobby().createGameManager();
     IGameManager gm = client.getLobby().getGameManager();
     //gm.buildOverviewsForPlayers(client.getLobby().getPlayers().get(0), client.getLobby().getPlayers().get(1));
     gm.getOverviews().get(0).printBoard();
     }
     */
}

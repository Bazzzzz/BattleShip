/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Battleship.Domain.Player;
import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.IPlayer;
import Battleship.RMI.RMIClient;
import Battleship.Server.RMIServer;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sebas
 */
public class RMIServerClientTest {

    private RMIClient client;

    public RMIServerClientTest() {
        client = null;
    }
    @Test
    public void TestConnection() {
        client = new RMIClient("localhost");
        assertTrue("Test connection of client to server", client.connectToServer());
    }
    @Test
    public void TestGetGameManager() {
        client = new RMIClient("localhost");
        client.connectToServer();
        assertNotNull("Client retrieved a game manager.", client.getGameManager());
    }
    @Test
    public void TestAddRemoveOnePlayerToGM() throws RemoteException {
        client = new RMIClient("localhost");
        client.connectToServer();
        IGameManager gm = client.getGameManager();
        int numberOfPlayers = gm.getPlayers().size();
        assertTrue("Number of players on creation is 0.", numberOfPlayers == 0);
        
        IPlayer player = new Player("Bas");
        gm.addPlayer(player);
        IPlayer playerFound = gm.getPlayers().get(0);
        boolean result = playerFound.getName().equals(player.getName());
        numberOfPlayers = gm.getPlayers().size();
        assertTrue("Player added to game.", result);
        assertTrue("Number of players after add is 1.", numberOfPlayers == 1);
        gm.removePlayer(player);
        numberOfPlayers = gm.getPlayers().size();
        
        assertTrue("Number of players after remove is 0.", numberOfPlayers == 0);
    }
    @Test 
    public void TestAddRemoveTwoPlayersToGM() throws RemoteException {
        client = new RMIClient("localhost");
        client.connectToServer();
        IGameManager gm = client.getGameManager();
        IPlayer player1 = new Player("Bas");
        gm.addPlayer(player1);
        
        IPlayer player2 = new Player("Sukh");
        gm.addPlayer(player2);
        
        int numberOfPlayers = gm.getPlayers().size();
        assertTrue("Number of players 2 after adding 2 players.", numberOfPlayers == 2);
        
        gm.removePlayer(player1);
        
        boolean result = true;
        Iterator<IPlayer> playersItr = gm.getPlayers().iterator();
        while(playersItr.hasNext()) {
            IPlayer tempPlayer = (IPlayer) playersItr.next();
            if (tempPlayer.getName().equals(player1.getName())) {
                result = false;
            }
        }
        numberOfPlayers = gm.getPlayers().size();
        assertTrue("Pplayer1 was removed from the list of players", result);
        assertTrue("Number of players 1 after removal of 1 player.", numberOfPlayers == 1);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void TestAdd3PlayersToGM() throws RemoteException {
        client = new RMIClient("localhost");
        client.connectToServer();
        IGameManager gm = client.getGameManager();
        IPlayer player1 = new Player("Bas");
        gm.addPlayer(player1);
        
        IPlayer player2 = new Player("Sukh");
        gm.addPlayer(player2);
        
        IPlayer player3 = new Player("Test");
        gm.addPlayer(player3);
    }
}

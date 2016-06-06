/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Battleship.Domain.GameManager;
import Battleship.Domain.Overview;
import Battleship.Domain.Player;
import Battleship.Domain.Ship;
import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.IPlayer;
import java.rmi.RemoteException;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sebas
 */
public class TestGame_InConsole {

    IGameManager manager;
    IPlayer player1;
    IPlayer player2;

    public TestGame_InConsole() throws RemoteException {
        manager = new GameManager();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        System.out.println(manager);
    }

    /*@Test
    public void testPrintBoardOriginal() {
        manager.buildOverviewsForPlayers(player1, player2);
        List<Overview> overviews = manager.getOverviews();
        Overview overview = overviews.get(0);
        overview.printBoard();
    }*/
/*
    @Test
    public void testPrintBoardAfterShipPlacement() {
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers(player1, player2);

        manager.placeShip(player1, locationShip, 3, 0);
        
        System.out.println("Players own board. First turn. Placed on 4,4 size 3");
        player1.getPlayer().printBoard();
        System.out.println("Players opponent board.First turn.");
        player2.getOpponent().printBoard();
        
        locationShip[0] = 7;
        locationShip[1] = 8;
        
        manager.placeShip(player1, locationShip, 6, 1);
        
        System.out.println("Players own board. Second turn. Placed on 7,8 size 6");
        player1.getPlayer().printBoard();
        System.out.println("Players opponent board. Second turn.");
        player2.getOpponent().printBoard();
    }
*/
    
    @Test
    public void testPrintBoardAfterTorpedoOnShip() throws RemoteException {
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.addPlayer(player1);
        //assertNull("[TEST] Add the same player to a game twice.", manager.addPlayer(player1));
        manager.addPlayer(player2);
        manager.buildOverviewsForPlayers();
        
        
        manager.placeShip(player2, locationShip, 3, 0);
        
        System.out.println("Players own board. After Place Ship.");
        player1.getPlayer().printBoard();
        System.out.println("Players opponent board. After Place Ship.");
        player1.getOpponent().printBoard();
        
        int[] locationTorpedo = new int[2];
        locationTorpedo[0] = 4;
        locationTorpedo[1] = 4;
        manager.fireTorpedo(player1, "TorpedoNormal", locationTorpedo);
        
        System.out.println("Players own board. After Torpedo Fire.");
        player1.getPlayer().printBoard();
        System.out.println("Players opponent board. After Torpedo Fire.");
        player1.getOpponent().printBoard();
        
        assertTrue("Location has Torpedo", player1.getOpponent().locationHasTorpedo(locationTorpedo));
    }
}

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

    public TestGame_InConsole() {
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

    @Test
    public void testPrintBoardAfterShipPlacement() {
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers(player1, player2);

        manager.placeShip(player1, locationShip, 4, 0);
        
        System.out.println("Players own board. First turn.");
        player1.getPlayer().printBoard();
        System.out.println("Players opponent board.First turn.");
        player2.getOpponent().printBoard();
        
        locationShip[0] = 7;
        locationShip[1] = 8;
        
        manager.placeShip(player1, locationShip, 6, 1);
        
        System.out.println("Players own board. Second turn.");
        player1.getPlayer().printBoard();
        System.out.println("Players opponent board. Second turn.");
        player2.getOpponent().printBoard();
    }
}

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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sebas
 */
public class TestGameManager {

    IGameManager manager;
    IPlayer player1;
    IPlayer player2;

    public TestGameManager() {
        manager = new GameManager();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
    }

    @Test
    public void TestAddPlayerSuccess() {
        manager.addPlayer(player1);
        manager.addPlayer(player2);

        assertEquals("Player1 added to gamemanager", player1, manager.getPlayers().get(0));
        assertEquals("Player2 added to gamemanager", player2, manager.getPlayers().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestAddPlayerAlreadyInList() {
        manager.addPlayer(player1);
        manager.addPlayer(player1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestAddPlayerNullPlayer() {
        IPlayer actual = manager.addPlayer(null);
    }

    @Test
    public void TestPlaceShipSuccessHorizontal() {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers(player1, player2);

        manager.placeShip(player1, locationShip, 3, 0);
        int actualAmountOfShips = manager.getPlayers().get(0).getPlayer().amountOfShips();

        assertEquals("Amount of ships increased after place ship.", 1, actualAmountOfShips);
        int[] tempLocation = new int[2];
        Ship locatedShip = null;
        for (int i = 4; i > 7; i++) {
            tempLocation[0] = 4;
            tempLocation[0] = i;
            locatedShip = manager.getPlayers().get(0).getPlayer().getShipOnLocation(tempLocation);
            assertTrue("Ship is located where we placed it.", locatedShip.getLocationStart()[0] == locationShip[0]
                    && locatedShip.getLocationStart()[1] == locationShip[1]);
        }

    }

    @Test
    public void TestPlaceShipSuccessVertical() {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers(player1, player2);

        manager.placeShip(player1, locationShip, 3, 1);
        int actualAmountOfShips = manager.getPlayers().get(0).getPlayer().amountOfShips();

        assertEquals("Amount of ships increased after place ship.", 1, actualAmountOfShips);
        int[] tempLocation = new int[2];
        Ship locatedShip = null;
        for (int i = 4; i > 7; i++) {
            tempLocation[0] = i;
            tempLocation[0] = 4;
            locatedShip = manager.getPlayers().get(0).getPlayer().getShipOnLocation(tempLocation);
            assertTrue("Ship is located where we placed it.", locatedShip.getLocationStart()[0] == locationShip[0]
                    && locatedShip.getLocationStart()[1] == locationShip[1]);
        }
    }

    @Test
    public void TestPlaceShipOnInvalidLocation() {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        int shipLength = 3;

        manager.buildOverviewsForPlayers(player1, player2);
        for (int i = 0; i <= Overview.BOARDWIDTH; i++) {
            locationShip[0] = 0;
            locationShip[1] = i;
            boolean actual = manager.placeShip(player1, locationShip, shipLength, 1);
            assertFalse("Can't place ship on the top row.", actual);
        }
        for (int i = 0; i <= Overview.BOARDHEIGHT - shipLength; i++) {
            locationShip[0] = i;
            locationShip[1] = 0;
            boolean actual = manager.placeShip(player1, locationShip, shipLength, 0);
            assertFalse("Can't place ship on the left column", actual);
        }
        for (int i = 0; i < Overview.BOARDWIDTH - shipLength; i++) {
            locationShip[0] = Overview.BOARDHEIGHT;
            locationShip[1] = i;
            boolean actual = manager.placeShip(player1, locationShip, shipLength, 0);
            assertFalse("Can't place ship on the bottom row.", actual);
        }
        for (int i = 0; i < Overview.BOARDHEIGHT - shipLength; i++) {
            locationShip[0] = i;
            locationShip[1] = Overview.BOARDWIDTH;
            boolean actual = manager.placeShip(player1, locationShip, shipLength, 1);
            assertFalse("Can't place ship on the right column", actual);
        }
    }

    @Test
    public void TestPlaceShipOnOtherShip() {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers(player1, player2);

        manager.placeShip(player1, locationShip, 3, 0);

        locationShip[0] = 4;
        locationShip[1] = 4;
        boolean result = manager.placeShip(player1, locationShip, 4, 0);
        assertFalse("Ships on top of each other horizontally.", result);

        locationShip[0] = 4;
        locationShip[1] = 5;
        result = manager.placeShip(player1, locationShip, 4, 0);
        assertFalse("Ships on top of each other horizontally.", result);

        locationShip[0] = 4;
        locationShip[1] = 6;
        result = manager.placeShip(player1, locationShip, 4, 0);
        assertFalse("Ships on top of each other horizontally.", result);

        locationShip[0] = 4;
        locationShip[1] = 4;
        result = manager.placeShip(player1, locationShip, 4, 1);
        assertFalse("Ships on top of each other vertically.", result);

        locationShip[0] = 3;
        locationShip[1] = 4;
        result = manager.placeShip(player1, locationShip, 4, 1);
        assertFalse("Ships on top of each other vertically.", result);

        locationShip[0] = 2;
        locationShip[1] = 4;
        result = manager.placeShip(player1, locationShip, 4, 1);
        assertFalse("Ships on top of each other vertically.", result);

        // New valid ship.
        locationShip[0] = 8;
        locationShip[1] = 8;
        result = manager.placeShip(player1, locationShip, 4, 0);

        locationShip[0] = 6;
        locationShip[1] = 8;
        result = manager.placeShip(player1, locationShip, 6, 1);
        assertFalse("Ships on top of each other vertically.", result);

        locationShip[0] = 6;
        locationShip[1] = 9;
        result = manager.placeShip(player1, locationShip, 6, 1);
        assertFalse("Ships on top of each other vertically.", result);

        locationShip[0] = 6;
        locationShip[1] = 10;
        result = manager.placeShip(player1, locationShip, 6, 1);
        assertFalse("Ships on top of each other vertically.", result);

        locationShip[0] = 6;
        locationShip[1] = 11;
        result = manager.placeShip(player1, locationShip, 6, 1);
        assertFalse("Ships on top of each other vertically.", result);
    }

    @Test
    public void TestFireTorpedoSucess() {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers(player1, player2);

        manager.placeShip(player2, locationShip, 3, 0);

        // Fire torpedo in the water.
        int[] locationTorpedo = new int[2];
        locationTorpedo[0] = 8;
        locationTorpedo[1] = 8;

        manager.fireTorpedo(player1, "Normal Torpedo", locationTorpedo);
        int expected = 1;
        int actual = manager.getTorpedos().size();

        assertEquals("Torpedo added to list.", expected, actual);

        boolean torpedoLocated = manager.getPlayers().get(0).getOpponent().locationHasTorpedo(locationTorpedo);
        assertTrue("Torpedo is located on the players opponents board where expected.", torpedoLocated);

        // Fire torpedo at ship.
        locationTorpedo[0] = 4;
        locationTorpedo[1] = 4;

        manager.fireTorpedo(player1, "Normal Torpedo", locationTorpedo);
        expected++;
        actual = manager.getTorpedos().size();
        assertEquals("Second torpedo added to the list.", expected, actual);

        int expectedDamage = 1;
        Ship damagedShip = manager.getPlayers().get(0).getOpponent().getShipOnLocation(locationTorpedo);
        assertEquals("Ship should be damaged after first torpedo hit", expectedDamage, damagedShip.getAmountHit());

        // Fire torpedo at ship second time.
        locationTorpedo[0] = 4;
        locationTorpedo[1] = 5;

        manager.fireTorpedo(player1, "Normal Torpedo", locationTorpedo);
        expected++;
        actual = manager.getTorpedos().size();
        assertEquals("Third torpedo added to the list.", expected, actual);

        expectedDamage = 2;
        damagedShip = manager.getPlayers().get(0).getOpponent().getShipOnLocation(locationTorpedo);
        assertEquals("Ship should be damaged after first torpedo hit", expectedDamage, damagedShip.getAmountHit());
        
        // Fire torpedo at ship and destroy it.
        locationTorpedo[0] = 4;
        locationTorpedo[1] = 6;
        
        manager.fireTorpedo(player1, "Normal Torpedo", locationTorpedo);
        expected++;
        actual = manager.getTorpedos().size();
        assertEquals("Third torpedo added to the list.", expected, actual);
        
        expectedDamage = 3;
        damagedShip = manager.getPlayers().get(0).getOpponent().getShipOnLocation(locationTorpedo);
        assertEquals("Ship should be damaged after first torpedo hit", expectedDamage, damagedShip.getAmountHit());
    }
    @Test
    public void TestFireTorpedoAtDamagedPartOfShip() {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers(player1, player2);

        manager.placeShip(player2, locationShip, 3, 0);
        
        // Fire torpedo at ship.
        int[] locationTorpedo = new int[2];
        locationTorpedo[0] = 4;
        locationTorpedo[1] = 4;

        manager.fireTorpedo(player1, "Normal Torpedo", locationTorpedo);
        
        boolean actual = manager.fireTorpedo(player1, "Torpedo", locationTorpedo);
        assertFalse("Already fired a torpedo at this location", actual);
        
        locationTorpedo[0] = 6;
        locationTorpedo[1] = 5;
        
        actual = manager.fireTorpedo(player1, "Normal Torpedo", locationTorpedo);
        assertTrue("New torpedo fired at location", actual);
        
    }
}

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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sebas
 */
public class GameManagerTest {

    IGameManager manager;
    IPlayer player1;
    IPlayer player2;

    public GameManagerTest() throws RemoteException {
        manager = new GameManager();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
    }

    @Test
    public void TestAddPlayerSuccess() throws RemoteException {
        manager.addPlayer(player1);
        manager.addPlayer(player2);

        assertEquals("Player1 added to gamemanager", player1, manager.getPlayers().get(0));
        assertEquals("Player2 added to gamemanager", player2, manager.getPlayers().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestAddPlayerAlreadyInList() throws RemoteException {
        manager.addPlayer(player1);
        manager.addPlayer(player1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestAddPlayerNullPlayer() throws RemoteException {
        IPlayer actual = manager.addPlayer(null);
    }

    @Test
    public void TestRemovePlayerSuccess() throws RemoteException {
        manager.addPlayer(player1);
        manager.addPlayer(player2);

        manager.removePlayer(player1);

        int actualAmountOfPlayers = manager.getPlayers().size();
        int expectedAmountOfPlayers = 1;

        assertEquals("Player1 removed from players list -> check list size", actualAmountOfPlayers, expectedAmountOfPlayers);

        boolean result = true;

        manager.addPlayer(player1);
        manager.removePlayer(player1);
        for (IPlayer player : manager.getPlayers()) {
            if (player.equals(player1)) {
                result = false;
                break;
            }
        }
        assertTrue("Player1 removed from players list -> player not found in list", result);

        result = manager.removePlayer(null);

        assertFalse("Can't remove a NULL player from the players list.", result);
    }

    @Test
    public void TestPlaceShipSuccessHorizontal() throws RemoteException {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers();
        // Player 1
        manager.placeShip(player1, locationShip, 3, 0);
        int actualAmountOfShips = manager.getPlayers().get(0).getPlayer().amountOfShips();

        assertEquals("Amount of ships increased after place ship.", 1, actualAmountOfShips);
        int[] tempLocation = new int[2];
        Ship locatedShip = null;
        for (int i = 4; i < 7; i++) {
            tempLocation[0] = 4;
            tempLocation[1] = i;
            locatedShip = manager.getPlayers().get(0).getPlayer().getShipOnLocation(tempLocation);
            assertTrue("Ship is located where we placed it.", locatedShip.getLocationStart()[0] == locationShip[0]
                    && locatedShip.getLocationStart()[1] == locationShip[1]);
        }

        // Player 2
        manager.placeShip(player2, locationShip, 3, 0);

        actualAmountOfShips = manager.getPlayers().get(1).getPlayer().amountOfShips();

        assertEquals("Amount of ships increased after place ship.", 1, actualAmountOfShips);
        tempLocation = new int[2];
        locatedShip = null;
        for (int i = 4; i < 7; i++) {
            tempLocation[0] = 4;
            tempLocation[1] = i;
            locatedShip = manager.getPlayers().get(1).getPlayer().getShipOnLocation(tempLocation);
            assertTrue("Ship is located where we placed it.", locatedShip.getLocationStart()[0] == locationShip[0]
                    && locatedShip.getLocationStart()[1] == locationShip[1]);
        }

    }

    @Test
    public void TestPlaceShipSuccessVertical() throws RemoteException {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers();

        manager.placeShip(player1, locationShip, 3, 1);
        int actualAmountOfShips = manager.getPlayers().get(0).getPlayer().amountOfShips();

        assertEquals("Amount of ships increased after place ship.", 1, actualAmountOfShips);
        int[] tempLocation = new int[2];
        Ship locatedShip = null;
        for (int i = 4; i < 7; i++) {
            tempLocation[0] = i;
            tempLocation[1] = 4;
            locatedShip = manager.getPlayers().get(0).getPlayer().getShipOnLocation(tempLocation);
            assertTrue("Ship is located where we placed it.", locatedShip.getLocationStart()[0] == locationShip[0]
                    && locatedShip.getLocationStart()[1] == locationShip[1]);
        }

        // Player 2
        manager.placeShip(player2, locationShip, 3, 1);

        actualAmountOfShips = manager.getPlayers().get(1).getPlayer().amountOfShips();

        assertEquals("Amount of ships increased after place ship.", 1, actualAmountOfShips);
        tempLocation = new int[2];
        locatedShip = null;
        for (int i = 4; i < 7; i++) {
            tempLocation[0] = i;
            tempLocation[1] = 4;
            locatedShip = manager.getPlayers().get(1).getPlayer().getShipOnLocation(tempLocation);
            assertTrue("Ship is located where we placed it.", locatedShip.getLocationStart()[0] == locationShip[0]
                    && locatedShip.getLocationStart()[1] == locationShip[1]);
        }
        tempLocation = new int[2];
        locatedShip = manager.getPlayers().get(0).getPlayer().getShipOnLocation(tempLocation);
        assertNull("Unable to located ship without location.", locatedShip);

        tempLocation[0] = 16;
        tempLocation[1] = 5;
        locatedShip = manager.getPlayers().get(0).getPlayer().getShipOnLocation(tempLocation);
        assertNull("Unable to located ship on location 16 x-axis.", locatedShip);

        tempLocation[0] = 5;
        tempLocation[1] = 16;
        locatedShip = manager.getPlayers().get(0).getPlayer().getShipOnLocation(tempLocation);
        assertNull("Unable to located ship on location 16 y-axis.", locatedShip);

        tempLocation[0] = 0;
        tempLocation[1] = 5;
        locatedShip = manager.getPlayers().get(0).getPlayer().getShipOnLocation(tempLocation);
        assertNull("Unable to located ship on location 0 x-axis.", locatedShip);

        tempLocation[0] = 5;
        tempLocation[1] = 0;
        locatedShip = manager.getPlayers().get(0).getPlayer().getShipOnLocation(tempLocation);
        assertNull("Unable to located ship on location 0 y-axis.", locatedShip);
    }

    @Test
    public void TestPlaceShipOnInvalidLocation() throws RemoteException {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        int shipLength = 3;

        manager.buildOverviewsForPlayers();
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
    public void TestPlaceShipOnOtherShip() throws RemoteException {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers();

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
    public void TestPlaceShipMoreThan7() throws RemoteException {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers();

        manager.placeShip(player1, locationShip, 3, 0); // 1

        locationShip[0] = 4;
        locationShip[1] = 8;
        manager.placeShip(player1, locationShip, 3, 0); // 2

        locationShip[0] = 6;
        locationShip[1] = 4;
        manager.placeShip(player1, locationShip, 3, 0); // 3

        locationShip[0] = 6;
        locationShip[1] = 8;
        manager.placeShip(player1, locationShip, 3, 0); // 4

        locationShip[0] = 8;
        locationShip[1] = 4;
        manager.placeShip(player1, locationShip, 3, 0); // 5

        locationShip[0] = 8;
        locationShip[1] = 8;
        manager.placeShip(player1, locationShip, 3, 0); // 6

        locationShip[0] = 2;
        locationShip[1] = 3;
        manager.placeShip(player1, locationShip, 3, 0); // 7

        locationShip[0] = 4;
        locationShip[1] = 8;
        boolean result = manager.placeShip(player1, locationShip, 3, 0); // 8

        assertFalse("[PlaceShip #8] Player1 can't place more than 7 ships on the board.", result);

        locationShip[0] = 7;
        locationShip[1] = 5;
        result = manager.placeShip(player1, locationShip, 3, 0); // 9

        assertFalse("[PlaceShip #9] Player1 can't place more than 7 ships on the board.", result);

        // PlaceShips of player2
        result = manager.placeShip(player2, locationShip, 3, 0); // 1

        assertTrue("[PlaceShip #1] Player2 can place ship", result);

        locationShip[0] = 4;
        locationShip[1] = 8;
        manager.placeShip(player2, locationShip, 3, 0); // 2

        locationShip[0] = 6;
        locationShip[1] = 4;
        manager.placeShip(player2, locationShip, 3, 0); // 3

        locationShip[0] = 6;
        locationShip[1] = 8;
        manager.placeShip(player2, locationShip, 3, 0); // 4

        locationShip[0] = 8;
        locationShip[1] = 4;
        manager.placeShip(player2, locationShip, 3, 0); // 5

        locationShip[0] = 8;
        locationShip[1] = 8;
        manager.placeShip(player2, locationShip, 3, 0); // 6

        locationShip[0] = 2;
        locationShip[1] = 3;
        manager.placeShip(player2, locationShip, 3, 0); // 7

        locationShip[0] = 4;
        locationShip[1] = 8;
        result = manager.placeShip(player2, locationShip, 3, 0); // 8

        assertFalse("[PlaceShip #8] Player2 can't place more than 7 ships on the board.", result);

        locationShip[0] = 7;
        locationShip[1] = 5;
        result = manager.placeShip(player2, locationShip, 3, 0); // 9

        assertFalse("[PlaceShip #9] Player1 can't place more than 7 ships on the board.", result);
    }

    @Test
    public void TestFireTorpedoSucess() throws RemoteException {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers();

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
    public void TestFireTorpedoAtDamagedPartOfShip() throws RemoteException {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers();

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

    @Test
    public void TestRepairShipSucces() throws RemoteException {
        manager.addPlayer(player1);
        manager.addPlayer(player2);
        int[] locationShip = new int[2];
        locationShip[0] = 4;
        locationShip[1] = 4;
        manager.buildOverviewsForPlayers();

        manager.placeShip(player1, locationShip, 3, 0);

        // Fire torpedo at ship.
        int[] locationTorpedo = new int[2];
        locationTorpedo[0] = 4;
        locationTorpedo[1] = 4;

        // Repair first damage.
        manager.fireTorpedo(player2, "Normal Torpedo", locationTorpedo);
        boolean result = manager.repairShip(1, player1, locationShip);

        assertTrue("Ship repaired on start location.", result);

        locationTorpedo[0] = 4;
        locationTorpedo[1] = 5;

        // Repair second damage.
        manager.fireTorpedo(player2, "Normal Torpedo", locationTorpedo);
        result = manager.repairShip(1, player1, locationTorpedo);

        assertTrue("Ship repaired on ship deck location.", result);

        locationTorpedo[0] = 4;
        locationTorpedo[1] = 6;

        // Repair third damage.
        manager.fireTorpedo(player2, "Normal Torpedo", locationTorpedo);
        result = manager.repairShip(1, player1, locationTorpedo);

        assertTrue("Ship repaired on end location.", result);
    }

}

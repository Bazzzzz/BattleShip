/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Domain;

import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.IPlayer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sebas
 */
public class GameManager implements IGameManager {

    List<Torpedo> torpedos;
    List<Ship> ships;
    List<Overview> overviews;
    List<IPlayer> players;

    public GameManager() {
        torpedos = new ArrayList<Torpedo>();
        ships = new ArrayList<Ship>(14);
        overviews = new ArrayList<Overview>(4);
        players = new ArrayList<IPlayer>(2);
    }

    /**
     * Confirm if action can be performed on the selected board.
     *
     * @return True if action can be performed.
     */
    public boolean confirmBoard() {
        return false;
    }

    /**
     * Places a ship on the players board.
     *
     * @param player not null
     * @param locationStart must hold 2 elements
     * @param shipLength
     * @param direction 0 for vertical, 1 for horizontal
     * @return True if ship was placed.
     */
    public boolean placeShip(IPlayer player, int[] locationStart, int shipLength, int direction) {
        Ship ship = null;
        if (player != null) {
            if (locationStart.length == 2) {
                ship = new Ship(shipLength, locationStart, direction);
                // Confirm availability start and end.
                if (player.getOpponent().locationAvailable(ship.getLocationStart()) && player.getOpponent().locationAvailable(ship.getLocationEnd())) {
                    // Confirm availability inbetween start and end.
                    if (player.getOpponent().locationShipLengthAvailable(locationStart, locationStart, direction)) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    public void fireTorpedo(IPlayer player, String torpedoName, int[] location) {

    }

    public List<IPlayer> getPlayers() {
        return null;
    }

    public SpecialPackage claimSpecial(int[] location, IPlayer player) {
        return null;
    }

    public void updateOverview() {

    }

    public void placeSpecials(Overview overview) {

    }

    public boolean repairShip(int fix, IPlayer player, Ship ship) {
        return false;
    }

    public List<Torpedo> getAvailableTorpedos(IPlayer player) {
        return null;
    }

    public List<Overview> getOverviews() {
        return null;
    }

    public List<SpecialPackage> getSpecials(IPlayer player) {
        return null;
    }

    public boolean useSpecial(SpecialPackage special) {
        return false;
    }

    public IPlayer addPlayer(IPlayer player) {
        return null;
    }

    public boolean removePlayer(IPlayer player) {
        return false;
    }

    public int damageShip(IPlayer player, int[] location) {
        return 0;
    }
}

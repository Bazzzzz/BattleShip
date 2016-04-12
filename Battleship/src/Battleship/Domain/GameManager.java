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
    List<Overview> overviews;
    List<IPlayer> players;

    public GameManager() {
        torpedos = new ArrayList<Torpedo>();
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
            if (player.getPlayer().amountOfShips() < 7) {
                if (locationStart.length == 2) {
                    ship = new Ship(shipLength, locationStart, direction);
                    // Confirm availability start and end.
                    if (player.getPlayer().locationAvailable(ship.getLocationStart()) && player.getPlayer().locationAvailable(ship.getLocationEnd())) {
                        // Confirm availability inbetween start and end.
                        if (player.getPlayer().locationShipLengthAvailable(ship.getLocationStart(), ship.getLocationEnd(), direction)) {
                            if (player.getPlayer().addShip(ship)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Fire a torpedo at the opponents board. If a ship is hit it will take
     * damage. If a special package is hit is will be claimed by the player.
     *
     * @param player not null
     * @param torpedoName not null
     * @param firedLocation
     */
    public void fireTorpedo(IPlayer player, String torpedoName, int[] firedLocation) {
        Torpedo torpedo = null;
        if (player != null) {
            IPlayer opponentPlayer = null;
            List<IPlayer> players = this.getPlayers();
            for (IPlayer p : players) {
                if (!player.equals(p)) {
                    opponentPlayer = p;
                    break;
                }
            }
            if (torpedoName != null) {
                torpedo = new Torpedo(torpedoName);
                if (player.getOpponent().locationHasShip(firedLocation)) {
                    this.damageShip(opponentPlayer, firedLocation);
                    torpedo.updateFireLocation(firedLocation);
                    torpedos.add(torpedo);
                } else if (player.getOpponent().locationHasSpecial(firedLocation)) {

                } else {
                    return;
                }
            }
        }
    }

    public List<IPlayer> getPlayers() {
        return this.players;
    }

    /**
     * Claims a special package and make it available for a player to use.
     *
     * @param location
     * @param player not null
     * @return The claimed SpecialPackage or null.
     */
    public SpecialPackage claimSpecial(int[] location, IPlayer player) {
        if (player != null) {
            List<SpecialPackage> specials = player.getSpecials();
            for (SpecialPackage special : specials) {
                if (special.getPlacedLocation().equals(location)) {
                    special.claimSpecial();
                    return special;
                }
            }
        }
        return null;
    }

    public void updateOverview() {

    }

    /**
     * Places special packages on the overview
     *
     * @param overview not null
     */
    public void placeSpecials(Overview overview) {
        if (overview != null) {
            overview.buildSpecialPackages();
        }
    }

    public boolean repairShip(int fix, IPlayer player, Ship ship) {
        return false;
    }

    public List<Torpedo> getAvailableTorpedos(IPlayer player) {
        return null;
    }

    public List<Overview> getOverviews() {
        return getOverviews();
    }

    public List<SpecialPackage> getSpecials(IPlayer player) {
        return player.getSpecials();
    }

    public boolean useSpecial(SpecialPackage special) {
        return false;
    }

    /**
     * Add a player to the players list
     *
     * @param player not null
     * @return The added player, Null if not added.
     */
    public IPlayer addPlayer(IPlayer player) {
        if (player != null) {
            this.players.add(player);
            return player;
        }
        return null;
    }

    /**
     * Removes a player if they are in the players list.
     *
     * @param player not null
     * @return True if removed, False if not.
     */
    public boolean removePlayer(IPlayer player) {
        if (player != null) {
            List<IPlayer> playersTemp = this.getPlayers();
            for (IPlayer p : playersTemp) {
                if (p.equals(player)) {
                    this.players.remove(p);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Inflicts damage to the ship from the player
     *
     * @param player
     * @param location
     * @return Total damage the ship has taken.
     */
    public int damageShip(IPlayer player, int[] location) {
        Ship ship = player.getPlayer().getShipOnLocation(location);
        int damage = -1;
        int shipDamage = ship.changeAmountHit(damage);
        return shipDamage;
    }
}

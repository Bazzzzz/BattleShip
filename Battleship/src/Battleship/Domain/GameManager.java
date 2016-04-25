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
     * @return True if torpedo was fired.
     */
    public boolean fireTorpedo(IPlayer player, String torpedoName, int[] firedLocation) {
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
            // TODO: Change all player.getOpponent() to opponentPlayer.getPlayer()
            if (torpedoName != null) {
                if (player.getOpponent().locationHasTorpedo(firedLocation)) {
                    return false;
                }
                torpedo = new Torpedo(torpedoName);
                torpedo.updateFireLocation(firedLocation);
                torpedos.add(torpedo);
                if (player.getOpponent().locationHasShip(firedLocation)) {
                    if (this.damageShip(opponentPlayer, firedLocation) == 1) {
                        // TODO: Animation of ship destruction?
                        
                    }
                    player.getOpponent().displayTorpedoShipHit(firedLocation);
                    return true;
                } else if (player.getOpponent().locationHasSpecial(firedLocation)) {
                    // TODO: Obtain Special.
                    return true;
                } else {
                    player.getOpponent().displayTorpedo(firedLocation);
                    return true;
                }
            }
        }
        return false;
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
    /**
     * Repairs the ship that's selected by the player.
     * @param fix larger than 0
     * @param player not NULL
     * @param location
     * @return True if a ship was fixed. False otherwise.
     */
    public boolean repairShip(int fix, IPlayer player, int[] location) {
        if (player != null && fix > 0) {
            Ship tempShip = player.getPlayer().getShipOnLocation(location);
            if (tempShip != null) {
                tempShip.changeAmountHit((-1) * fix);
                if (player.getPlayer().fixShipOnLocation(location)) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<Torpedo> getAvailableTorpedos(IPlayer player) {
        return null;
    }

    public List<Overview> getOverviews() {
        return this.overviews;
    }

    public List<Torpedo> getTorpedos() {
        return torpedos;
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
     * @param player not null or already in the list
     * @return The added player or null if player is null.
     * @exception IllegalArgumentException Player is already in the list.
     */
    public IPlayer addPlayer(IPlayer player) {
        if (player != null) {
            if (this.players.size() != 0) {
                for (IPlayer p : this.players) {
                    if (p.equals(player)) {
                        throw new IllegalArgumentException("The player you tried to add is already in the list.");
                    }
                }
            }

            this.players.add(player);
            return player;
        }
        throw new IllegalArgumentException("Unable to add a NULL player to the list..");
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
        int damage = 1;
        int shipDamage = ship.changeAmountHit(damage);

        if (ship.isDestroyed()) {
            return -1;
        }
        return shipDamage;
    }

    /**
     * Set the overviews per player.
     *
     * @param player1
     * @param player2
     */
    public void buildOverviewsForPlayers(IPlayer player1, IPlayer player2) {
        // Set players own overviews.
        Overview player1OwnField = new Overview();
        Overview player2OwnField = new Overview();

        player1.setPlayerOverview(player1OwnField);
        player2.setPlayerOverview(player2OwnField);

        // Set players opponents overviews.
        Overview player1OpponentField = new Overview();
        player1OpponentField = player2OwnField;

        player1.setOpponentOverview(player2OwnField);
        Overview player1OpponentFieldTemp = player1.getOpponent();

        player2.setOpponentOverview(player1OwnField);
        Overview player2OpponentField = player2.getOpponent();

        overviews.add(player1OwnField);
        overviews.add(player2OwnField);
        overviews.add(player1OpponentField);
        overviews.add(player2OpponentField);
    }
}

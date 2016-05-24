/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Domain;

import fontys.observer.RemotePropertyListener;
import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.ILobby;
import Battleship.Interfaces.IPlayer;
import Battleship.RMI.RMIClient;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePublisher;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author sebas
 */
public class GameManager extends UnicastRemoteObject implements IGameManager, RemotePropertyListener {

    private String name;
    private List<Torpedo> torpedos;
    private List<Overview> overviews;
    private List<IPlayer> players;

    public GameManager() throws RemoteException {
        torpedos = new ArrayList<>();
        overviews = new ArrayList<>(4);
        players = new ArrayList<>(2);

    }

    public GameManager(String name) throws RemoteException {
        this();
        if (name.endsWith("game")) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Game name has to end with game.");
        }

    }

    public String getName() throws RemoteException {
        return name;
    }

    /**
     * Confirm if action can be performed on the selected board.
     *
     * @return True if action can be performed.
     */
    @Override
    public boolean confirmBoard() throws RemoteException {
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
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized boolean placeShip(IPlayer player, int[] locationStart, int shipLength, int direction) throws RemoteException {
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
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized boolean fireTorpedo(IPlayer player, String torpedoName, int[] firedLocation) throws RemoteException {
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
                if (opponentPlayer.getPlayer().locationHasTorpedo(firedLocation)) {
                    return false;
                }
                torpedo = new Torpedo(torpedoName);
                torpedo.updateFireLocation(firedLocation);
                torpedos.add(torpedo);
                if (opponentPlayer.getPlayer().locationHasShip(firedLocation)) {
                    if (this.damageShip(opponentPlayer, firedLocation) == 1) {
                        // TODO: Animation of ship destruction?

                    }
                    opponentPlayer.getPlayer().displayTorpedoShipHit(firedLocation);
                    return true;
                } else if (opponentPlayer.getPlayer().locationHasSpecial(firedLocation)) {
                    // TODO: Obtain Special TESTING
                    this.claimSpecial(firedLocation, player);
                    return true;
                } else {
                    opponentPlayer.getPlayer().displayTorpedo(firedLocation);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<IPlayer> getPlayers() throws RemoteException {
        return this.players;
    }

    @Override
    public List<Torpedo> getTorpedos() throws RemoteException {
        return torpedos;
    }

    /**
     * Claims a special package and make it available for a player to use.
     *
     * @param location
     * @param player not null
     * @return The claimed SpecialPackage or null.
     */
    @Override
    public synchronized SpecialPackage claimSpecial(int[] location, IPlayer player) throws RemoteException {
        if (player != null) {
            List<SpecialPackage> specials = player.getSpecials();
            for (SpecialPackage special : specials) {
                if (special.getPlacedLocation().equals(location)) {
                    special.claimSpecial();
                    return special;
                    // TODO: claimSpecial TESTING
                }
            }
        }
        return null;
    }

    @Override
    public void updateOverview(IGameManager gameManager, IPlayer player) throws RemoteException {
        Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, new Throwable());
    }

    /**
     * Places special packages on the overview
     *
     * @param overview not null
     */
    @Override
    public synchronized void placeSpecials(Overview overview) throws RemoteException {
        if (overview != null) {
            overview.buildSpecialPackages();
        }
    }

    /**
     * Repairs the ship that's selected by the player.
     *
     * @param fix larger than 0
     * @param player not NULL
     * @param location
     * @return True if a ship was fixed. False otherwise.
     */
    @Override
    public synchronized boolean repairShip(int fix, IPlayer player, int[] location) throws RemoteException {
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

    @Override
    public synchronized List<Torpedo> getAvailableTorpedos(IPlayer player) throws RemoteException {
        return null;
    }

    @Override
    public synchronized List<Overview> getOverviews() throws RemoteException {
        return this.overviews;
    }

    @Override
    public synchronized List<SpecialPackage> getSpecials(IPlayer player) throws RemoteException {
        return player.getSpecials();
    }

    @Override
    public synchronized boolean useSpecial(SpecialPackage special) throws RemoteException {
        return false;
    }

    /**
     * Add a player to the players list
     *
     * @param player not null or already in the list
     * @return The added player or null if player is null.
     * @exception IllegalArgumentException Player is already in the list.
     */
    @Override
    public synchronized IPlayer addPlayer(IPlayer player) throws RemoteException {
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
    @Override
    public synchronized boolean removePlayer(IPlayer player) throws RemoteException {
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
    @Override
    public int damageShip(IPlayer player, int[] location) throws RemoteException {
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
    @Override
    public synchronized void buildOverviewsForPlayers(IPlayer player1, IPlayer player2) throws RemoteException {
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

    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {

    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) throws RemoteException {
        IGameManager manager = (IGameManager) pce.getNewValue();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(manager.getPlayers().size());
                } catch (RemoteException ex) {
                    Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}

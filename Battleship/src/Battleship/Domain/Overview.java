/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author sebas
 */
public class Overview implements Serializable {

    private boolean isOpponentsBoard;
    private List<SpecialPackage> specials;
    private List<Ship> ships;
    /**
     * 0 = nothing is placed
     * 1 = ship location
     * 2 = SpecialPackage
     */
    private int[][] board;
    public static final int BOARDWIDTH = 16;
    public static final int BOARDHEIGHT = 16;

    public Overview() {
        this.isOpponentsBoard = false;
        this.specials = new ArrayList<SpecialPackage>(4);
        this.ships = new ArrayList<Ship>(7);
        board = new int[BOARDWIDTH][BOARDHEIGHT];
    }

    public boolean isIsOpponentsBoard() {
        return isOpponentsBoard;
    }

    public List<SpecialPackage> getSpecials() {
        return specials;
    }

    /**
     * Enables a board to become an opponents board.
     *
     * @return opponentsBoard field.
     */
    public boolean setOpponentsBoard() {
        this.isOpponentsBoard = !this.isOpponentsBoard;
        return this.isOpponentsBoard;
    }

    /**
     * Checks if the locations between the end and the start of a ship are
     * available.
     *
     * @param locationStart has 2 elements
     * @param locationEnd has 2 elements
     * @param direction 0 for vertical, 1 for horizontal
     * @return True if all locations for a ships length are available.
     */
    public boolean locationShipLengthAvailable(int[] locationStart, int[] locationEnd, int direction) {
        if (locationStart.length == 2 && locationEnd.length == 2) {
            // Vertical means x-axis remains the same.
            int xStart = locationStart[0];
            int yStart = locationStart[1];
            if (direction == 0) {
                int yEnd = locationEnd[1];

                for (int i = yEnd; i > yStart; i--) {
                    int[] tempLocation = new int[2];
                    tempLocation[0] = xStart;
                    tempLocation[1] = i;
                    if (!this.locationAvailable(tempLocation)) {
                        return false;
                    }
                }
                return true;
            } else if (direction == 1) {
                // Horizontal means y-axis remains the same.
                int xEnd = locationEnd[0];

                for (int i = xEnd; i > xStart; i--) {
                    int[] tempLocation = new int[2];
                    tempLocation[0] = i;
                    tempLocation[1] = yStart;
                    if (!this.locationAvailable(tempLocation)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a location on the overview is available for placement action.
     *
     * @param location has 2 elements First index holds x-axis, second index
     * holds y-axis. Board location is available if location index holds 0.
     * Board location has ship if location index holds 1. Board location has
     * special if location index holds 2. Board location has torpedo if location
     * index holds -1.
     * @return True if the location is available, False if filled.
     */
    public boolean locationAvailable(int[] location) {
        if (location.length == 2) {
            int xLocationIndex = location[0];
            int yLocationIndex = location[1];
            if ((xLocationIndex >= 0 && xLocationIndex < BOARDWIDTH - 1)
                    && (yLocationIndex >= 0 && yLocationIndex < BOARDHEIGHT - 1)) {
                if (board[xLocationIndex][yLocationIndex] == 0) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Checks if a location on the overview has a ship.
     *
     * @param location has 2 elements First index holds x-axis, second index
     * holds y-axis. Board location is available if location index holds 0.
     * Board location has ship if location index holds 1. Board location has
     * special if location index holds 2. Board location has torpedo if location
     * index holds -1.
     * @return True if the location holds a piece of a ship, False if not.
     */
    public boolean locationHasShip(int[] location) {
        if (verifySpecificLocation(location)) {
            int xLocationIndex = location[0];
            int yLocationIndex = location[1];

            if (board[xLocationIndex][yLocationIndex] == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a location on the overview has a torpedo.
     *
     * @param location has 2 elements First index holds x-axis, second index
     * holds y-axis. Board location is available if location index holds 0.
     * Board location has ship if location index holds 1. Board location has
     * special if location index holds 2. Board location has torpedo if location
     * index holds -1.
     * @return True if the location holds a torpedo, False if not.
     */
    public boolean locationHasTorpedo(int[] location) {
        if (verifySpecificLocation(location)) {
            int xLocationIndex = location[0];
            int yLocationIndex = location[1];

            if (board[xLocationIndex][yLocationIndex] == -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Shows the amount of ships a player has on the overview.
     *
     * @return Size of ships.
     */
    public int amountOfShips() {
        return ships.size();
    }

    /**
     * Checks if a location on the overview has a special package.
     *
     * @param location has 2 elements First index holds x-axis, second index
     * holds y-axis. Board location is available if location index holds 0.
     * Board location has ship if location index holds 1. Board location has
     * special if location index holds 2. Board location has torpedo if location
     * index holds -1.
     * @return True if the location holds a special package, False if not.
     */
    public boolean locationHasSpecial(int[] location) {
        if (verifySpecificLocation(location)) {
            int xLocationIndex = location[0];
            int yLocationIndex = location[1];

            if (board[xLocationIndex][yLocationIndex] == 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a ship to the board.
     *
     * @param ship
     * @return True if placed.
     */
    public boolean addShip(Ship ship) {
        if (ship != null) {
            ships.add(ship);
            this.placeShipOnBoard(ship);
            return true;
        }
        return false;

    }

    /**
     * Get the ship that's located on a certain location. Ship can be found by
     * it's start and end location, as well as any location in between.
     *
     * @param location has to hold 2 elements.
     * @return Ship that's located on a location.
     */
    public Ship getShipOnLocation(int[] location) {
        if (location.length == 2
                && location[0] > 0 && location[0] < BOARDWIDTH
                && location[1] > 0 && location[1] < BOARDHEIGHT) {
            for (Ship ship : this.ships) {
                if (ship.getLocationStart().equals(location)) {
                    return ship;
                } else if (ship.getLocationEnd().equals(location)) {
                    return ship;
                } else if (checkForShipOnLocation(ship, location)) {
                    return ship;
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    /**
     * Calculates whether the given location holds a ship.
     *
     * @param ship
     * @param location
     * @return True if a ship is found. False if not.
     */
    private boolean checkForShipOnLocation(Ship ship, int[] location) {
        int locationStart[] = ship.getLocationStart();
        int locationEnd[] = ship.getLocationEnd();
        // Vertical x-axis remains the same
        if (ship.getDirection() == 0) {
            int xIndex = locationStart[0];
            for (int i = locationEnd[1]; i > locationStart[0]; i--) {
                int[] tempLocation = new int[2];
                tempLocation[0] = xIndex;
                tempLocation[1] = i;
                if (location == tempLocation) {
                    return true;
                }
            }
        } // Horizontal y-axis remains the same
        else if (ship.getDirection() == 1) {
            int yIndex = locationStart[1];
            for (int i = locationEnd[0]; i > locationStart[0]; i--) {
                int[] tempLocation = new int[2];
                tempLocation[0] = i;
                tempLocation[1] = yIndex;
                if (location == tempLocation) {
                    return true;
                }
            }
        } else {
            return true;
        }

        return false;
    }

    /**
     * Place ship on the board.
     *
     * @param ship
     */
    private void placeShipOnBoard(Ship ship) {
        this.defineLocationsForShipBody(ship);
    }

    /**
     * Calculates the location of the ship on the board. Fills in a 1 to
     * determine where a ship is located.
     *
     * @param ship
     */
    private void defineLocationsForShipBody(Ship ship) {
        int[] locationStart = ship.getLocationStart();
        int[] locationEnd = ship.getLocationEnd();
        int direction = ship.getDirection();
        // Vertical x-axis remains the same
        if (direction == 0) {
            int xIndex = locationStart[0];
            for (int i = locationEnd[1]; i > locationStart[1]; i--) {
                board[xIndex][i] = 1;
            }
        } // Horizontal y-axis remains the same
        else if (direction == 1) {
            int yIndex = locationStart[1];
            for (int i = locationEnd[0]; i > locationStart[0]; i--) {
                board[i][yIndex] = 1;
            }
        }
    }

    /**
     * Verifies if the location given is reachable on the board.
     *
     * @param location has 2 elements
     * @return True if reachable location, False if not.
     */
    private boolean verifySpecificLocation(int[] location) {
        if (location.length == 2) {
            int xLocationIndex = location[0];
            int yLocationIndex = location[1];
            if ((xLocationIndex >= 0 && xLocationIndex < BOARDWIDTH - 1)
                    && (yLocationIndex >= 0 && yLocationIndex < BOARDHEIGHT - 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Builds and places random special packages on the overview.
     */
    public void buildSpecialPackages() {
        int[] randomLocation = new int[2];
        int numberOfPackages = 0;
        while (numberOfPackages < 4) {

            int[] tempLocation = new int[2];

            // Set x-axis
            Random randomX = new Random();
            int randomNumber = randomX.nextInt(BOARDWIDTH - 1);
            tempLocation[0] = randomNumber;

            // Set y-axis
            Random randomY = new Random();
            randomNumber = randomY.nextInt(BOARDHEIGHT - 1);
            tempLocation[1] = randomNumber;

            if (locationAvailable(tempLocation)) {
                numberOfPackages++;
                randomLocation = tempLocation;
                SpecialPackage special = new SpecialTorpedo("temp", randomLocation);
                specials.add(special);
            }
        }
        if(specials.size() == 4) {
            for (SpecialPackage special : specials) {
                placeSpecialOnBoard(special);
            }
        }
    }
    private void placeSpecialOnBoard(SpecialPackage special) {
        board[special.getPlacedLocation()[0]][special.getPlacedLocation()[1]] = 2;
    }
}

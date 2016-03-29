/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Domain;

/**
 *
 * @author sebas
 */
public class Ship {

    private int length;
    private int[] locationStart;
    private int[] locationEnd;
    private int amountHit = 0;
    private String name;
    private int direction;

    public Ship(int length, int[] locationStart, int direction) {
        if (length < 2) {
            throw new IllegalArgumentException("Ship can't be smaller than 2.");
        } else if (length > 7) {
            throw new IllegalArgumentException("Ship can't be larger than 7.");
        } else {
            this.length = length;
        }
        // direction 1 = horizontal
        // direction 0 = vertical
        if (direction == 1 || direction == 0) {
            this.direction = direction;
        } else {
            throw new IllegalArgumentException("Ships direction can't be anything but 0, 1.");
        }
        if (locationStart.length == 2) {
            this.locationEnd = calculateLocationEnd(length, locationStart, direction);
            this.locationStart = locationStart;
        } else {
            throw new IllegalArgumentException("Ships start location invalid.");
        }
        this.name = determineShipName(length);

    }
    /**
     * Changes the damage a ship has taken from torpedos.
     * @param number
     * @return Damage taken.
     */
    public int changeAmountHit(int number) {
        if (number > 0) {
            this.amountHit = this.increaseHit(number);
        }
        else {
            this.amountHit = this.decreaseHit(number);
        }
        return this.amountHit;
    }
    
    private int increaseHit(int number) {
        return this.amountHit += number;
        
    }
    private int decreaseHit(int number) {
        return this.amountHit -= number;
    }
    /**
     * Determines the name of the ship dependant on the size.
     *
     * @param length
     * @return Name of the ship.
     */
    private String determineShipName(int length) {
        switch (length) {
            case 2:
                return "Patrol Boat";
            case 3:
                return "Cruiser";
            case 4:
                return "Submarine";
            case 5:
                return "Battleship";
            case 6:
                return "Aircraft Carrier";
            default:
                return "Destroyer";
        }
    }

    /**
     * Calculates the end location of a ship.
     * A ship can't be placed beyond the board, nor can it end or start in a corner.
     *
     * @param length
     * @param locationStart
     * @param direction
     * @return
     */
    private int[] calculateLocationEnd(int length, int[] locationStart, int direction) {
        int endXIndex = 1;
        int endYIndex = 1;
        int[] calculatedEndLocation;
        // Check to see if a ship doesn't start in a corner.
        if (checkCorners(locationStart)) {
            // Check to see if a ship fits on the board.
            if (checkIfFits(locationStart, length, direction)) {
                // Horizontal ships
                if (direction == 1) {
                    endXIndex += locationStart[0] + length;
                    endYIndex += locationStart[1];
                } // Vertical ships
                else {
                    endXIndex += locationStart[0];
                    endYIndex += locationStart[1] + length;
                }
                calculatedEndLocation = new int[2];
                calculatedEndLocation[0] = endXIndex;
                calculatedEndLocation[1] = endYIndex;
                return calculatedEndLocation;
            } else {
                throw new IllegalArgumentException("Ships has to fit in the board.");
            }
        } else {
            throw new IllegalArgumentException("Ships can't be placed in a corner.");
        }
    }

    /**
     * Checks if a ship can be placed in their respective horizontal or vertical
     * placement.
     *
     * @param locationStart
     * @param length
     * @param direction
     * @return True if placeable.
     */
    private boolean checkIfFits(int[] locationStart, int length, int direction) {
        // Check horizontal ships
        if (direction == 1) {
            if (checkIfFitsIndex(locationStart[0], length)) {
                return true;
            }
        } // Check vertical ships
        else {
            if (checkIfFitsIndex(locationStart[1], length)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates if the index value of a ships location and the length of the
     * ship don't pass the size of the board.
     *
     * @param value
     * @param length
     * @return True if a ship fits.
     */
    private boolean checkIfFitsIndex(int value, int length) {
        if (value + length < 15) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calculates if a ship doesn't start in a corner.
     *
     * @param locationStart
     * @return True if it doesn't start in a corner.
     */
    private boolean checkCorners(int[] locationStart) {
        if ((locationStart[0] > 1 && locationStart[1] > 1) // Top left corner
                || (locationStart[0] < 15 && locationStart[1] < 15) // Bottom right corner
                || (locationStart[0] > 1 && locationStart[1] < 15) // Bottom left corner
                || locationStart[0] < 15 && locationStart[1] > 1) // Top right corner
        {
            return true;
        }
        return false;
    }
}

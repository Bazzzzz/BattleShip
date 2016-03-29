/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sebas
 */
public class Overview implements Serializable {
    private boolean isOpponentsBoard;
    private List<SpecialPackage> specials;
    
    public Overview() {
        this.isOpponentsBoard = false;
        this.specials = new ArrayList<SpecialPackage>(4);
    }
    /**
     * Checks if a location on the overview is available for action.
     * @param location not null
     * @return True if the location is available, False if filled.
     */
    public boolean locationAvailable(int[] location) {
        return false;
    }
    /**
     * Checks if a location on the overview has a ship.
     * @param location not null
     * @return True if the location holds a piece of a ship, False if not.
     */
    public boolean locationHasShip(int[] location) {
        return false;
    }
}

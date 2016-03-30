/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Domain;

import java.io.Serializable;

/**
 *
 * @author sebas
 */
public class Torpedo implements Serializable {

    private String name;
    private int[] firedLocation;

    /**
     * Constructs a Torpedo
     * @param name not null
     */
    public Torpedo(String name) {
        if (name != null) {
            this.name = name;
            this.firedLocation = null;
        }
    }
    /**
     * Constructs a Torpedo
     * @param name not null
     * @param firedLocation not null
     */
    public Torpedo(String name, int[] firedLocation) {
        if (name != null && firedLocation != null) {
            this.name = name;
            this.firedLocation = firedLocation;
        }
    }
    
    public void updateFireLocation(int[] location) {
        this.firedLocation = location;
    }
    public String getName() {
        return name;
    }

    public int[] getFiredLocation() {
        return firedLocation;
    }

    @Override
    public String toString() {
        if (this.firedLocation == null) {
            return "Torpedo: " + name;
        }
        return "Torpedo: " + name + " fired at: " + firedLocation + ".";
    }

}

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
public abstract class SpecialPackage {

    private String name;
    private boolean claimed;
    private int[] placedLocation;

    public SpecialPackage(String name, int[] placedLocation) {
        this.name = name;
        this.claimed = false;
        this.placedLocation = placedLocation;
    }

    /**
     * Claims a special package
     *
     * @return boolean
     */
    public boolean isClaimed() {
        this.claimed = !this.claimed;
        return this.claimed;
    }
}

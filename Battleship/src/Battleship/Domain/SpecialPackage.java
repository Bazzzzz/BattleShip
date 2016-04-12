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

    public int[] getPlacedLocation() {
        return this.placedLocation;
    }
    /**
     * Claims a special package
     *
     * @return boolean
     */
    public boolean isClaimed() {
        return this.claimed;
    }
    public void claimSpecial() {
        this.claimed = !this.claimed;
    }
}

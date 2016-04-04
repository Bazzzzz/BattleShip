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
public class SpecialTorpedo extends SpecialPackage {
    private int[] hitbox;
    private int[] location;
    
    /**
     *
     * @param name
     * @param placedLocation
     */
    public SpecialTorpedo(String name, int[] placedLocation) {
        super(name,placedLocation);

    }
}

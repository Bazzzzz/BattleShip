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
    private String name;
    private int[] location;
    
    /**
     *
     * @param name
     * @param location
     */
    public SpecialTorpedo(String name, int[] locationFound) {
        super(name,locationFound);
        if (hitbox != null && name != null && location != null) {
            this.hitbox = hitbox;
            this.location = location;
            this.name = name;
            
        }
    }
}

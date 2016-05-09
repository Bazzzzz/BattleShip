/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Domain;

import Battleship.Interfaces.IPlayer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sebas
 */
public class Player implements IPlayer, Serializable {
    private String name;
    private boolean isTurn;
    private Overview player;
    private Overview opponent;
    private List<SpecialPackage> specials;

    public String getName() {
        return name;
    }
    
    /**
     * Constructs a player.
     * @param name not null
     */
    public Player(String name) {
        if(name != null) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("No player name filled in.");
        }
        this.isTurn = false;
        player = null;
        opponent = null;
        specials = new ArrayList<SpecialPackage>();
    }

    public Overview getPlayer() {
        return player;
    }

    public Overview getOpponent() {
        return opponent;
    }
    
    public void setPlayerOverview(Overview playerOverview) {
        this.player = playerOverview;
    }
    
    public void setOpponentOverview(Overview opponentOverview) {
        opponentOverview.setOpponentsBoard();
        this.opponent = opponentOverview;
    }
    /**
     * Changes the turn.
     * @return True if it's the players turn. False if not.
     */
    public boolean changeTurn() {
        this.isTurn = !this.isTurn;
        return this.isTurn;
    }
    /**
     * Returns the claimed specials for a player.
     * @return 
     */
    public List<SpecialPackage> getSpecials() {
        // TODO: Return special only when claimed.
        return specials;
    }
    /**
     * Allows a player to use a special package.
     * @param special not null
     * @return True if used. False if not.
     */
    public boolean useSpecial(SpecialPackage special) {
        // TODO: Method.
        return true;
    }
    public boolean equals(Object o) {
        if(o instanceof IPlayer) {
            IPlayer player = (IPlayer)o;
            if(this.getName().equals(player.getName())) {
                return true;
            }
        }
        return false;
    }
}

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
    private boolean isReady;

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
        this.isReady = false;
    }

    public Overview getPlayer() {
        return player;
    }

    public Overview getOpponent() {
        return opponent;
    }
    
    public Overview setPlayerOverview(Overview playerOverview) {
        this.player = playerOverview;
        return this.player;
    }
    
    public Overview setOpponentOverview(Overview opponentOverview) {
        opponentOverview.setOpponentsBoard();
        this.opponent = opponentOverview;
        return this.opponent;
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
        
        for(SpecialPackage special : this.opponent.getSpecials()) {
            if(special.isClaimed()) {
                this.specials.add(special);
            }
        }
        
        return this.specials;
    }
    /**
     * Allows a player to use a special package.
     * @param special not null
     * @return True if used. False if not.
     */
    @Override
    public boolean useSpecial(SpecialPackage special) {
        // TODO: Method.
        return true;
    }
    @Override
    public boolean isPlayerReady() {
        return this.isReady;
    }
    @Override
    public void setPlayerReady(boolean isReady) {
        this.isReady = isReady;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof IPlayer) {
            IPlayer player = (IPlayer)o;
            if(this.getName().equals(player.getName())) {
                return true;
            }
        }
        return false;
    }
    
    public String toString() {
        return String.format("Player information: \n %s", this.getName());
    }
}

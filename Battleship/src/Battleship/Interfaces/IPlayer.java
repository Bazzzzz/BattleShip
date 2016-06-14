/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Interfaces;

import Battleship.Domain.Overview;
import Battleship.Domain.SpecialPackage;
import java.rmi.Remote;
import java.util.List;

/**
 *
 * @author sebas
 */
public interface IPlayer {

    public String getName();
    
    public Overview getPlayer();

    public Overview getOpponent();

    public Overview setOpponentOverview(Overview opponentOverview);
    
    public Overview setPlayerOverview(Overview playerOverview);
    
    public boolean isTurn();
    /**
     * Changes the turn.
     *
     * @return True if it's the players turn. False if not.
     */
    public boolean changeTurn();

    /**
     * Returns the claimed specials for a player.
     *
     * @return
     */
    public List<SpecialPackage> getSpecials();

    /**
     * Allows a player to use a special package.
     *
     * @param special not null
     * @return True if used. False if not.
     */
    public boolean useSpecial(SpecialPackage special);
    
    /**
     * Checks if a player is ready for a game to start.
     * @return 
     */
    public boolean isPlayerReady();
    
    /**
     * Sets a player ready or unready. Has to be ready (true) for a game to start.
     * @param isReady True if ready to start. False if not ready.
     */
    public void setPlayerReady(boolean isReady);
    
    @Override
    public boolean equals(Object o);
}

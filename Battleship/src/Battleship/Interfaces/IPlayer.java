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

    public void setOpponentOverview(Overview opponentOverview);
    
    public void setPlayerOverview(Overview playerOverview);
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
}

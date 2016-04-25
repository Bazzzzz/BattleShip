/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Interfaces;

import Battleship.Domain.Overview;
import Battleship.Domain.Ship;
import Battleship.Domain.SpecialPackage;
import Battleship.Domain.Torpedo;
import java.util.List;

/**
 *
 * @author sebas
 */
public interface IGameManager {

    public boolean confirmBoard();

    public boolean placeShip(IPlayer player, int[] locationStart, int shipLength, int direction);

    public boolean fireTorpedo(IPlayer player, String torpedoName, int[] firedLocation);

    public List<IPlayer> getPlayers();

    public List<Torpedo> getTorpedos();
    
    public SpecialPackage claimSpecial(int[] location, IPlayer player);

    public void updateOverview();

    public void placeSpecials(Overview overview);

    public boolean repairShip(int fix, IPlayer player, int[] location);

    public List<Torpedo> getAvailableTorpedos(IPlayer player);

    public List<Overview> getOverviews();

    public List<SpecialPackage> getSpecials(IPlayer player);

    public boolean useSpecial(SpecialPackage special);

    public IPlayer addPlayer(IPlayer player);

    public boolean removePlayer(IPlayer player);

    public int damageShip(IPlayer player, int[] location);
    
    public void buildOverviewsForPlayers(IPlayer player1, IPlayer player2);
}

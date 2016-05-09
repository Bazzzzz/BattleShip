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
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author sebas
 */
public interface IGameManager extends Remote {

    public boolean confirmBoard() throws RemoteException;

    public boolean placeShip(IPlayer player, int[] locationStart, int shipLength, int direction) throws RemoteException;

    public boolean fireTorpedo(IPlayer player, String torpedoName, int[] firedLocation) throws RemoteException;

    public List<IPlayer> getPlayers() throws RemoteException;

    public List<Torpedo> getTorpedos() throws RemoteException;
    
    public SpecialPackage claimSpecial(int[] location, IPlayer player) throws RemoteException;

    public void updateOverview() throws RemoteException;

    public void placeSpecials(Overview overview) throws RemoteException;

    public boolean repairShip(int fix, IPlayer player, int[] location) throws RemoteException;

    public List<Torpedo> getAvailableTorpedos(IPlayer player) throws RemoteException;

    public List<Overview> getOverviews() throws RemoteException;

    public List<SpecialPackage> getSpecials(IPlayer player) throws RemoteException;

    public boolean useSpecial(SpecialPackage special) throws RemoteException;

    public IPlayer addPlayer(IPlayer player) throws RemoteException;

    public boolean removePlayer(IPlayer player) throws RemoteException;

    public int damageShip(IPlayer player, int[] location) throws RemoteException;
    
    public void buildOverviewsForPlayers(IPlayer player1, IPlayer player2) throws RemoteException;
}

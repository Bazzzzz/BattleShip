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
import Battleship.RMI.RMIClient;
import fontys.observer.RemotePropertyListener;
import fontys.observer.RemotePublisher;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author sebas
 */
public interface IGameManager extends RemotePublisher {

    public String getName() throws RemoteException;

    public boolean confirmBoard() throws RemoteException;

    public boolean placeShip(IPlayer player, int[] locationStart, int shipLength, int direction) throws RemoteException;

    public boolean fireTorpedo(IPlayer player, IPlayer receiveingPlayer, String torpedoName, int[] firedLocation) throws RemoteException;

    public boolean changeTurn(IPlayer player) throws RemoteException;
    
    public boolean getPlayerTurn(IPlayer player) throws RemoteException;

    public List<IPlayer> getPlayers() throws RemoteException;

    public List<Torpedo> getTorpedos() throws RemoteException;

    public SpecialPackage claimSpecial(int[] location, IPlayer player) throws RemoteException;

    public void updateOverview(IPlayer player, Overview overview) throws RemoteException;

    public void placeSpecials(Overview overview) throws RemoteException;

    public boolean repairShip(int fix, IPlayer player, int[] location) throws RemoteException;

    public List<Torpedo> getAvailableTorpedos(IPlayer player) throws RemoteException;
    /**
     * Index 0 = Player1 overview
     * Index 1 = Player 2 overview
     * Index 2 = Player1 opponent overview (Player 2 overview)
     * Index 3 = Player2 opponent overview (Player1 overview)
     * @return
     * @throws RemoteException 
     */
    public List<Overview> getOverviews() throws RemoteException;

    public List<SpecialPackage> getSpecials(IPlayer player) throws RemoteException;

    public boolean useSpecial(SpecialPackage special) throws RemoteException;

    public IPlayer addPlayer(IPlayer player) throws RemoteException;

    public boolean removePlayer(IPlayer player) throws RemoteException;

    public int damageShip(IPlayer player, int[] location) throws RemoteException;

    public void buildOverviewsForPlayers() throws RemoteException;

    public void addListener(RemotePropertyListener listener, String property) throws RemoteException;

    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
/**
 *
 * @author sebas
 */
public interface IClientManager extends Remote {
    public List<ILobby> getLobbies() throws RemoteException; 
    public boolean addLobby(ILobby lobby) throws RemoteException;
    public boolean removeLobby(ILobby lobby) throws RemoteException;
    public ILobby findLobbyByPlayer(String playerName) throws RemoteException;
    public ILobby findLobbyByName(String name) throws RemoteException;
    public void removeAllLobbies() throws RemoteException;
    public boolean updateLobby(ILobby lobby) throws RemoteException ;
}

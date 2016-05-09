/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Domain;

import Battleship.Interfaces.ILobby;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author sebas
 */
public class Lobby extends UnicastRemoteObject implements ILobby {
    public Lobby() throws Exception {
        
    }
}

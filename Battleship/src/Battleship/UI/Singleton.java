/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import Battleship.Interfaces.ILobby;

/**
 *
 * @author sebas
 */
public class Singleton {
    private static Singleton instance = new Singleton();
    private String lobbyName;
    
    public static Singleton getInstance() {
        return Singleton.instance;
    }
    
    public Singleton(){
        this.lobbyName = null;
    }
    
    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }
    public String getLobbyName() {
        return this.lobbyName;
    }
    
}

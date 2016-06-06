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
    private String gameName;
    
    public static Singleton getInstance() {
        return Singleton.instance;
    }
    
    public Singleton(){
        this.lobbyName = null;
        this.gameName = null;
    }
    
    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }
    public String getLobbyName() {
        return this.lobbyName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    
    
}

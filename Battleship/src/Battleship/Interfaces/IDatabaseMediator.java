/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.Interfaces;

import Battleship.Domain.Account;

/**
 *
 * @author sebas
 */
public interface IDatabaseMediator {
    public boolean addNewPlayer(Account account);
    
    public boolean login(String username, String password);
    
    public boolean logout();
}

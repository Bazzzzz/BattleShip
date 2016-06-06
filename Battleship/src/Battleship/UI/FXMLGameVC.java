/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import Battleship.Interfaces.IGameManager;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author sebas
 */
public class FXMLGameVC implements Initializable {

    IGameManager gameManager;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        String gameName = Singleton.getInstance().getGameName();
        if (Battleship.handler.getRMIClient().connectToServer("gamesList", null)) {
            try {
                if (Battleship.handler.getRMIClient().getGameManagerRMI(Singleton.getInstance().getGameName())) {
                    IGameManager game = Battleship.handler.getRMIClient().getGameManager();
                    if (game != null) {
                        this.gameManager = game;
                        this.gameManager.buildOverviewsForPlayers();

                        System.out.println("Print player1's board");
                        this.gameManager.getPlayers().get(0).getPlayer().printBoard();

                        System.out.println("Print playe1's opponent board");
                        this.gameManager.getPlayers().get(0).getOpponent().printBoard();

                        System.out.println("Print player2's board");
                        this.gameManager.getPlayers().get(1).getPlayer().printBoard();

                        System.out.println("Print player2's opponent board");
                        this.gameManager.getPlayers().get(1).getOpponent().printBoard();
                    }

                }

            } catch (RemoteException ex) {
                Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}

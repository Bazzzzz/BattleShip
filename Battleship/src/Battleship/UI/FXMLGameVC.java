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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

/**
 * FXML Controller class
 *
 * @author sebas
 */
public class FXMLGameVC implements Initializable {

    IGameManager gameManager;
    @FXML
    private Button btnPlaceShip;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblGameTime;
    @FXML
    private Label lblPlayer1Name;
    @FXML
    private Label lblPlayer1Score;
    @FXML
    private Label lblPlayer1Turn;
    @FXML
    private Label lblPlayer2Name;
    @FXML
    private Label lblPlayer2Score;
    @FXML
    private Label lblPlayer2Turn;
    @FXML
    private Button btnFireTorpedo;
    @FXML
    private Button btnUseSpecial;
    @FXML
    private Button btnEndTurn;
    @FXML
    private Canvas canvasPlayer;
    @FXML
    private Canvas canvasOpponent;

    private Image fieldBackground;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initializeImages();

        this.drawBackground();

        String gameName = Singleton.getInstance().getGameName();
        if (Battleship.handler.getRMIClient().connectToServer("game", gameName)) {
            try {
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
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.drawStart();
        this.drawBoards();
    }

    private void drawBoards() {
        double canvasHeight = this.canvasPlayer.getHeight();
        double canvasWidth = this.canvasPlayer.getWidth();
        
        try {
            int boardWidth = this.gameManager.getOverviews().get(0).getBoardWidth();
            int boardHeight = this.gameManager.getOverviews().get(0).getBoardHeight();
            
            
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void drawStart() {
        double canvasHeight = this.canvasPlayer.getHeight();
        double canvasWidth = this.canvasPlayer.getWidth();
        try {
            int boardWidth = this.gameManager.getOverviews().get(0).getBoardWidth();
            int boardHeight = this.gameManager.getOverviews().get(0).getBoardHeight();
            // Draws an array so it's viewable.
            for (int i = boardWidth; i > 0; i--) {
                for (int j = boardHeight; j > 0; j--) {
                    this.canvasOpponent.getGraphicsContext2D().strokeLine(canvasWidth, canvasHeight, canvasWidth / i, canvasHeight / j);
                    this.canvasPlayer.getGraphicsContext2D().strokeLine(canvasWidth, canvasHeight, canvasWidth / i, canvasHeight / j);
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void drawBackground() {
        this.canvasOpponent.getGraphicsContext2D().drawImage(this.fieldBackground, 0, 0);
        this.canvasPlayer.getGraphicsContext2D().drawImage(this.fieldBackground, 0, 0);
    }

    private void initializeImages() {
        this.fieldBackground = new Image("Battleship/Images/WaterBG1.jpg");
    }

}

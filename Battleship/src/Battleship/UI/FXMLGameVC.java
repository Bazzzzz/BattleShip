/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.IPlayer;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sebas
 */
public class FXMLGameVC implements Initializable {

    IGameManager gameManager;
    
    IPlayer playingPlayer;
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
    private GridPane gridPanePlayer;
    @FXML
    private GridPane gridPaneOpponent;
    @FXML
    private HBox hBoxPlayerView;
    @FXML
    private HBox hBoxOpponentView;

    private Image fieldBackground;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initializeImages();

        this.drawBackground();
        
        playingPlayer = Battleship.handler.getPlayingPlayer();
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
                this.drawStart(gameManager);

                this.drawBoards(gameManager);

                this.showGrids();
                // http://stackoverflow.com/questions/31095954/how-to-get-gridpane-row-and-column-ids-on-mouse-entered-in-each-cell-of-grid-in
                
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    @FXML
    public void handlePlaceShipButton(ActionEvent e) {
        Random random = new Random();
        int x = random.nextInt(16);
        int y = random.nextInt(16);
        int[] location = new int[2];
        location[0] = x;
        location[1] = y;
        
        try {
            this.gameManager.getPlayers().get(0).getPlayer().printBoard();
            this.gameManager.placeShip(playingPlayer, location, 3, 0);
            this.drawBoards(this.gameManager);
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showGrids() {
        gridPanePlayer.setGridLinesVisible(true);

        gridPaneOpponent.setGridLinesVisible(true);

        hBoxPlayerView.getChildren().add(gridPanePlayer);
        hBoxOpponentView.getChildren().add(gridPaneOpponent);
    }

    private void drawBoards(IGameManager game) {
//        double canvasHeight = this.canvasPlayer.getHeight();
//        double canvasWidth = this.canvasPlayer.getWidth();

        try {
            boolean firstPlayer = true;
            for (IPlayer p : game.getPlayers()) {
                int[][] board = p.getPlayer().getBoard();
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[0].length; j++) {
                        if (firstPlayer) {
                            Label boardIndex = new Label(Integer.toString(board[i][j]));
                            boardIndex.setAlignment(Pos.CENTER);
                            gridPanePlayer.add(boardIndex, i, j);
                        } else {
                            Label boardIndex = new Label(Integer.toString(board[i][j]));
                            boardIndex.setAlignment(Pos.CENTER);
                            gridPaneOpponent.add(boardIndex, i, j);

                        }
                    }
                }
                firstPlayer = false;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void drawStart(IGameManager game) {
//        double canvasHeight = this.canvasPlayer.getHeight();
//        double canvasWidth = this.canvasPlayer.getWidth();
        try {
            gridPanePlayer = new GridPane();
            gridPaneOpponent = new GridPane();
            game.buildOverviewsForPlayers();
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void drawBackground() {
//        this.canvasOpponent.getGraphicsContext2D().drawImage(this.fieldBackground, 0, 0);
//        this.canvasPlayer.getGraphicsContext2D().drawImage(this.fieldBackground, 0, 0);
    }

    private void initializeImages() {
        this.fieldBackground = new Image("Battleship/Images/WaterBG1.jpg");
    }

}

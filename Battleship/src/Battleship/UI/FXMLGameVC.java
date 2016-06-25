/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import Battleship.Domain.Overview;
import Battleship.Exceptions.BattleshipExceptions;
import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.IPlayer;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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

    IPlayer opponentPlayer;

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

    private ScheduledExecutorService serviceGameRunner;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initializeImages();

        this.drawBackground();

        gridPanePlayer = new GridPane();
        gridPaneOpponent = new GridPane();

        String gameName = Singleton.getInstance().getGameName();
        if (Battleship.handler.getRMIClient().connectToServer("game", gameName)) {
            try {
                IGameManager game = Battleship.handler.getRMIClient().getGameManager();
                if (game != null) {
                    this.gameManager = game;
                    if (this.gameManager.getPlayers().size() == 2
                            && Battleship.handler.getPlayingPlayer().equals(gameManager.getPlayers().get(0))) {
                        this.drawStart();
                    }
                    if (Battleship.handler.getPlayingPlayer().equals(gameManager.getPlayers().get(0))) {
                        playingPlayer = gameManager.getPlayers().get(0);
                    }
                    if (Battleship.handler.getPlayingPlayer().equals(gameManager.getPlayers().get(1))) {
                        playingPlayer = gameManager.getPlayers().get(1);
                    }
                    if (playingPlayer.equals(this.gameManager.getPlayers().get(0))) {
                        opponentPlayer = this.gameManager.getPlayers().get(1);
                    } else {
                        opponentPlayer = this.gameManager.getPlayers().get(0);
                    }

                    System.out.println("Print player1's board");
                    this.gameManager.getPlayers().get(0).getPlayer().printBoard();

                    System.out.println("Print player2's board");
                    this.gameManager.getPlayers().get(1).getPlayer().printBoard();

                }
                System.out.println("RMI call to game name: " + Battleship.handler.getRMIClient().getSelectedGameRMI(gameName).getName());

                // TODO: Location on selection in gridview instead of random.
                // TODO: Check display on ship hit.
                // http://stackoverflow.com/questions/31095954/how-to-get-gridpane-row-and-column-ids-on-mouse-entered-in-each-cell-of-grid-in
                serviceGameRunner = Executors.newSingleThreadScheduledExecutor();
                serviceGameRunner.scheduleAtFixedRate(new GameRunner(), 0, 20, TimeUnit.SECONDS);

                // Set player 1 as first turn.
                if (!this.gameManager.getPlayers().get(0).isTurn()) {
                    for (int i = 0; i < this.gameManager.getPlayers().size(); i++) {
                        if (this.playingPlayer.equals(this.gameManager.getPlayers().get(i))) {
                            this.gameManager.changeTurn(this.gameManager.getPlayers().get(i));
                            System.out.println(this.gameManager.getPlayerTurn(this.gameManager.getPlayers().get(i)));
                            //Battleship.handler.getRMIClient().bindToServer("GameUpdate", this.gameManager);
                            lblPlayer2Turn.setVisible(false);
                        }
                    }
                }

                drawBoards(gameManager);
                this.showGrids();
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private class GameRunner implements Runnable {

        @Override
        public void run() {
            String gameName = Singleton.getInstance().getGameName();
            System.out.println("GameRunner run.");
            try {
                gameManager = Battleship.handler.getRMIClient().getSelectedGameRMI(gameName);

                System.out.println("\nRunner Thread: " + gameManager.getName() + ", overview player 1: \n");
                Overview overviewP1 = gameManager.getPlayers().get(0).getPlayer();
                overviewP1.printBoard();
                System.out.println("\nRunner Thread: " + gameManager.getName() + ", overview player 2: \n");
                Overview overviewP2 = gameManager.getPlayers().get(1).getPlayer();
                overviewP2.printBoard();

                if (gameManager.getPlayerTurn(gameManager.getPlayers().get(0))) {
                    lblPlayer1Turn.setVisible(true);
                    lblPlayer2Turn.setVisible(false);
                } else {
                    lblPlayer1Turn.setVisible(false);
                    lblPlayer2Turn.setVisible(true);
                }
                drawBoards(gameManager);
                //                if (runGame != null) {
                //                    gameManager = runGame;
                //                    Platform.runLater(new Runnable() {
                //                        public void run() {
                //                            drawBoards(gameManager);
                //                        }
                //                    }
                //                    );
                //                    System.out.println(
                //                            "Runner Thread: " + runGame.getName() + ", overview player 1: " + runGame.getPlayers().get(0).getPlayer());
                //                    System.out.println(
                //                            "Runner Thread: " + runGame.getName() + ", overview player 2: " + runGame.getPlayers().get(1).getPlayer());
                //
                //                }
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    public void handlePlaceShipButton(ActionEvent e) {

        Random random = new Random();
        int x = random.nextInt(14);
        int y = random.nextInt(14);
        int[] location = new int[2];
        location[0] = x + 1;
        location[1] = y + 1;

        try {
            //for (IPlayer playerLoop : this.gameManager.getPlayers()) {
            //  if (playerLoop.equals(playingPlayer)) {
            System.out.println("Print playing player's board before ship placement.\n");
            playingPlayer.getPlayer().printBoard();
            System.out.println(this.gameManager.getName());
            if (this.gameManager.getPlayerTurn(playingPlayer)) {
                if (this.gameManager.placeShip(playingPlayer, location, 3, 0)) {
                    this.drawBoards(this.gameManager);
                    System.out.println("Print player1's board");
                    this.gameManager.getPlayers().get(0).getPlayer().printBoard();

                    System.out.println("Print player2's board");
                    this.gameManager.getPlayers().get(1).getPlayer().printBoard();
                    if (playingPlayer.getPlayer().amountOfShips() < 7) {
                        this.gameManager.changeTurn(playingPlayer);
                    }

                } else {
                    // TODO: Error Message.
                }
            }
            //}
            //}
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BattleshipExceptions ex) {
            Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleFireTorpedoButton(ActionEvent e) {
        Random random = new Random();
        int x = random.nextInt(16);
        int y = random.nextInt(16);
        int[] location = new int[2];
        location[0] = x;
        location[1] = y;
        try {
            IPlayer opponentPlayer = null;
            for (IPlayer playerLoop : this.gameManager.getPlayers()) {
                if (!playingPlayer.equals(playerLoop)) {
                    opponentPlayer = playerLoop;
                }
            }
            if (this.gameManager.getPlayerTurn(playingPlayer) && opponentPlayer != null) {
                if (this.gameManager.fireTorpedo(playingPlayer, opponentPlayer, "TorpedoTemp", location)) {
                    this.drawBoards(this.gameManager);

                    this.gameManager.changeTurn(playingPlayer);
                    //Battleship.handler.getRMIClient().bindToServer("GameUpdate", this.gameManager);

                    System.out.println("Print player1's board");
                    this.gameManager.getPlayers().get(0).getPlayer().printBoard();

                    System.out.println("Print player2's board");
                    this.gameManager.getPlayers().get(1).getPlayer().printBoard();
                } else {
                    // TODO: Error Message.
                }

            }
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleEndPlayerTurnButton(ActionEvent e) {
        try {
            for (IPlayer playerLoop : this.gameManager.getPlayers()) {
                this.gameManager.changeTurn(playerLoop);
                //Battleship.handler.getRMIClient().bindToServer("GameUpdate", this.gameManager);
            }
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
            if (playingPlayer.equals(game.getPlayers().get(0))) {
                playingPlayer = game.getPlayers().get(0);
                opponentPlayer = game.getPlayers().get(1);
            } else {
                playingPlayer = game.getPlayers().get(1);
                opponentPlayer = this.gameManager.getPlayers().get(0);
            }
            Overview overviewPlayer = playingPlayer.getPlayer();
            drawBoard(overviewPlayer, true);

            Overview overviewOpponent = opponentPlayer.getPlayer();
            drawBoard(overviewOpponent, false);
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*
         try {
         for (int i = 0; i < game.getOverviews().size(); i++) {
         if (game.getPlayers().get(0).equals(this.playingPlayer)) {
         if (i == 0) { // Overview of Player1
         Overview overview = game.getOverviews().get(i);
         if (game.getPlayers().get(0).getPlayer().equals(overview)) {
         System.out.println("DrawBoards after action player 1 | Player 1 board\n");
         overview.printBoard();
         drawBoard(overview, true);
         }
         }
         if (i == 1) { // Overview of Player1 Opponent
         Overview overview = game.getOverviews().get(i);
         if (game.getPlayers().get(1).getPlayer().equals(overview)) {
         System.out.println("");
         System.out.println("DrawBoards after action player 1 | Player 1 Opponent board (Player 2 board)\n");

         overview.printBoard();
         drawBoard(overview, false);
         }

         }
         }
         if (game.getPlayers().get(1).equals(this.playingPlayer)) {
         if (i == 2) { // Overview Player2
         Overview overview = game.getOverviews().get(i);
         if (game.getPlayers().get(1).getPlayer().equals(overview)) {
         System.out.println("");
         System.out.println("DrawBoards after action player 2 | Player 2 board\n");

         overview.printBoard();
         drawBoard(overview, true);
         }
         }
         if (i == 3) { // Overview of Player2 Opponent
         Overview overview = game.getOverviews().get(i);
         if (game.getPlayers().get(1).getOpponent().equals(overview)) {
         System.out.println("");
         System.out.println("DrawBoards after action player 2 | Player 2 Opponent board (Player 1 board)\n");

         overview.printBoard();
         drawBoard(overview, false);
         }
         }
         }
         }

         } catch (RemoteException ex) {
         Logger.getLogger(FXMLGameVC.class.getName()).log(Level.SEVERE, null, ex);
         }*/
    }

    private void drawBoard(Overview overview, boolean isPlayerBoard) {
        if (isPlayerBoard) {
            int[][] board = overview.getBoard();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    Label boardIndex = new Label(Integer.toString(board[i][j]));
                    boardIndex.setAlignment(Pos.CENTER);

                    gridPanePlayer.add(boardIndex, i, j);
                }
            }
        } else {
            int[][] board = overview.getBoard();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    Label boardIndex = new Label(Integer.toString(board[i][j]));
                    boardIndex.setAlignment(Pos.CENTER);

                    gridPaneOpponent.add(boardIndex, i, j);
                }
            }
        }
    }

    private void drawStart() {
//        double canvasHeight = this.canvasPlayer.getHeight();
//        double canvasWidth = this.canvasPlayer.getWidth();
        try {
            if (this.gameManager.getOverviews().size() != 2) {
                this.gameManager.buildOverviewsForPlayers();
                //Battleship.handler.getRMIClient().bindToServer("GameUpdate", this.gameManager);

            }
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

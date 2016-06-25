/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import Battleship.Domain.Account;
import Battleship.Interfaces.IGameManager;
import Battleship.Interfaces.ILobby;
import Battleship.Interfaces.IPlayer;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sebas
 */
public class FXMLLobbyController implements Initializable {

    @FXML
    private Button btnStart;
    @FXML
    private Button btnLeave;
    @FXML
    private Label lblTitle;
    @FXML
    private CheckBox cbSpecials;
    @FXML
    private TextField tfMaxGameTime;
    @FXML
    private TextField tfMaxShips;
    @FXML
    private TextField tfFieldSize;
    @FXML
    private CheckBox cbReady;
    @FXML
    private Label lblPlayer1Name;
    @FXML
    private Label lblPlayer1Score;
    @FXML
    private Label lblPlayer2Name;
    @FXML
    private Label lblPlayer2Score;

    private ILobby lobby;

    private ScheduledExecutorService serviceLobbyRunner;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (Battleship.handler.getRMIClient().connectToServer("lobbyList", null)) {
            this.lobby = Battleship.handler.getRMIClient().getSelectedLobbyRMI(Singleton.getInstance().getLobbyName());
        }
        System.out.println("LobbyController: " + this.lobby.toString());
        btnStart.setDisable(false);

        /*cbReady.setOnAction((event) -> {
         if (!cbReady.isDisabled()) {
         try {
                    
         for (IPlayer playerLoop : this.lobby.getPlayers()) {
         if (playerLoop.getName().equals(Battleship.handler.getLoggedInPlayer().getLoginName())) {
         int index = this.lobby.getPlayers().indexOf(playerLoop);
         boolean check = cbReady.isSelected();
         this.lobby.getPlayers().get(index).setPlayerReady(check);
         System.out.println(this.lobby.getPlayers().get(index).isPlayerReady());
         }
         }
         } catch (RemoteException ex) {
         Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         });*/
        this.loadData();

        serviceLobbyRunner = Executors.newSingleThreadScheduledExecutor();
        serviceLobbyRunner.scheduleAtFixedRate(new LobbyRunner(), 0, 10, TimeUnit.SECONDS);
    }

    private class LobbyRunner implements Runnable {

        @Override
        public void run() {
            ILobby curLobby = lobby;
            ILobby runLobby;
            try {
                runLobby = Battleship.handler.getRMIClient().getSelectedLobbyRMI(curLobby.getName());
                if (runLobby != null) {
                    lobby = runLobby;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            loadData();
                        }
                    });
                    System.out.println("New lobby data loaded." + lobby.getPlayers().size());
                    for (IPlayer p : lobby.getPlayers()) {
                        String output = String.format("Lobby has players: %s", p.toString());
                        System.out.println(output);
                    }
                }
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Runnable Ran. Lobby information: " + lobby);
        }
    }

    @FXML
    public void handleLeaveLobbyButton(ActionEvent e) {
        Account player = Battleship.handler.getLoggedInPlayer();
        try {
            this.lobby.removePlayerFromLobby(player.getLoginName());
            serviceLobbyRunner.shutdownNow();
            this.handleCloseWindow();
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleStartButton(ActionEvent e) {
        try {

            //if (this.lobby.playersReady()) {
            IGameManager tempGame = this.lobby.createGameManager();
            Battleship.handler.getRMIClient().bindToServer("Game", tempGame);
            Battleship.handler.getRMIClient().bindToServer("LobbyUpdate", lobby);
            Singleton.getInstance().setLobbyName(this.lobby.getName());
            Singleton.getInstance().setGameName(tempGame.getName());

            serviceLobbyRunner.shutdownNow();
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                handleOpenGameWindow();
                            } catch (IOException ex) {
                                Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                }
            });
            //}
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void loadData() {
        IPlayer player1 = null;
        IPlayer player2 = null;
        String lobbyName = null;
        boolean playersReady = false;
        try {
            if (lobby.getPlayers().size() == 2) {
                player1 = this.lobby.getPlayers().get(0);
                player2 = this.lobby.getPlayers().get(1);
            }
            lobbyName = this.lobby.getName();
            playersReady = this.lobby.playersReady();
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        final IPlayer finalPlayer1 = player1;
        final IPlayer finalPlayer2 = player2;
        final String finalLobbyName = lobbyName;
        final boolean finalPlayersReady = playersReady;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (finalPlayer1 != null
                        && finalPlayer2 != null
                        && (finalLobbyName != null || !finalLobbyName.isEmpty())) {
                    lblTitle.setText(finalLobbyName);
                    lblPlayer2Name.setText(finalPlayer2.getName());
                    lblPlayer2Score.setText("0");

                    lblPlayer1Name.setText(finalPlayer1.getName());
                    lblPlayer1Score.setText("0");
                    /*if (finalPlayersReady) {
                     btnStart.setDisable(false);
                     }*/

                }
            }
        }
        );
    }

    private void handleCloseWindow() {
        try {
            if (this.lobby.getPlayers().size() == 0) {
                Battleship.handler.getRMIClient().unbindFromServer("Lobby", this.lobby);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Parent window;
        try {
            serviceLobbyRunner.shutdownNow();

            window = FXMLLoader.load(getClass().getResource("FXMLLobbyList.fxml"));
            Battleship.currentStage.getScene().setRoot(window);
        } catch (IOException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void handleOpenGameWindow() throws IOException {
        Parent window;

        window = FXMLLoader.load(getClass().getResource("FXMLGame.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Game: " + lobby.getName());
        stage.setScene(new Scene(window));
        stage.show();
    }
}

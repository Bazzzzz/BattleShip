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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    private Label lblPlayer1Name;
    @FXML
    private Label lblPlayer1Score;
    @FXML
    private Label lblPlayer2Name;
    @FXML
    private Label lblPlayer2Score;

    private String joinedLobbyName;

    private ILobby lobby;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.lobby = Singleton.getInstance().getLobby();
        joinedLobbyName = Battleship.handler.getJoinedLobbyName();
        System.out.println("LobbyController: " + this.lobby.toString());
        this.loadData();

        this.trackChanges();
    }

    @FXML
    public void handleLeaveLobbyButton(ActionEvent e) {
        Account player = Battleship.handler.getLoggedInPlayer();
        try {
            this.lobby.removePlayerFromLobby(player.getLoginName());
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleStartButton(ActionEvent e) {
        try {
            IGameManager game = this.lobby.createGameManager();
            Battleship.handler.getRMIClient().bindToServer("game", game);
            Singleton.getInstance().setLobby(this.lobby);
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadData() {
        try {
            lblTitle.setText(this.lobby.getName());
            Account player1 = Battleship.handler.getLoggedInPlayer();

            if (player1 != null) {
                this.lblPlayer1Name.setText(player1.getLoginName());
                this.lblPlayer1Score.setText(player1.toString());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void trackChanges() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ILobby curLobby = lobby;
                ILobby runLobby = Battleship.handler.getRMIClient().getSelectedLobby(lobby);

                if (!runLobby.equals(curLobby)) {
                    lobby = runLobby;
                    loadData();
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
}

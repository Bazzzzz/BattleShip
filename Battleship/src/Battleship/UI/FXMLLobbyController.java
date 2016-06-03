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

    private ILobby lobby;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if(Battleship.handler.getRMIClient().connectToServer("lobbyList", null)) {
            this.lobby = Battleship.handler.getRMIClient().getSelectedLobbyRMI(Singleton.getInstance().getLobbyName());
        }
        System.out.println("LobbyController: " + this.lobby.toString());
        this.loadData();

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new LobbyRunner(), 0, 10, TimeUnit.SECONDS);
    }

    private class LobbyRunner implements Runnable {

        @Override
        public void run() {
            ILobby curLobby = lobby;
            ILobby runLobby;
            try {

                runLobby = Battleship.handler.getRMIClient().getSelectedLobbyRMI(lobby.getName());
                if (runLobby != null) {
                    lobby = runLobby;
                    loadData();
                    System.out.println("New lobby data loaded." + lobby.getPlayers().size());
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

        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleStartButton(ActionEvent e) {
        try {
            IGameManager game = this.lobby.createGameManager();
            Battleship.handler.getRMIClient().bindToServer("Game", game);
            Battleship.handler.getRMIClient().bindToServer("LobbyUpdate", lobby);
            Singleton.getInstance().setLobbyName(this.lobby.getName());

        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadData() {
        try {
            lblTitle.setText(this.lobby.getName());
            if (lobby.getPlayers().size() == 2) {
                IPlayer player2 = this.lobby.getPlayers().get(1);
                lblPlayer2Name.setText(player2.getName());
                lblPlayer2Score.setText("0");
            }
            IPlayer player1 = this.lobby.getPlayers().get(0);
            lblPlayer1Name.setText(player1.getName());
            lblPlayer1Score.setText("0");
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

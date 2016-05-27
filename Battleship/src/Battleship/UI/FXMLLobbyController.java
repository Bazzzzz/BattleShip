/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import Battleship.Interfaces.ILobby;
import Battleship.Interfaces.IPlayer;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        // TODO
        lobby = Battleship.handler.getSelectedLobby();

        try {
            lblTitle.setText(lobby.getName());
            IPlayer player1 = lobby.getPlayers().get(0);

            if (player1 != null) {
                this.lblPlayer1Name.setText(player1.getName());
                this.lblPlayer1Score.setText(player1.toString());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

     }

}

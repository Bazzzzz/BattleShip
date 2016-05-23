/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import Battleship.Domain.Account;
import Battleship.Exceptions.BattleshipExceptions;
import Battleship.RMI.RMIClient;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sebas
 */
public class FXMLMainController implements Initializable {

    @FXML
    private Button btnPlay;
    @FXML
    private Button btnRegister;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfServerIP;
    @FXML
    private Label lblError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void handlePlayButton(ActionEvent e) throws IOException {
        try {
            RMIClient client = new RMIClient(tfServerIP.getText());
            this.handlePlayAction(client);
        } catch (BattleshipExceptions ex) {
            lblError.setText(ex.getMessage());
        }

    }

    @FXML
    public void handleRegisterButton(ActionEvent e) {
        try {
            RMIClient client = new RMIClient(tfServerIP.getText());
            this.handleRegisterAction();
        } catch (BattleshipExceptions ex) {
            lblError.setText(ex.getMessage());
        }
    }

    private void handlePlayAction(RMIClient client) throws IOException {
        try {
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            if (!username.equals("")) {
                if (!password.equals("")) {
                    this.handleRMIConnection(client);
                    this.loginPlayer(username, password);
                    // TODO: Move into lobby list screen
                    Parent window;
                    window = FXMLLoader.load(getClass().getResource("FXMLLobbyList.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Lobbies");
                    stage.setScene(new Scene(window));
                    stage.show();
                } else {
                    // TODO: Start game without saving account details.
                }
            } else {
                throw new BattleshipExceptions("Fill in a username.");
            }
        } catch (BattleshipExceptions ex) {
            lblError.setText(ex.getMessage());
        }
    }

    private void handleRegisterAction() {
        try {
            if (tfUsername.getText() != null && tfPassword.getText() != null) {
                if (Battleship.handler.newPlayerToDB(tfUsername.getText(), tfPassword.getText())) {
                    // TODO: Confirmation message.
                    System.out.println("Player registered.");
                    lblError.setText("Player registered.");

                } else {
                    throw new BattleshipExceptions("Fill in username and password.");
                }
            }
        } catch (BattleshipExceptions | IllegalArgumentException ex) {
            lblError.setText(ex.getMessage());
        }
    }

    private void handleRMIConnection(RMIClient client) {
        client.connectToServer("lobbies");
        Battleship.handler.setRMIClient(client);
    }

    private void loginPlayer(String username, String password) {
        Battleship.handler.loginPlayer(username, password);
    }
}

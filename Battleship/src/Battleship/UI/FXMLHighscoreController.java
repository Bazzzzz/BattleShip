/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import Battleship.Domain.Account;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author sebas
 */
public class FXMLHighscoreController implements Initializable {

    @FXML
    private ListView lvHighscore;
    @FXML
    private Button btnHome;
    
    private ObservableList<Account> accounts;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accounts = FXCollections.observableArrayList();
        lvHighscore.setItems(accounts);
        
        accounts.setAll(Battleship.handler.getHighscore());
    }    
    @FXML
    public void handleHomeAction(ActionEvent e) {
        try {
            Parent window;
            window = FXMLLoader.load(getClass().getResource("FXMLMain.fxml"));
            Battleship.currentStage.getScene().setRoot(window);
        } catch (IOException ex) {
            Logger.getLogger(FXMLHighscoreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

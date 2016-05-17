/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import Battleship.Domain.Lobby;
import Battleship.Interfaces.IClientManager;
import Battleship.Interfaces.ILobby;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author sebas
 */
public class FXMLLobbyListController implements Initializable {
    @FXML
    private ListView<ILobby> lvLobbies;
    @FXML
    private Button btnJoinLobby;
    @FXML
    private Button btnNewLobby;
    @FXML
    private Label lblGameHost;
    @FXML
    private TextArea taGameSettings;

    private ObservableList<ILobby> obsLobbies;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        obsLobbies = FXCollections.observableArrayList();
        lvLobbies.setItems(obsLobbies);
        
        try {
            this.fillLobbyList();
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(obsLobbies.size() == 0) {
            try {
                this.dummyData();
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }    
    
    private void fillLobbyList() throws RemoteException {
        IClientManager cm = Battleship.handler.getRMIClient().getClientManager();
        
        if(cm != null) {
            obsLobbies.addAll(cm.getLobbies());
        }
    }
    
    private void dummyData() throws RemoteException {
        obsLobbies.add(new Lobby("Bas"));
    }
}

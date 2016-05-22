/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import Battleship.Domain.Lobby;
import Battleship.Domain.Player;
import Battleship.Exceptions.BattleshipExceptions;
import Battleship.Interfaces.IClientManager;
import Battleship.Interfaces.ILobby;
import Battleship.Interfaces.IPlayer;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
        this.obsLobbies = FXCollections.observableArrayList();
        this.lvLobbies.setItems(this.obsLobbies);

        try {
            this.fillLobbyList();
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (this.obsLobbies.size() == 0) {
            try {
                this.dummyData();
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.lvLobbies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ILobby>() {

            @Override
            public void changed(ObservableValue<? extends ILobby> observable, ILobby oldValue, ILobby newValue) {
                try {
                    if (newValue.getPlayers().size() > 0) {
                        lblGameHost.setText(newValue.getPlayers().get(0).getName().toUpperCase());
                        String gameSettings = String.format("Number of players: %d", newValue.getPlayers().size());

                        taGameSettings.setText(gameSettings);
                    } else {
                        throw new BattleshipExceptions("Lobby doesn't exist anymore.");
                    }
                } catch (BattleshipExceptions ex) {
                    System.out.println(ex.getMessage());
                }

            }

        });
    }

    @FXML
    public void handleNewLobbyButton(ActionEvent e) {
        IClientManager cm = Battleship.handler.getRMIClient().getClientManager();
        String lobbyName = Battleship.handler.getLoggedInPlayer().getLoginName();
        try {
            ILobby lobby = new Lobby(lobbyName);
            IPlayer player = new Player(Battleship.handler.getLoggedInPlayer().getLoginName());
            lobby.addPlayer(player);
            cm.addLobby(lobby);
            this.updateLobbyList(lobby, true);

        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void handleJoinLobbyButton(ActionEvent e) {
        try {
            ILobby selectedLobby = (ILobby) this.lvLobbies.getSelectionModel().getSelectedItem();
            if (selectedLobby != null) {
                IClientManager cm = Battleship.handler.getRMIClient().getClientManager();
                IPlayer player = new Player(Battleship.handler.getLoggedInPlayer().getLoginName());
                selectedLobby.addPlayer(player);
                
                cm.updateLobby(selectedLobby);
                
                System.out.println(cm.findLobbyByPlayer(player.getName()));
            } else {
                throw new BattleshipExceptions("Error joining.");
            }
        } catch (BattleshipExceptions ex) {
            System.out.println(ex.getMessage());
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
    /**
     * Initially fills the list of lobbies and on refresh.
     * @throws RemoteException 
     */
    private void fillLobbyList() throws RemoteException {
        IClientManager cm = Battleship.handler.getRMIClient().getClientManager();

        if (cm != null) {
            this.obsLobbies.addAll(cm.getLobbies());
        }
    }
    
    private void updateLobbyList(ILobby lobby, boolean adding) {
        if (adding) {
            this.obsLobbies.add(lobby);
        } else {
            this.obsLobbies.remove(lobby);
        }
        
    }

    private void dummyData() throws RemoteException {
        this.obsLobbies.add(new Lobby("Bas"));
        this.obsLobbies.add(new Lobby("Ter"));
        this.obsLobbies.add(new Lobby("Hans"));

        System.out.println("Lobby list filled with dummy data.");
    }
}

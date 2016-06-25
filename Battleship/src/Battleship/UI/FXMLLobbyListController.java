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
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private Button btnRefresh;
    @FXML
    private Label lblGameHost;
    @FXML
    private TextArea taGameSettings;
    @FXML
    private Button btnHome;

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
                } catch (RemoteException ex) {
                    Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });
    }

    @FXML
    public void handleNewLobbyButton(ActionEvent e) {
        String lobbyName = Battleship.handler.getLoggedInPlayer().getLoginName();
        try {
            ILobby lobby = new Lobby(lobbyName);
            IPlayer player = new Player(Battleship.handler.getLoggedInPlayer().getLoginName());
            lobby.addPlayer(player);
            Battleship.handler.setPlayingPlayer(player);
            Battleship.handler.getRMIClient().bindToServer("Lobby", lobby);

            this.updateLobbyList(lobby, true);
            try {
                Singleton.getInstance().setLobbyName(lobby.getName());
                System.out.println("New lobby Singleton: " + Singleton.getInstance().getLobbyName());

                this.loadLobbyFXML();
            } catch (IOException ex) {
                Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void handleJoinLobbyButton(ActionEvent e) {
        try {
            ILobby selectedLobby = (ILobby) this.lvLobbies.getSelectionModel().getSelectedItem();
            if (selectedLobby != null) {
                IPlayer player = new Player(Battleship.handler.getLoggedInPlayer().getLoginName());
                Battleship.handler.setPlayingPlayer(player);

                if (Battleship.handler.getRMIClient().connectToServer("lobbyList", null)) {
                    ILobby lobby = Battleship.handler.getRMIClient().getSelectedLobbyRMI(selectedLobby.getName());
                    if (lobby != null) {
                        lobby.updateLobby(player, true);
                        this.updateLobbyList(selectedLobby, false);
                        this.updateLobbyList(lobby, true);

                        System.out.println("Joined Lobby: " + lobby.toString() + "\n As: " + player.toString());
                        System.out.println("Lobby players: " + lobby.getPlayers().size());
                        Singleton.getInstance().setLobbyName(lobby.getName());
                        System.out.println("Join lobby Singleton: " + Singleton.getInstance().getLobbyName());
                        try {
                            this.loadLobbyFXML();
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    throw new BattleshipExceptions("Error joining.");
                }
            }
        } catch (BattleshipExceptions ex) {
            System.out.println(ex.getMessage());
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void handleHomeButton(ActionEvent e) {
        Parent window;
        try {
            window = FXMLLoader.load(getClass().getResource("FXMLMain.fxml"));
            Battleship.currentStage.getScene().setRoot(window);
        } catch (IOException ex) {
            Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleRefreshButton(ActionEvent e) {
        try {
            this.fillLobbyList();
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Loads the lobby FXML screen.
     *
     * @throws IOException
     */
    public void loadLobbyFXML() throws IOException {
        Parent window;
        window = FXMLLoader.load(getClass().getResource("FXMLLobby.fxml"));

        Battleship.currentStage.getScene().setRoot(window);
        /*Stage stage = new Stage();
         stage.setTitle("Lobby");
         stage.setScene(new Scene(window));
         stage.show();*/
    }

    /**
     * Initially fills the list of lobbies and on refresh.
     *
     * @throws RemoteException
     */
    private void fillLobbyList() throws RemoteException {
        this.obsLobbies.clear();
        if (Battleship.handler.getRMIClient() != null) {
            if (Battleship.handler.getRMIClient().connectToServer("lobbyList", null)) {
                Collection<ILobby> lobbyList = Battleship.handler.getRMIClient().getLobbyList();
                System.out.println("Lobbies found: " + lobbyList.toString());
                this.obsLobbies.addAll(lobbyList);
            }
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
        this.obsLobbies.clear();
        this.obsLobbies.add(new Lobby("Bas"));
        this.obsLobbies.add(new Lobby("Ter"));
        this.obsLobbies.add(new Lobby("Hans"));

        System.out.println("Lobby list filled with dummy data.");
    }
}

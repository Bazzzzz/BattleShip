/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Battleship.UI;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author sebas
 */
public class Battleship extends Application {
    public static Stage currentStage;
    public static ApplicationHandler handler;

    @Override
    public void start(Stage stage) throws IOException {
        currentStage = stage;
        handler = null;
        Parent root = FXMLLoader.load(getClass().getResource("FXMLMain.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void setApplicationHandler(String ipAddress) {
        Battleship.handler = new ApplicationHandler(ipAddress);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

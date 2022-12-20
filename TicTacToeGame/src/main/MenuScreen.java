/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controllers.Client;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Copy Center
 */
public class MenuScreen extends Application {

    Socket mySocket;
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        MenuScreen.stage = stage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/views/Splach.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                    Client.getInstance().CloseRequest();
                    Client.getInstance().closeClient();
                    
                    System.out.println("close");
                    Platform.exit();
                    System.exit(0);
                } 

            

        });

    }

    public static void main(String[] args) {
        launch(args);
    }

}

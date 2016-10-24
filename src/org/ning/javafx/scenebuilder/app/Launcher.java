/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ning.javafx.scenebuilder.app;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class Launcher extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Pane root=FXMLLoader.load(getClass().getResource("ui/mainContent.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResource("fxml-16.png").toString()));
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle(java.util.ResourceBundle.getBundle("org/ning/javafx/scenebuilder/app/Bundle").getString("appTitle"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

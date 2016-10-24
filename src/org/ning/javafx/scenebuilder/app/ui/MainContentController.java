/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ning.javafx.scenebuilder.app.ui;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.Buffer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class MainContentController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void launchNow(ActionEvent actionEvent) {
        System.err.println("launchNow");
        openJar(null);
    }

    @FXML
    public void selectFileAndLaunch(ActionEvent actionEvent) {
        System.err.println("selectFileAndLaunch");
        openJar(null);
    }

    /**
     *
     * @param fxmlFile
     */
    private void openJar(String fxmlFile) {
        for (String jarPath : new File("lib").list()) {
            System.out.println(jarPath);
            String args = (fxmlFile == null) ? "" : (" " + fxmlFile);
            new Thread() {
                @Override
                public void run() {
                    super.run(); //To change body of generated methods, choose Tools | Templates.
                    try {
                        String temp = "";
                        {
                            OutputStreamWriter os = new OutputStreamWriter(Runtime.getRuntime().exec("ipconfig").getOutputStream());
                            BufferedWriter bufferedWriter = new BufferedWriter(os);
                            bufferedWriter.write(temp);
                        }
                        System.out.println(temp);
                        {
                            OutputStreamWriter os = new OutputStreamWriter(Runtime.getRuntime().exec("java -jar " + jarPath + args).getOutputStream());
                            BufferedWriter bufferedWriter = new BufferedWriter(os);
                            bufferedWriter.write(temp);
                        }
                        System.out.println(temp);
                    } catch (IOException ex) {
                        Logger.getLogger(MainContentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }.start();
        }
    }
}

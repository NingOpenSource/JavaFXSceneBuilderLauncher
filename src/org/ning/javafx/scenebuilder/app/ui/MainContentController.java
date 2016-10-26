/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ning.javafx.scenebuilder.app.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jodd.io.NetUtil;
import org.ning.javafx.scenebuilder.app.utils.OSUtils;

/**
 * FXML Controller class
 *
 * @author ning
 */
public class MainContentController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    LoadAnim anim=new LoadAnim();
                    Stage s=new Stage();
                    s.setAlwaysOnTop(true);
                    anim.start(s);
                } catch (Exception ex) {
                    Logger.getLogger(OSUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @FXML
    public void launchNow(ActionEvent actionEvent) {
        System.err.println("launchNow");
        openJar(null);
    }

    @FXML
    public void onDragOver(DragEvent actionEvent) {
        actionEvent.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    public void onDragDropped(DragEvent actionEvent) {
        Dragboard dragboard = actionEvent.getDragboard();
        for (File file : dragboard.getFiles()) {
            System.out.println(file.getName());
            if (!file.getName().contains(".fxml")) {
                continue;
            }
            openJar(file.getAbsolutePath());
        }
    }

    @FXML
    public void selectFileAndLaunch(ActionEvent actionEvent) {
        System.err.println("selectFileAndLaunch");
        FileChooser chooser = new FileChooser();
        chooser.setTitle(java.util.ResourceBundle.getBundle("org/ning/javafx/scenebuilder/app/Bundle").getString("fxmlChooserTitle"));
//        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("a javaFX gui xml document file.", "fxml"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(java.util.ResourceBundle.getBundle("org/ning/javafx/scenebuilder/app/Bundle").getString("fxmlDesc"), "*.fxml"));
        List<File> files = chooser.showOpenMultipleDialog(new Stage());
        for (File file : files) {
            System.out.println(file.getName());
            if (!file.getName().contains(".fxml")) {
                continue;
            }
            openJar(file.getAbsolutePath());
        }
    }

    /**
     *
     * @param fxmlFile
     */
    private void openJar(String fxmlFile) {
        File jar = findJar();
        System.out.println(jar.getAbsoluteFile());
        String args = (fxmlFile == null) ? "" : (" " + fxmlFile);
        new Thread() {
            @Override
            public void run() {
                super.run(); //To change body of generated methods, choose Tools | Templates.
                String temp = "";
                {
                    temp = new OSUtils().process("java -jar " + jar.getAbsolutePath() + args);
                }
                System.out.println(temp);
            }

        }.start();
    }

    /**
     * 寻找jar包
     *
     * @return
     */
    private File findJar() {
        File file=new File("lib/runJar");
        File temp=new File(file,"sceneBuilder.jar");
        if(temp.exists()){
            return temp;
        }
        for (File jar : file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (!name.contains(".jar")) {
                    return false;
                }
                if (name.contains("jodd")) {
                    return false;
                }
                if (name.contains(".bat")) {
                    return false;
                }
                return true;
            }
        })) {
            return jar;
        }
        return null;
    }
    @FXML
    public void aboutThisProject(){
        try {  
            java.awt.Desktop.getDesktop().browse(new URI("https://github.com/NingOpenSource/JavaFXSceneBuilderLauncher"));
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainContentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainContentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     *更新SceneBuilder
     * @param actionEvent
     */
    @FXML
    public void updateSceneBuilder(ActionEvent actionEvent) {
        try {
            File file=new File("lib/runJar","sceneBuilder.jar");
            if (file.exists()) {
                file.delete();
            }
            
            NetUtil.downloadFile("http://gluonhq.com/download/scene-builder-jar/", file);
            
        } catch (IOException ex) {
            Logger.getLogger(MainContentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 关联fxml文件
     */
    @FXML
    public void associateFxmlDocument(ActionEvent actionEvent) {
        new Thread() {
            @Override
            public void run() {
                super.run(); //To change body of generated methods, choose Tools | Templates.
                String osName = System.getProperty("os.name").toLowerCase();
                if (osName.contains("windows")) {
                    String cmd1 = "assoc .fxml=FxmlDocument";
                    ;
                    String cmd2 = "ftype FxmlDocument="
                            + System.getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe -jar " + findJar().getAbsolutePath() + " %0" //                                + "\"java -jar " + findJar().getAbsolutePath() + "\" %1"
                            ;
                    OSUtils oSUtils = new OSUtils();
                    oSUtils.processAdminOnWindows(cmd1);
                    oSUtils.processAdminOnWindows(cmd2);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, java.util.ResourceBundle.getBundle("org/ning/javafx/scenebuilder/app/Bundle").getString("associateFxmlDocumentAlertTitle"), ButtonType.OK);
                            alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                                @Override
                                public void handle(DialogEvent event) {

                                }

                            });
                            alert.show();
                        }
                    });
                }
            }

        }.start();

    }
}

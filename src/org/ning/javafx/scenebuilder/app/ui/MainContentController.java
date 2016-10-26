/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ning.javafx.scenebuilder.app.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
            File jar=findJar();
            System.out.println(jar.getAbsoluteFile());
            String args = (fxmlFile == null) ? "" : (" " + fxmlFile);
            new Thread() {
                @Override
                public void run() {
                    super.run(); //To change body of generated methods, choose Tools | Templates.
                    String temp = "";
                    {
                        temp = process("java -jar " + jar.getAbsolutePath() + args);
                    }
                    System.out.println(temp);
                }

            }.start();
    }
private File findJar(){
    for (File jar : new File("lib").listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.contains("jodd")){
                    return false;
                }
                return true;
            }
        })) {
        return jar;
    }
    return null;
};
    private String process(String cmd) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = p.getInputStream();
            int i = -1;
            while ((i = inputStream.read()) != -1) {
                baos.write(i);
            }
            inputStream.close();
            return baos.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 用管理员权限执行
     * @param cmd
     * @return 
     */
    private String processAdminOnWindows(String cmd) {
        return process("cmd /C runAs /user:administrator "+cmd);
    }
    /**
     * 用管理员权限执行
     * @param cmd
     * @return 
     */
    private String processAdminOnLinux(String cmd) {
        return process("cmd /C runAs /user:administrator "+cmd);
    }
    /**
     * 用管理员权限执行
     * @param cmd
     * @return 
     */
    private String processAdminOnMac(String cmd) {
        return process("cmd /C runAs /user:administrator "+cmd);
    }
    /**
     * 关联fxml文件
     */
    @FXML
    public void associateFxmlDocument(ActionEvent actionEvent){
        String osName=System.getProperty("os.name").toLowerCase();
        if(osName.contains("windows")){
            System.out.println("this is windows");
            System.out.println("org.ning.javafx.scenebuilder.app.ui.MainContentController.associateFxmlDocument()\n"+processAdminOnWindows("assoc .fxml=FxmlDocument"));
            processAdminOnWindows("ftype FxmlDocument=\"java -jar "+findJar().getAbsolutePath()+"\" %*");
        }
    }
}

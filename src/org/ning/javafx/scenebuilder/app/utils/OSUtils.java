/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ning.javafx.scenebuilder.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.ning.javafx.scenebuilder.app.ui.LoadAnim;

/**
 *
 * @author Ning
 */
public class OSUtils {

    public OSUtils() {
        System.err.println("" + System.getProperty("os.arch"));
    }

    /**
     * 用管理员权限执行
     *
     * @param cmd
     * @return
     */
    private String processAdminOnLinux(String cmd) {
        return process("cmd /C runAs /user:administrator " + cmd);
    }

    /**
     * 用管理员权限执行
     *
     * @param cmd
     * @return
     */
    private String processAdminOnMac(String cmd) {
        return process("cmd /C runAs /user:administrator " + cmd);
    }

    /**
     * 用管理员权限执行
     *
     * @param cmd
     * @return
     */
    public String processAdminOnWindows(String cmd) {
        String nircmd_arch="";
        if (System.getProperty("os.arch").contains("64")) {
            /**
             * win64
             */
            nircmd_arch="nircmd-x64";
        } else {
            /**
             * win32
             */
            nircmd_arch="nircmd";
        }
        File nircmd_home=new File("lib/os/windows/"+nircmd_arch,"nircmd.exe");
        String cmd_ = nircmd_home.getAbsolutePath()+" elevate cmd /C ";
        return process(cmd_ + cmd);
    }

    /**
     * 执行命令
     *
     * @param cmd
     * @return
     */
    public String process(String cmd) {
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
}

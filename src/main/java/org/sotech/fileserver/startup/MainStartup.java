/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sotech.fileserver.startup;

import org.sotech.fileserver.ftp.FTPServer;
import org.sotech.fileserver.sftp.SFTPServer;
import org.sotech.fileserver.socket.SocketServer;

public class MainStartup {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        /**
         * Start Only one server
         */
        SocketServer.startServer();
        FTPServer.start();
        SFTPServer.start();
    }

}

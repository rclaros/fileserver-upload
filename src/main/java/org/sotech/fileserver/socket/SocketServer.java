/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sotech.fileserver.socket;

import org.sotech.fileserver.config.ApplicationConfig;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SocketServer extends Thread {

    private ServerSocket ss;

    public SocketServer(int port) {
        try {
            ss = new ServerSocket(port, 0, InetAddress.getByName(ApplicationConfig.server_ip));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Socket clientSock = ss.accept();
                saveFile(clientSock);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile(Socket clientSock) throws IOException {
        try (DataInputStream dis = new DataInputStream(clientSock.getInputStream())) {
            String fileName = dis.readUTF();
            String root = ApplicationConfig.directory_ftp+"/" + fileName;
            Files.copy(dis, Paths.get(root));
        }
    }

    public static void startServer() {
        SocketServer fs = new SocketServer(26);
        fs.start();
    }
}

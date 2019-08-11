/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sotech.fileserver.ftp;

import org.sotech.fileserver.config.ApplicationConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpReply;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.FtpletContext;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.apache.log4j.BasicConfigurator;

public class FTPServer {

    public static void start() throws Exception {
        BasicConfigurator.configure();
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        UserManager userManager = userManagerFactory.createUserManager();
        List<Authority> authorities = new ArrayList<>();
        authorities.add(new WritePermission());
        BaseUser user = new BaseUser();
        user.setName("your user");
        user.setPassword("your password");
        user.setAuthorities(authorities);
        user.setHomeDirectory(ApplicationConfig.directory_ftp);
        userManager.save(user);
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(21);
        listenerFactory.setServerAddress(ApplicationConfig.server_ip);
        listenerFactory.setDataConnectionConfiguration(new DataConnectionConfigurationFactory().createDataConnectionConfiguration());
        FtpServerFactory factory = new FtpServerFactory();
        factory.setUserManager(userManager);
        factory.addListener("default", listenerFactory.createListener());

        ConnectionConfigFactory connection = new ConnectionConfigFactory();
        connection.setMaxLoginFailures(1000);
        connection.setLoginFailureDelay(1000);
        connection.setAnonymousLoginEnabled(false);
        factory.setConnectionConfig(connection.createConnectionConfig());
        Map<String, Ftplet> m = new HashMap<>();
        m.put("miaFtplet", new Ftplet() {
            @Override
            public void init(FtpletContext ftpletContext) throws FtpException {
                System.out.println("init");
                //System.out.println("Thread #" + Thread.currentThread().getId());
            }

            @Override
            public void destroy() {
                System.out.println("destroy");
                //System.out.println("Thread #" + Thread.currentThread().getId());
            }

            @Override
            public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException {
                //System.out.println("beforeCommand " + session.getUserArgument() + " : " + session.toString() + " | " + request.getArgument() + " : " + request.getCommand() + " : " + request.getRequestLine());
                //System.out.println("Thread #" + Thread.currentThread().getId());
                //do something
                return FtpletResult.DEFAULT;//...or return accordingly
            }

            @Override
            public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply) throws FtpException, IOException {
                //System.out.println("afterCommand " + session.getUserArgument() + " : " + session.toString() + " | " + request.getArgument() + " : " + request.getCommand() + " : " + request.getRequestLine() + " | " + reply.getMessage() + " : " + reply.toString());
                //System.out.println("Thread #" + Thread.currentThread().getId());

                //do something
                return FtpletResult.DEFAULT;//...or return accordingly
            }

            @Override
            public FtpletResult onConnect(FtpSession session) throws FtpException, IOException {
                //System.out.println("onConnect " + session.getUserArgument() + " : " + session.toString());
                //System.out.println("Thread #" + Thread.currentThread().getId());

                //do something
                return FtpletResult.DEFAULT;//...or return accordingly
            }

            @Override
            public FtpletResult onDisconnect(FtpSession session) throws FtpException, IOException {
                //System.out.println("onDisconnect " + session.getUserArgument() + " : " + session.toString());
                //System.out.println("Thread #" + Thread.currentThread().getId());

                //do something
                return FtpletResult.DEFAULT;//...or return accordingly
            }
        });
        factory.setFtplets(m);
        FtpServer server = factory.createServer();
        server.start();
    }

}

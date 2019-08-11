/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sotech.fileserver.sftp;

import org.sotech.fileserver.config.ApplicationConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.UserAuth;
import org.apache.sshd.server.auth.UserAuthPassword;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;

public class SFTPServer {

    public static void start() throws Exception {
        BasicConfigurator.configure();
        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(24);
        sshd.setHost(ApplicationConfig.server_ip);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File(ApplicationConfig.directory+"/signed/filetransfer.dev").getAbsolutePath()));
        sshd.setCommandFactory(new ScpCommandFactory());
        List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
        userAuthFactories.add(new UserAuthPassword.Factory());
        sshd.setUserAuthFactories(userAuthFactories);
        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
            public boolean authenticate(String username, String password, ServerSession session) {
                return "your user".equals(username) && "your password".equals(password);
            }
        });
        VirtualFileSystemFactory vfSysFactory = new VirtualFileSystemFactory();
        vfSysFactory.setDefaultHomeDir(ApplicationConfig.directory_sftp);
        sshd.setFileSystemFactory(vfSysFactory);
        List<NamedFactory<Command>> namedFactoryList = new ArrayList<NamedFactory<Command>>();
        namedFactoryList.add(new org.apache.sshd.server.sftp.SftpSubsystem.Factory());
        sshd.setSubsystemFactories(namedFactoryList);
        sshd.start();
    }

}

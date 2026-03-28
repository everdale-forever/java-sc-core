package com.banaanae.javasccore;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server {
    private ServerSocket server;
    
    public void start(int port) throws IOException {
        server = new ServerSocket(port);
        while (!server.isClosed()) {
            Socket client = server.accept();
            // handle client (in new thread or executor)
        }
    }

    public void stop() throws IOException {
        if (server != null && !server.isClosed()) server.close();
    }
}

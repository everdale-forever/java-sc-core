package com.banaanae.javasccore;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting server");
        
        final Server server = new Server();
        server.start(9339);
    }
}

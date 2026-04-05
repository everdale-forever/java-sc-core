package com.banaanae.javasccore;

import com.banaanae.javasccore.logic.server.LogicConfig;
import java.io.IOException;

public class App {
    public static void main(String[] args)  {
        final Server server = new Server();
        final int port = LogicConfig.PORT;
        
        try {
            server.start(port);
        } catch (IOException ex) {
            System.getLogger(App.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}

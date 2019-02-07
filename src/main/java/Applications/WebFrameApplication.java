package Applications;

import Server.HttpServer;

import java.io.IOException;

/**
 * Main application to put into work the framework
 */
public class WebFrameApplication {
    public static void run(int port, String args[]){
        try {
            HttpServer.main(port, args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

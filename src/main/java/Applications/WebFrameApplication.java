package Applications;

import Server.HttpServer;

import java.io.IOException;

/**
 * Main application to put into work the framework
 */
public class WebFrameApplication {
    public static void run(Class<?> mainClass, int port, String args[]){
        try {
            HttpServer.main(mainClass, port, args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

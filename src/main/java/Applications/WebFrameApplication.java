package Applications;

import Server.HttpServer;

import java.io.IOException;

public class WebFrameApplication {
    public static void run(String args[]){
        try {
            HttpServer.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

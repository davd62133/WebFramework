package Server;
import Annotationes.WebClass;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.net.*;
import java.io.*;

/**
 * The Http Server
 */
public class HttpServer {
    public static void main(Class<?> mainClass, int port, String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(1);
        }
        Class<?> cl = mainClass;
        /**Reflections reflection = new Reflections(mainClass.getPackage().toString());
        for(Class<?> cl2 : reflection.getTypesAnnotatedWith(WebClass.class)){
            cl = cl2;
        }**/

        Socket clientSocket = null;
        while (true) {
            try {
                System.out.println("Ready to receive ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            MultiThread multiThread = new MultiThread(clientSocket, cl);
            multiThread.start();
        }
    }
}



package Server;
import Annotationes.WebClass;
import org.reflections.Reflections;
import java.net.*;
import java.io.*;

/**
 * The Http Server
 */
public class HttpServer {
    public static void main(int port, String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        Class<?> cl = null;
        Reflections reflection = new Reflections();
        for(Class<?> cl2 : reflection.getTypesAnnotatedWith(WebClass.class)){
            cl = cl2;
        }

        Socket clientSocket = null;
        while (true) {
            try {
                System.out.println("Listo para recibir ...");
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



package Server;
import Annotationes.WebClass;
import Annotationes.WebGet;
import org.reflections.Reflections;
import sun.reflect.Reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.io.*;
import java.nio.file.Files;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        //System.out.println(MultiThread.getResource("descarga.png"));
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(/**new Integer(System.getenv("PORT"))**/35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        Class<?> cl = null;
        Reflections reflection = new Reflections();
        for(Class<?> cl2 : reflection.getTypesAnnotatedWith(WebClass.class)){
            //System.out.println(cl.getSimpleName());
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



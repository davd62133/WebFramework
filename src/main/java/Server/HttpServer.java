package Server;
import Annotationes.WebClass;
import Annotationes.WebGet;

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

        Socket clientSocket = null;
        while (true) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            MultiThread multiThread = new MultiThread(clientSocket, args);
            multiThread.start();
        }
    }
}

class MultiThread extends Thread{
    Socket socket;
    String[] args;
    public MultiThread(Socket socket, String[] args){
        this.socket = socket;
        this.args = args;
    }
    static String getResource(String rsc) {
        String val = "";
        System.out.println(rsc);
        try {

            //InputStream i = cLoader.getResourceAsStream(rsc);
            InputStream i = Class.forName("MultiThread").getClassLoader().getResourceAsStream(rsc);
            BufferedReader r = new BufferedReader(new InputStreamReader(i));

            String l;
            while((l = r.readLine()) != null) {
                val = val + l;
            }
            i.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public Method isAnnotationPresent(String path) throws ClassNotFoundException {
        Method method = null;
        for (Method m : Class.forName(args[0]).getMethods()){
            if(m.isAnnotationPresent(WebGet.class) && m.getAnnotation(WebGet.class).value().equals(path)){
                System.out.println("NETROOO");
                method = m;
            }
        }
        return method;
    }



    @Override
    public void run(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine = in.readLine();
            String outputLine;
            String format;
            String result;
            byte[] bytes = null;
            if (inputLine != null) {
                System.out.println(inputLine);
                inputLine = inputLine.split(" ")[1];
                System.out.println(inputLine);
                if (inputLine.endsWith(".html")) {
                    //File file = new File("public/" + inputLine);
                    //FileInputStream fis = new FileInputStream(file);
                    bytes = Files.readAllBytes(new File("public/" + inputLine).toPath());
                    //bytes = MultiThread.class.getResourceAsStream("/public/" + inputLine).;
                    //bytes = getResource(inputLine).getBytes();
                    //bytes = Files.readAllBytes(fis.)
                    result = "" + bytes.length;
                    format = "text/html";
                } else if (inputLine.endsWith(".png")) {
                    bytes = Files.readAllBytes(new File("public/" + inputLine).toPath());
                    //bytes = getResource(inputLine.replace("/","")).getBytes();
                    result = "" + bytes.length;
                    format = "image/png";
                } else if (inputLine.endsWith(".jpg")) {
                    bytes = Files.readAllBytes(new File("public/" + inputLine).toPath());
                    //bytes = getResource(inputLine.replace("/","")).getBytes();
                    result = "" + bytes.length;
                    format = "image/jpg";
                }else if((isAnnotationPresent(inputLine))!=null){
                    isAnnotationPresent(inputLine).invoke(null);
                    bytes = ((String) isAnnotationPresent(inputLine).invoke(null)).getBytes();
                    result = "" + bytes.length;
                    format = "text/html";
                } else {
                    bytes = Files.readAllBytes(new File("public/index.html").toPath());
                    //bytes = getResource("index.html").getBytes();
                    result = "" + bytes.length;
                    format = "text/html";
                }

            } else {

                bytes = Files.readAllBytes(new File("public/index.html").toPath());
                //bytes = getResource("index.html").getBytes();
                result = "" + bytes.length;
                format = "text/html";
            }
            outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: "
                    + format
                    + "\r\n"
                    + result
                    + "\r\n\r\n";

            byte[] hByte = outputLine.getBytes();
            byte[] rta = new byte[bytes.length + hByte.length];
            for (int i = 0; i < hByte.length; i++) {
                rta[i] = hByte[i];
            }
            for (int i = hByte.length; i < hByte.length + bytes.length; i++) {
                rta[i] = bytes[i - hByte.length];
            }
            socket.getOutputStream().write(rta);
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
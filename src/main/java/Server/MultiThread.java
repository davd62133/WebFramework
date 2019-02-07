package Server;

import Annotationes.WebGet;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.file.Files;

public class MultiThread extends Thread {
    Socket socket;
    String[] args;
    Class<?> cl;

    public MultiThread(Socket socket, Class<?> cl) {
        this.socket = socket;
        this.args = args;
        this.cl = cl;
    }


    public Method isAnnotationPresent(String path) {
        Method method = null;
        for (Method m : cl.getMethods()) {
            if (m.isAnnotationPresent(WebGet.class) && m.getAnnotation(WebGet.class).value().equals(path)) {
                method = m;
            }
        }
        return method;
    }


    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine = in.readLine();
            String outputLine;
            String format;
            String result;
            byte[] bytes = null;
            if (inputLine != null) {
                inputLine = inputLine.split(" ")[1];
                if (inputLine.endsWith(".html")) {
                    bytes = Files.readAllBytes(new File("src/main/public/" + inputLine).toPath());
                    result = "" + bytes.length;
                    format = "text/html";
                } else if (inputLine.endsWith(".png")) {
                    bytes = Files.readAllBytes(new File("src/main/public/" + inputLine).toPath());
                    result = "" + bytes.length;
                    format = "image/png";
                } else if (inputLine.endsWith(".jpg")) {
                    bytes = Files.readAllBytes(new File("src/main/public/" + inputLine).toPath());
                    result = "" + bytes.length;
                    format = "image/jpg";
                } else if (inputLine.endsWith(".js")) {
                    bytes = Files.readAllBytes(new File("src/main/public/" + inputLine).toPath());
                    result = "" + bytes.length;
                    format = "text/js";
                } else if (inputLine.endsWith(".css")) {
                    bytes = Files.readAllBytes(new File("src/main/public/" + inputLine).toPath());
                    result = "" + bytes.length;
                    format = "text/css";
                } else if ((isAnnotationPresent(inputLine)) != null) {
                    isAnnotationPresent(inputLine).invoke(null);
                    bytes = ((String) isAnnotationPresent(inputLine).invoke(null)).getBytes();
                    result = "" + bytes.length;
                    format = "text/html";
                } else if (inputLine.endsWith("/")  ||  inputLine.endsWith("index.html")) {
                     bytes = Files.readAllBytes(new File("src/main/public/index.html").toPath());
                     result = "" + bytes.length;
                     format = "text/html";
                } else {
                    throw new IOException();
                }
            } else {
                bytes = Files.readAllBytes(new File("src/main/public/index.html").toPath());
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
        } catch (IOException e) {
            String outputLine = "HTTP/1.1 404 Not Found\r\n";

            try {
                socket.getOutputStream().write(outputLine.getBytes());
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();

        }
    }
}
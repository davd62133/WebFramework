package Server;

import Annotationes.WebGet;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Multi Solictud of coneccions
 */
public class MultiThread extends Thread {
    Socket socket;
    String[] args;
    Class<?> cl;

    public MultiThread(Socket socket, Class<?> cl) {
        this.socket = socket;
        this.args = args;
        this.cl = cl;
    }

    /**
     * Returns a method if exists with the annotation and the path
     * @param path the path to the app
     * @return the method, or ull if it doesnt exist
     */
    public Method isAnnotationPresent(String path) {
        Method method = null;
        for (Method m : cl.getMethods()) {
            if (m.isAnnotationPresent(WebGet.class) && m.getAnnotation(WebGet.class).value().equals(path)) {
                method = m;
            }
        }
        return method;
    }

    /**
     * The map of the parametrs with there values
     * @param parameters string of all the ingformacion (URL)
     * @return Map<String, object>
     */
    public static Map<String, Object> getParametersMap(String parameters)
    {
        String[] params = parameters.split("&");
        Map<String, Object> map = new HashMap<String, Object>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }


    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine = in.readLine();
            String outputLine;
            String format;
            String result;
            Map<String, Object> parameters = null;
            byte[] bytes = null;
            if (inputLine != null) {
                inputLine = inputLine.split(" ")[1];
                if(inputLine.contains("?")){
                    parameters = getParametersMap(inputLine.split("\\?")[1]);
                    inputLine =  inputLine.split("\\?")[0];
                }
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
                    //isAnnotationPresent(inputLine).invoke(null);
                    if (parameters != null) {
                        bytes = ((String) isAnnotationPresent(inputLine).invoke(cl.newInstance(), parameters.values().toArray())).getBytes();
                    } else{
                        bytes = ((String) isAnnotationPresent(inputLine).invoke(null)).getBytes();
                    }
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
        } catch (IOException  e) {
            String outputLine = "HTTP/1.1 404 Not Found\r\n";

            try {
                socket.getOutputStream().write(outputLine.getBytes());
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();

        }
    }
}
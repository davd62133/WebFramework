import Annotationes.WebClass;
import Annotationes.WebGet;
import Server.HttpServer;

import java.io.IOException;

@WebClass
public class Prueba {
    public static void main(String args[]){
        try {
            HttpServer.main(new String[]{"Prueba"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @WebGet("/hola")
    public static String prueba(){
        return "HOLAAAAAa";
    }
}

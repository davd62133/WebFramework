package prueba;

import Annotationes.WebClass;
import Annotationes.WebGet;
import Applications.WebFrameApplication;
import Server.HttpServer;

import java.io.IOException;

@WebClass
public class Prueba {


    @WebGet("/hola")
    public static String prueba(){
        return "HOLAAAAAa";
    }
}

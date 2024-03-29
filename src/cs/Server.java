package cs;

import org.apache.xmlrpc.*;

// Levanta el servidor principal, basado en RPC para funcionar como intermadiario entre el usuario y los servidores secundarios
public class Server {        
    public static void main(String[] args){
        try{
            System.out.println("Iniciando el servidor");
            
            WebServer wbServer = new WebServer(8080);
            Funcionalidad fun = new Funcionalidad();
            wbServer.addHandler("myServer", fun);
        
            wbServer.start();
            
            System.out.println("Inicio exitoso del servidor principal\n");
        }catch(Exception ex){
            System.err.println("Server: " + ex);
        }
        
    }
}

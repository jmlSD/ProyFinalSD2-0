package serverrpc;

import org.apache.xmlrpc.*;


public class Server_rpc {
    WebServer wbServer;   
    
    //  Levanta el servidor RPC, agrega la interfaz y lo inicia
    public void executeRPC(){
        try{
            
            wbServer = new WebServer(8081);
            InterfazRPC opMat = new InterfazRPC();
            wbServer.addHandler("myServerRPC", opMat);
        
            wbServer.start();
            System.out.println("Inicio exitoso del server RPC \n");
        }catch(Exception ex){
            System.err.println("Server: " + ex);
        }        
    }
    
    // Apaga el servidor sin borra el manejador
    public void apagaRPC(){        
        wbServer.shutdown();
    }
}

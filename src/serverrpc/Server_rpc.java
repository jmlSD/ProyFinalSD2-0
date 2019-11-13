package serverrpc;

import org.apache.xmlrpc.*;

public class Server_rpc {
    WebServer wbServer;
    //public static void main(String[] args){    
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
    public void apagaRPC(){        
        //wbServer.removeHandler("myServerRPC");
        wbServer.shutdown();
    }
}

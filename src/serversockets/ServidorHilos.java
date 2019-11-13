package serversockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorHilos {
    ServerSocket ssk;
    Socket sk;

    public void levantaSCK() throws IOException{
        ssk = new ServerSocket(8084);
    }

    public void apagaSCK() throws IOException{
        ssk.close();
        sk.close();
        System.out.println("Servidor Sockets apagado \n");
    }
        
    public void executeSCK(int opc){
        try {
            int id = 1;
                
            sk = ssk.accept();
            System.out.println("Servidor Sockets arriba, conexion de: " + sk.getInetAddress().getHostName());  
            Runnable r = new ManejadorHilos(sk, id, opc);
            Thread t = new Thread(r);
            t.start();
            id++;
        
        }catch (IOException ex) {
        Logger.getLogger(ServidorHilos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void executeSCK(int opc, Socket sk1) throws InterruptedException{
        int id = 1;
        
        Runnable r = new ManejadorHilos(sk, id, opc);
        Thread t = new Thread(r);
        t.start();
        t.join();
        id++;
    }
  
}

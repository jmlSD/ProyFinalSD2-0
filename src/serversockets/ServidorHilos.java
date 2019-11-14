package serversockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorHilos {
    ServerSocket ssk;
    Socket sk;
    int id = 1;

    public void levantaSCK() throws IOException{
        ssk = new ServerSocket(8084);
    }
    
    //cierra la conexion del servidor y del cliente
    public void apagaSCK() throws IOException{
        ssk.close();
        sk.close();
        System.out.println("Servidor Sockets apagado \n");
    }
        
    // acepta la conexion entrante, y ejecuta el manejador de hilos
    public void executeSCK(int opc){
        try {
            
            sk = ssk.accept();
            System.out.println("Inicio exitoso del server Sockets, conexion de: " + sk.getInetAddress().getHostName() + "\n");
            Runnable r = new ManejadorHilos(sk, id, opc);
            Thread t = new Thread(r);
            t.start();
            id++;
        
        }catch (IOException ex) {
        Logger.getLogger(ServidorHilos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Ya no realiza conexxion, solo ejecuta manejador de hilos
    public void executeSCK(int opc, Socket sk1) throws InterruptedException{
        
        Runnable r = new ManejadorHilos(sk, id, opc);
        Thread t = new Thread(r);
        t.start();
        t.join();
        id++;
    }
  
}

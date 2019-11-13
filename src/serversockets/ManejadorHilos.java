package serversockets;

import funciontareas.*;
import java.io.*;
import java.net.Socket;
import java.util.logging.*;

public class ManejadorHilos implements Runnable {
    
    private DataInputStream dis;
    private DataOutputStream dos;
    private FileWriter fileW;
    private Socket sk;
    private int id;
    private int aux, opc;

    public ManejadorHilos(Socket sk, int id,int aux) {
        this.sk = sk;
        this.id = id;
        this.aux = aux;
    }
    
    @Override 
    public void run(){
        //System.out.println("conexion SCK " + id + " recibida--->"+ sk.getInetAddress().getHostName());
        try {         
            this.dis = new DataInputStream(sk.getInputStream());
            this.dos = new DataOutputStream((sk.getOutputStream()));
            
            if(this.aux == 0){
                dos.writeUTF("Elige una opcion:");
                dos.writeUTF("1.- Buscar una letra");
                dos.writeUTF("2.- Buscar una palabra");
                dos.writeUTF("3.- Salir");  
                
            }else if(this.aux==1){
                
            
            }else if(this.aux == 2){
                opc = dis.readInt();
                String dato = dis.readUTF();
                
                switch(opc){
                    case 1:

                        Tarea3A p = new Tarea3A();
                        int res = p.tareaA(dato);
                        dos.writeInt(res);
                        System.out.println("El numero de coincidencias es: " + res);
                        break;
                        
                    case 2:

                        Tarea3B h = new Tarea3B();
                        h.tareaB(dato);
                        res = h.getApariciones();
                        dos.writeInt(res);
                        System.out.println("El numero de coincidencias es: " + res);
                        break;
                        
                    case 3:
                        break;
                    default:
                        dos.writeUTF("La opción ingresada no es válida");
                        break;
                }   
            }

                
        }catch (IOException ex){
            Logger.getLogger(ManejadorHilos.class.getName()).log(Level.SEVERE, null, ex);
        }catch (InterruptedException ex){
            Logger.getLogger(ManejadorHilos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    
    
    


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

    public ManejadorHilos(Socket sk, int id, int aux) {
        this.sk = sk;
        this.id = id;
        this.aux = aux;
    }
    
    
    //  Se encarga de establecer los flujos de comunicacion, y ejecuta las tareas 3A y 3B, por la variable aux
    @Override 
    public void run(){
        try {         
            this.dis = new DataInputStream(sk.getInputStream());
            this.dos = new DataOutputStream((sk.getOutputStream()));
            
            if(this.aux == 0){  // Solo manda el menu
                dos.writeUTF("Elige una opcion:");
                dos.writeUTF("1.- Buscar una letra");
                dos.writeUTF("2.- Buscar una palabra");
                dos.writeUTF("3.- Salir");  
                
            }else if(this.aux==1){  //Se utiliza como comodin
                
            
            }else if(this.aux == 2){ //lee la opcion, el dato y lo procesa
                opc = dis.readInt();
                String dato = dis.readUTF();
                
                switch(opc){
                    case 1:

                        Tarea3A p = new Tarea3A();  // procesa letra
                        int res = p.tareaA(dato);
                        dos.writeInt(res);
                        System.out.println("El numero de coincidencias de "+dato+" es: " + res);
                        break;
                        
                    case 2:

                        Tarea3B h = new Tarea3B();  // procesa palabra
                        h.tareaB(dato);
                        res = h.getApariciones();
                        dos.writeInt(res);
                        System.out.println("El numero de coincidencias de "+dato+" es: " + res);
                        break;
                        
                    case 3:
                        break;  // Se hace un break en caso de salir
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
    
    
    

